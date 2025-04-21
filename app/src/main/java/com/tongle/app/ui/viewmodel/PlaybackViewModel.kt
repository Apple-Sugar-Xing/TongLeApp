package com.tongle.app.ui.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tongle.app.data.model.Song
import com.tongle.app.data.model.Story
import com.tongle.app.data.repository.ContentRepository
import com.tongle.app.data.repository.UserSettingsRepository
import com.tongle.app.service.AudioPlaybackService
import com.tongle.app.service.PlaybackState
import com.tongle.app.service.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentRepository: ContentRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    // 服务连接状态
    private val _isServiceConnected = MutableStateFlow(false)
    val isServiceConnected: StateFlow<Boolean> = _isServiceConnected.asStateFlow()

    // 播放状态
    private val _playbackState = MutableStateFlow<PlaybackState?>(null)
    val playbackState: StateFlow<PlaybackState?> = _playbackState.asStateFlow()

    // 定时器状态
    private val _timerState = MutableStateFlow<TimerState?>(null)
    val timerState: StateFlow<TimerState?> = _timerState.asStateFlow()

    // 当前内容是否已收藏
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    // 用户设置
    private val _userSettings = MutableStateFlow<UserSettings?>(null)
    val userSettings: StateFlow<UserSettings?> = _userSettings.asStateFlow()

    // 音频服务
    private var audioService: AudioPlaybackService? = null

    // 服务连接
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as AudioPlaybackService.AudioServiceBinder
            audioService = binder.getService()
            _isServiceConnected.value = true

            // 订阅播放状态
            viewModelScope.launch {
                audioService?.playbackState?.collectLatest { state ->
                    _playbackState.value = state
                    
                    // 检查当前内容是否已收藏
                    if (state.contentId > 0) {
                        checkFavoriteStatus(state.contentType, state.contentId)
                    }
                }
            }

            // 订阅定时器状态
            viewModelScope.launch {
                audioService?.timerState?.collectLatest { state ->
                    _timerState.value = state
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioService = null
            _isServiceConnected.value = false
        }
    }

    init {
        // 绑定服务
        bindAudioService()
        
        // 加载用户设置
        loadUserSettings()
    }

    // 绑定音频服务
    private fun bindAudioService() {
        val intent = Intent(context, AudioPlaybackService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        context.startService(intent)
    }

    // 加载用户设置
    private fun loadUserSettings() {
        viewModelScope.launch {
            userSettingsRepository.userSettings.collectLatest { settings ->
                _userSettings.value = UserSettings(
                    timerDuration = settings.timerDuration,
                    enableSleepMode = settings.enableSleepMode
                )
            }
        }
    }

    // 检查收藏状态
    private fun checkFavoriteStatus(contentType: String, contentId: Long) {
        viewModelScope.launch {
            if (contentType == "story") {
                contentRepository.isStoryFavorite(contentId).collectLatest { isFav ->
                    _isFavorite.value = isFav
                }
            } else if (contentType == "song") {
                contentRepository.isSongFavorite(contentId).collectLatest { isFav ->
                    _isFavorite.value = isFav
                }
            }
        }
    }

    // 播放故事
    fun playStory(story: Story) {
        audioService?.playStory(story)
    }

    // 播放儿歌
    fun playSong(song: Song) {
        audioService?.playSong(song)
    }

    // 播放/暂停
    fun togglePlayPause() {
        audioService?.togglePlayPause()
    }

    // 停止播放
    fun stop() {
        audioService?.stop()
    }

    // 跳转到指定位置
    fun seekTo(position: Long) {
        audioService?.seekTo(position)
    }

    // 设置定时器
    fun setTimer(durationMinutes: Int, enableSleepMode: Boolean = true) {
        audioService?.setTimer(durationMinutes, enableSleepMode)
        
        // 更新用户设置
        viewModelScope.launch {
            userSettingsRepository.updateTimerDuration(durationMinutes)
            userSettingsRepository.updateSleepMode(enableSleepMode)
        }
    }

    // 取消定时器
    fun cancelTimer() {
        audioService?.cancelTimer()
    }

    // 切换收藏状态
    fun toggleFavorite() {
        val state = _playbackState.value ?: return
        if (state.contentId <= 0) return
        
        viewModelScope.launch {
            if (state.contentType == "story") {
                contentRepository.toggleFavoriteStory(state.contentId)
            } else if (state.contentType == "song") {
                contentRepository.toggleFavoriteSong(state.contentId)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // 解绑服务
        context.unbindService(serviceConnection)
    }
}

// 用户设置数据类（UI层使用）
data class UserSettings(
    val timerDuration: Int = 30,
    val enableSleepMode: Boolean = true
)
