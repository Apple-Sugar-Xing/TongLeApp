package com.tongle.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.tongle.app.MainActivity
import com.tongle.app.R
import com.tongle.app.data.model.Song
import com.tongle.app.data.model.Story
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@AndroidEntryPoint
class AudioPlaybackService : Service() {

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "audio_playback_channel"
    }

    @Inject
    lateinit var exoPlayer: ExoPlayer

    private val binder = AudioServiceBinder()
    private var timer: CountDownTimer? = null
    private var sleepModeEnabled = true
    private var originalVolume = 0
    private var audioManager: AudioManager? = null

    // 播放状态
    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState

    // 定时器状态
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        originalVolume = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0

        // 设置ExoPlayer监听器
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_READY -> {
                        _playbackState.value = _playbackState.value.copy(
                            isPlaying = exoPlayer.isPlaying,
                            duration = exoPlayer.duration,
                            isBuffering = false
                        )
                        updateNotification()
                    }
                    Player.STATE_BUFFERING -> {
                        _playbackState.value = _playbackState.value.copy(
                            isBuffering = true
                        )
                    }
                    Player.STATE_ENDED -> {
                        _playbackState.value = _playbackState.value.copy(
                            isPlaying = false,
                            isEnded = true
                        )
                        updateNotification()
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playbackState.value = _playbackState.value.copy(
                    isPlaying = isPlaying
                )
                updateNotification()
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        cancelTimer()
        exoPlayer.release()
        super.onDestroy()
    }

    // 播放故事
    fun playStory(story: Story) {
        _playbackState.value = PlaybackState(
            title = story.title,
            description = story.description,
            coverUrl = story.coverImageUrl,
            contentType = "story",
            contentId = story.id
        )
        
        val mediaItem = if (story.isDownloaded && story.localPath != null) {
            MediaItem.fromUri(story.localPath)
        } else {
            MediaItem.fromUri(story.audioUrl)
        }
        
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        requestAudioFocus()
        updateNotification()
    }

    // 播放儿歌
    fun playSong(song: Song) {
        _playbackState.value = PlaybackState(
            title = song.title,
            description = song.description,
            coverUrl = song.coverImageUrl,
            contentType = "song",
            contentId = song.id
        )
        
        val mediaItem = if (song.isDownloaded && song.localPath != null) {
            MediaItem.fromUri(song.localPath)
        } else {
            MediaItem.fromUri(song.audioUrl)
        }
        
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        requestAudioFocus()
        updateNotification()
    }

    // 播放/暂停
    fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.play()
            requestAudioFocus()
        }
        updateNotification()
    }

    // 停止播放
    fun stop() {
        exoPlayer.stop()
        _playbackState.value = _playbackState.value.copy(
            isPlaying = false
        )
        updateNotification()
    }

    // 跳转到指定位置
    fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }

    // 设置定时器
    fun setTimer(durationMinutes: Int, enableSleepMode: Boolean = true) {
        cancelTimer()
        sleepModeEnabled = enableSleepMode
        
        val durationMillis = durationMinutes * 60 * 1000L
        _timerState.value = TimerState(
            isActive = true,
            totalDuration = durationMillis,
            remainingTime = durationMillis
        )
        
        timer = object : CountDownTimer(durationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timerState.value = _timerState.value.copy(
                    remainingTime = millisUntilFinished
                )
                
                // 如果启用了睡眠模式，并且剩余时间少于1分钟，开始降低音量
                if (sleepModeEnabled && millisUntilFinished < 60000) {
                    val volumePercent = millisUntilFinished / 60000f
                    adjustVolume(volumePercent)
                }
            }
            
            override fun onFinish() {
                _timerState.value = TimerState(isActive = false)
                stop()
                // 恢复原始音量
                if (sleepModeEnabled) {
                    audioManager?.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        originalVolume,
                        0
                    )
                }
            }
        }.start()
    }

    // 取消定时器
    fun cancelTimer() {
        timer?.cancel()
        timer = null
        _timerState.value = TimerState(isActive = false)
        
        // 恢复原始音量
        if (sleepModeEnabled) {
            audioManager?.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                originalVolume,
                0
            )
        }
    }

    // 调整音量（用于睡眠模式）
    private fun adjustVolume(percent: Float) {
        val maxVolume = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 0
        val targetVolume = (originalVolume * percent).toInt().coerceAtLeast(1)
        audioManager?.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            targetVolume,
            0
        )
    }

    // 请求音频焦点
    private fun requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setOnAudioFocusChangeListener { focusChange ->
                    when (focusChange) {
                        AudioManager.AUDIOFOCUS_LOSS -> {
                            exoPlayer.pause()
                        }
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                            exoPlayer.pause()
                        }
                        AudioManager.AUDIOFOCUS_GAIN -> {
                            exoPlayer.play()
                        }
                    }
                }
                .build()
            audioManager?.requestAudioFocus(focusRequest)
        } else {
            @Suppress("DEPRECATION")
            audioManager?.requestAudioFocus(
                { focusChange ->
                    when (focusChange) {
                        AudioManager.AUDIOFOCUS_LOSS -> {
                            exoPlayer.pause()
                        }
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                            exoPlayer.pause()
                        }
                        AudioManager.AUDIOFOCUS_GAIN -> {
                            exoPlayer.play()
                        }
                    }
                },
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

    // 创建通知渠道
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "音频播放",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "用于控制音频播放的通知"
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // 创建通知
    private fun createNotification(): android.app.Notification {
        val state = _playbackState.value
        
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(state.title)
            .setContentText(state.description)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // 需要替换为实际图标
            .setContentIntent(pendingIntent)
            .setOngoing(state.isPlaying)
            .build()
    }

    // 更新通知
    private fun updateNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, createNotification())
    }

    // 服务绑定器
    inner class AudioServiceBinder : Binder() {
        fun getService(): AudioPlaybackService = this@AudioPlaybackService
    }
}

// 播放状态数据类
data class PlaybackState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val isEnded: Boolean = false,
    val duration: Long = 0,
    val title: String = "",
    val description: String = "",
    val coverUrl: String = "",
    val contentType: String = "",
    val contentId: Long = 0
)

// 定时器状态数据类
data class TimerState(
    val isActive: Boolean = false,
    val totalDuration: Long = 0,
    val remainingTime: Long = 0
)
