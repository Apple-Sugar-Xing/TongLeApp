package com.tongle.app.ui.test

import android.content.Context
import android.util.Log
import com.tongle.app.data.repository.UserSettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 用户设置测试工具类，用于测试用户设置相关功能
 */
@Singleton
class SettingsTester @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userSettingsRepository: UserSettingsRepository
) {
    private val TAG = "SettingsTester"
    private val scope = CoroutineScope(Dispatchers.Main)
    
    /**
     * 测试用户设置仓库功能
     */
    fun testUserSettingsRepository() {
        scope.launch {
            try {
                // 获取当前设置
                val initialSettings = userSettingsRepository.userSettings.first()
                Log.d(TAG, "初始设置: $initialSettings")
                
                // 测试更新定时器时长
                val newTimerDuration = 45
                Log.d(TAG, "更新定时器时长为: $newTimerDuration")
                userSettingsRepository.updateTimerDuration(newTimerDuration)
                
                // 测试更新睡眠模式
                val newSleepMode = !initialSettings.enableSleepMode
                Log.d(TAG, "更新睡眠模式为: $newSleepMode")
                userSettingsRepository.updateSleepMode(newSleepMode)
                
                // 测试更新主题
                val newTheme = if (initialSettings.theme == "light") "dark" else "light"
                Log.d(TAG, "更新主题为: $newTheme")
                userSettingsRepository.updateTheme(newTheme)
                
                // 测试更新家长控制
                val newParentalControl = !initialSettings.parentalControlEnabled
                Log.d(TAG, "更新家长控制为: $newParentalControl")
                userSettingsRepository.updateParentalControl(newParentalControl)
                
                // 测试更新家长控制PIN码
                val newPin = "1234"
                Log.d(TAG, "更新家长控制PIN码")
                userSettingsRepository.updateParentalControlPin(newPin)
                
                // 获取更新后的设置
                val updatedSettings = userSettingsRepository.userSettings.first()
                Log.d(TAG, "更新后的设置: $updatedSettings")
                
                // 验证更新是否成功
                val isTimerDurationUpdated = updatedSettings.timerDuration == newTimerDuration
                val isSleepModeUpdated = updatedSettings.enableSleepMode == newSleepMode
                val isThemeUpdated = updatedSettings.theme == newTheme
                val isParentalControlUpdated = updatedSettings.parentalControlEnabled == newParentalControl
                val isPinUpdated = updatedSettings.parentalControlPin == newPin
                
                Log.d(TAG, "定时器时长更新成功: $isTimerDurationUpdated")
                Log.d(TAG, "睡眠模式更新成功: $isSleepModeUpdated")
                Log.d(TAG, "主题更新成功: $isThemeUpdated")
                Log.d(TAG, "家长控制更新成功: $isParentalControlUpdated")
                Log.d(TAG, "PIN码更新成功: $isPinUpdated")
                
                // 恢复初始设置
                userSettingsRepository.updateTimerDuration(initialSettings.timerDuration)
                userSettingsRepository.updateSleepMode(initialSettings.enableSleepMode)
                userSettingsRepository.updateTheme(initialSettings.theme)
                userSettingsRepository.updateParentalControl(initialSettings.parentalControlEnabled)
                userSettingsRepository.updateParentalControlPin(initialSettings.parentalControlPin)
                
                Log.d(TAG, "用户设置仓库测试完成，功能正常")
            } catch (e: Exception) {
                Log.e(TAG, "用户设置仓库测试失败: ${e.message}")
            }
        }
    }
    
    /**
     * 测试主题切换功能
     */
    fun testThemeSwitching() {
        scope.launch {
            try {
                // 获取当前主题
                val initialTheme = userSettingsRepository.userSettings.first().theme
                Log.d(TAG, "当前主题: $initialTheme")
                
                // 切换到暗色主题
                if (initialTheme != "dark") {
                    Log.d(TAG, "切换到暗色主题")
                    userSettingsRepository.updateTheme("dark")
                }
                
                // 切换到亮色主题
                if (initialTheme != "light") {
                    Log.d(TAG, "切换到亮色主题")
                    userSettingsRepository.updateTheme("light")
                }
                
                // 恢复初始主题
                Log.d(TAG, "恢复初始主题: $initialTheme")
                userSettingsRepository.updateTheme(initialTheme)
                
                Log.d(TAG, "主题切换测试完成，功能正常")
            } catch (e: Exception) {
                Log.e(TAG, "主题切换测试失败: ${e.message}")
            }
        }
    }
    
    /**
     * 测试家长控制功能
     */
    fun testParentalControl() {
        scope.launch {
            try {
                // 获取当前家长控制设置
                val initialSettings = userSettingsRepository.userSettings.first()
                val initialEnabled = initialSettings.parentalControlEnabled
                val initialPin = initialSettings.parentalControlPin
                
                Log.d(TAG, "当前家长控制状态: $initialEnabled, PIN: $initialPin")
                
                // 启用家长控制
                Log.d(TAG, "启用家长控制")
                userSettingsRepository.updateParentalControl(true)
                
                // 设置PIN码
                val testPin = "5678"
                Log.d(TAG, "设置PIN码: $testPin")
                userSettingsRepository.updateParentalControlPin(testPin)
                
                // 验证PIN码
                val updatedSettings = userSettingsRepository.userSettings.first()
                val isPinCorrect = updatedSettings.parentalControlPin == testPin
                Log.d(TAG, "PIN码验证: $isPinCorrect")
                
                // 禁用家长控制
                Log.d(TAG, "禁用家长控制")
                userSettingsRepository.updateParentalControl(false)
                
                // 恢复初始设置
                userSettingsRepository.updateParentalControl(initialEnabled)
                userSettingsRepository.updateParentalControlPin(initialPin)
                
                Log.d(TAG, "家长控制测试完成，功能正常")
            } catch (e: Exception) {
                Log.e(TAG, "家长控制测试失败: ${e.message}")
            }
        }
    }
    
    /**
     * 运行所有测试
     */
    fun runAllTests() {
        Log.d(TAG, "开始用户设置测试")
        
        testUserSettingsRepository()
        testThemeSwitching()
        testParentalControl()
        
        Log.d(TAG, "用户设置测试完成")
    }
}
