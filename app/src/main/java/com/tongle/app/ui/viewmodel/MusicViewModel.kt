package com.tongle.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tongle.app.data.model.Song
import com.tongle.app.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {

    // 儿歌列表状态
    private val _songsState = MutableStateFlow<SongsState>(SongsState.Loading)
    val songsState: StateFlow<SongsState> = _songsState.asStateFlow()

    // 当前选中的分类
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // 推荐儿歌
    private val _recommendedSongs = MutableStateFlow<List<Song>>(emptyList())
    val recommendedSongs: StateFlow<List<Song>> = _recommendedSongs.asStateFlow()

    init {
        loadRecommendedSongs()
        loadAllSongs()
    }

    // 加载所有儿歌
    fun loadAllSongs() {
        viewModelScope.launch {
            _songsState.value = SongsState.Loading
            try {
                contentRepository.getAllSongs().collectLatest { songs ->
                    _songsState.value = SongsState.Success(songs)
                }
            } catch (e: Exception) {
                _songsState.value = SongsState.Error("加载儿歌失败: ${e.message}")
            }
        }
    }

    // 按分类加载儿歌
    fun loadSongsByCategory(category: String) {
        viewModelScope.launch {
            _selectedCategory.value = category
            _songsState.value = SongsState.Loading
            try {
                contentRepository.getSongsByCategory(category).collectLatest { songs ->
                    _songsState.value = SongsState.Success(songs)
                }
            } catch (e: Exception) {
                _songsState.value = SongsState.Error("加载儿歌失败: ${e.message}")
            }
        }
    }

    // 加载推荐儿歌
    private fun loadRecommendedSongs() {
        viewModelScope.launch {
            try {
                contentRepository.getRecommendedSongs().collectLatest { songs ->
                    _recommendedSongs.value = songs
                }
            } catch (e: Exception) {
                // 处理错误
            }
        }
    }

    // 收藏/取消收藏儿歌
    fun toggleFavorite(songId: Long) {
        viewModelScope.launch {
            contentRepository.toggleFavoriteSong(songId)
        }
    }
}

// 儿歌列表状态
sealed class SongsState {
    object Loading : SongsState()
    data class Success(val songs: List<Song>) : SongsState()
    data class Error(val message: String) : SongsState()
}
