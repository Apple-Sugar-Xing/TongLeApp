package com.tongle.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tongle.app.data.model.UserSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// 为Context类扩展dataStore属性
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * 用户设置仓库，负责管理用户设置数据
 */
@Singleton
class UserSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // 定义偏好设置的键
    private object PreferencesKeys {
        val TIMER_DURATION = intPreferencesKey("timer_duration")
        val ENABLE_SLEEP_MODE = booleanPreferencesKey("enable_sleep_mode")
        val THEME = stringPreferencesKey("theme")
        val MAX_VOLUME_LEVEL = intPreferencesKey("max_volume_level")
        val PARENTAL_CONTROL_ENABLED = booleanPreferencesKey("parental_control_enabled")
        val PARENTAL_CONTROL_PIN = stringPreferencesKey("parental_control_pin")
    }
    
    // 获取用户设置
    val userSettings: Flow<UserSettings> = context.dataStore.data.map { preferences ->
        UserSettings(
            timerDuration = preferences[PreferencesKeys.TIMER_DURATION] ?: 30,
            enableSleepMode = preferences[PreferencesKeys.ENABLE_SLEEP_MODE] ?: true,
            theme = preferences[PreferencesKeys.THEME] ?: "light",
            maxVolumeLevel = preferences[PreferencesKeys.MAX_VOLUME_LEVEL] ?: 100,
            parentalControlEnabled = preferences[PreferencesKeys.PARENTAL_CONTROL_ENABLED] ?: false,
            parentalControlPin = preferences[PreferencesKeys.PARENTAL_CONTROL_PIN]
        )
    }
    
    // 更新定时器时长
    suspend fun updateTimerDuration(duration: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TIMER_DURATION] = duration
        }
    }
    
    // 更新睡眠模式设置
    suspend fun updateSleepMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ENABLE_SLEEP_MODE] = enabled
        }
    }
    
    // 更新主题设置
    suspend fun updateTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME] = theme
        }
    }
    
    // 更新最大音量级别
    suspend fun updateMaxVolumeLevel(level: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.MAX_VOLUME_LEVEL] = level
        }
    }
    
    // 更新家长控制设置
    suspend fun updateParentalControl(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PARENTAL_CONTROL_ENABLED] = enabled
        }
    }
    
    // 更新家长控制PIN码
    suspend fun updateParentalControlPin(pin: String?) {
        context.dataStore.edit { preferences ->
            if (pin != null) {
                preferences[PreferencesKeys.PARENTAL_CONTROL_PIN] = pin
            } else {
                preferences.remove(PreferencesKeys.PARENTAL_CONTROL_PIN)
            }
        }
    }
}
