package com.tongle.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tongle.app.data.model.Favorite
import com.tongle.app.data.model.Song
import com.tongle.app.data.model.Story
import com.tongle.app.data.repository.ContentRepository
import com.tongle.app.data.repository.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    // 收藏的故事
    private val _favoriteStories = MutableStateFlow<List<Story>>(emptyList())
    val favoriteStories: StateFlow<List<Story>> = _favoriteStories.asStateFlow()

    // 收藏的儿歌
    private val _favoriteSongs = MutableStateFlow<List<Song>>(emptyList())
    val favoriteSongs: StateFlow<List<Song>> = _favoriteSongs.asStateFlow()

    // 用户设置
    private val _userSettings = MutableStateFlow<com.tongle.app.data.model.UserSettings?>(null)
    val userSettings: StateFlow<com.tongle.app.data.model.UserSettings?> = _userSettings.asStateFlow()

    init {
        loadFavoriteStories()
        loadFavoriteSongs()
        loadUserSettings()
    }

    // 加载收藏的故事
    private fun loadFavoriteStories() {
        viewModelScope.launch {
            contentRepository.getFavoriteStories().collectLatest { stories ->
                _favoriteStories.value = stories
            }
        }
    }

    // 加载收藏的儿歌
    private fun loadFavoriteSongs() {
        viewModelScope.launch {
            contentRepository.getFavoriteSongs().collectLatest { songs ->
                _favoriteSongs.value = songs
            }
        }
    }

    // 加载用户设置
    private fun loadUserSettings() {
        viewModelScope.launch {
            userSettingsRepository.userSettings.collectLatest { settings ->
                _userSettings.value = settings
            }
        }
    }

    // 更新主题设置
    fun updateTheme(theme: String) {
        viewModelScope.launch {
            userSettingsRepository.updateTheme(theme)
        }
    }

    // 更新家长控制设置
    fun updateParentalControl(enabled: Boolean) {
        viewModelScope.launch {
            userSettingsRepository.updateParentalControl(enabled)
        }
    }

    // 更新家长控制PIN码
    fun updateParentalControlPin(pin: String?) {
        viewModelScope.launch {
            userSettingsRepository.updateParentalControlPin(pin)
        }
    }
}
