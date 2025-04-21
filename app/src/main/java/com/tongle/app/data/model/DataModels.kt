package com.tongle.app.data.model

// 故事数据模型
data class Story(
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val audioUrl: String,
    val coverImageUrl: String,
    val duration: Int, // 单位：秒
    val ageRange: String, // 适合年龄段
    val isDownloaded: Boolean = false,
    val localPath: String? = null
)

// 儿歌数据模型
data class Song(
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val audioUrl: String,
    val coverImageUrl: String,
    val duration: Int, // 单位：秒
    val hasLyrics: Boolean,
    val lyricsUrl: String?,
    val isDownloaded: Boolean = false,
    val localPath: String? = null
)

// 歌词数据模型
data class Lyric(
    val songId: Long,
    val timeLines: List<TimeLine>
)

data class TimeLine(
    val startTime: Int, // 单位：毫秒
    val endTime: Int,   // 单位：毫秒
    val text: String
)

// 用户设置
data class UserSettings(
    val timerDuration: Int = 30, // 默认30分钟
    val enableSleepMode: Boolean = true,
    val theme: String = "light",
    val maxVolumeLevel: Int = 100,
    val parentalControlEnabled: Boolean = false,
    val parentalControlPin: String? = null
)

// 用户收藏
data class Favorite(
    val id: Long,
    val contentId: Long,
    val contentType: String, // "story" 或 "song"
    val addedTime: Long
)

// 播放历史
data class PlayHistory(
    val id: Long,
    val contentId: Long,
    val contentType: String, // "story" 或 "song"
    val playTime: Long,
    val playDuration: Int, // 播放了多长时间（秒）
    val completed: Boolean // 是否播放完成
)
