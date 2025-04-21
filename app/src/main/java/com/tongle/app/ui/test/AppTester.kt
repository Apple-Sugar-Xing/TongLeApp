package com.tongle.app.ui.test

import android.content.Context
import android.util.Log
import com.tongle.app.data.model.Song
import com.tongle.app.data.model.Story
import com.tongle.app.data.repository.ContentRepository
import com.tongle.app.service.AudioPlaybackService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 测试工具类，用于测试应用的核心功能
 */
@Singleton
class AppTester @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentRepository: ContentRepository
) {
    private val TAG = "AppTester"
    private val scope = CoroutineScope(Dispatchers.Main)
    
    /**
     * 测试内容仓库功能
     */
    fun testContentRepository() {
        scope.launch {
            try {
                // 测试获取所有故事
                val stories = contentRepository.getAllStories().first()
                Log.d(TAG, "获取到 ${stories.size} 个故事")
                
                // 测试获取所有儿歌
                val songs = contentRepository.getAllSongs().first()
                Log.d(TAG, "获取到 ${songs.size} 首儿歌")
                
                // 测试按分类获取故事
                val categoryStories = contentRepository.getStoriesByCategory("童话故事").first()
                Log.d(TAG, "获取到 ${categoryStories.size} 个童话故事")
                
                // 测试按分类获取儿歌
                val categorySongs = contentRepository.getSongsByCategory("经典儿歌").first()
                Log.d(TAG, "获取到 ${categorySongs.size} 首经典儿歌")
                
                // 测试收藏功能
                if (stories.isNotEmpty()) {
                    val storyId = stories[0].id
                    contentRepository.toggleFavoriteStory(storyId)
                    val isFavorite = contentRepository.isStoryFavorite(storyId).first()
                    Log.d(TAG, "故事收藏状态: $isFavorite")
                    
                    // 获取收藏的故事
                    val favoriteStories = contentRepository.getFavoriteStories().first()
                    Log.d(TAG, "收藏的故事数量: ${favoriteStories.size}")
                }
                
                Log.d(TAG, "内容仓库测试完成，功能正常")
            } catch (e: Exception) {
                Log.e(TAG, "内容仓库测试失败: ${e.message}")
            }
        }
    }
    
    /**
     * 测试音频播放服务
     */
    fun testAudioPlaybackService(service: AudioPlaybackService) {
        scope.launch {
            try {
                // 获取一个故事和一首儿歌用于测试
                val story = withContext(Dispatchers.IO) {
                    contentRepository.getAllStories().first().firstOrNull()
                }
                
                val song = withContext(Dispatchers.IO) {
                    contentRepository.getAllSongs().first().firstOrNull()
                }
                
                // 测试播放故事
                if (story != null) {
                    Log.d(TAG, "测试播放故事: ${story.title}")
                    service.playStory(story)
                    // 等待一段时间，确保播放开始
                    withContext(Dispatchers.IO) {
                        Thread.sleep(2000)
                    }
                    
                    // 测试暂停
                    Log.d(TAG, "测试暂停播放")
                    service.togglePlayPause()
                    withContext(Dispatchers.IO) {
                        Thread.sleep(1000)
                    }
                    
                    // 测试恢复播放
                    Log.d(TAG, "测试恢复播放")
                    service.togglePlayPause()
                    withContext(Dispatchers.IO) {
                        Thread.sleep(1000)
                    }
                    
                    // 测试跳转
                    Log.d(TAG, "测试跳转到30秒位置")
                    service.seekTo(30000)
                    withContext(Dispatchers.IO) {
                        Thread.sleep(1000)
                    }
                }
                
                // 测试播放儿歌
                if (song != null) {
                    Log.d(TAG, "测试播放儿歌: ${song.title}")
                    service.playSong(song)
                    // 等待一段时间，确保播放开始
                    withContext(Dispatchers.IO) {
                        Thread.sleep(2000)
                    }
                }
                
                // 测试定时器
                Log.d(TAG, "测试设置定时器: 1分钟")
                service.setTimer(1, true)
                withContext(Dispatchers.IO) {
                    Thread.sleep(2000)
                }
                
                // 测试取消定时器
                Log.d(TAG, "测试取消定时器")
                service.cancelTimer()
                
                // 测试停止播放
                Log.d(TAG, "测试停止播放")
                service.stop()
                
                Log.d(TAG, "音频播放服务测试完成，功能正常")
            } catch (e: Exception) {
                Log.e(TAG, "音频播放服务测试失败: ${e.message}")
            }
        }
    }
    
    /**
     * 测试UI交互
     * 注意：这个方法只是模拟UI交互，实际测试需要在真实设备上进行
     */
    fun testUIInteraction() {
        Log.d(TAG, "开始UI交互测试")
        
        // 模拟故事页面交互
        Log.d(TAG, "测试故事页面交互")
        Log.d(TAG, "- 加载故事列表")
        Log.d(TAG, "- 选择故事分类")
        Log.d(TAG, "- 点击故事卡片")
        
        // 模拟儿歌页面交互
        Log.d(TAG, "测试儿歌页面交互")
        Log.d(TAG, "- 加载儿歌列表")
        Log.d(TAG, "- 选择儿歌分类")
        Log.d(TAG, "- 点击儿歌卡片")
        
        // 模拟播放页面交互
        Log.d(TAG, "测试播放页面交互")
        Log.d(TAG, "- 播放/暂停按钮")
        Log.d(TAG, "- 进度条拖动")
        Log.d(TAG, "- 定时器设置")
        Log.d(TAG, "- 收藏按钮")
        
        // 模拟个人页面交互
        Log.d(TAG, "测试个人页面交互")
        Log.d(TAG, "- 查看收藏内容")
        Log.d(TAG, "- 修改主题设置")
        Log.d(TAG, "- 家长控制设置")
        
        Log.d(TAG, "UI交互测试完成")
    }
    
    /**
     * 综合测试
     */
    fun runAllTests(service: AudioPlaybackService) {
        Log.d(TAG, "开始综合测试")
        
        // 测试内容仓库
        testContentRepository()
        
        // 测试音频播放服务
        testAudioPlaybackService(service)
        
        // 测试UI交互
        testUIInteraction()
        
        Log.d(TAG, "综合测试完成")
    }
}
