package com.tongle.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tongle.app.data.model.Story
import com.tongle.app.data.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {

    // 故事列表状态
    private val _storiesState = MutableStateFlow<StoriesState>(StoriesState.Loading)
    val storiesState: StateFlow<StoriesState> = _storiesState.asStateFlow()

    // 当前选中的分类
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // 推荐故事
    private val _recommendedStories = MutableStateFlow<List<Story>>(emptyList())
    val recommendedStories: StateFlow<List<Story>> = _recommendedStories.asStateFlow()

    init {
        loadRecommendedStories()
        loadAllStories()
    }

    // 加载所有故事
    fun loadAllStories() {
        viewModelScope.launch {
            _storiesState.value = StoriesState.Loading
            try {
                contentRepository.getAllStories().collectLatest { stories ->
                    _storiesState.value = StoriesState.Success(stories)
                }
            } catch (e: Exception) {
                _storiesState.value = StoriesState.Error("加载故事失败: ${e.message}")
            }
        }
    }

    // 按分类加载故事
    fun loadStoriesByCategory(category: String) {
        viewModelScope.launch {
            _selectedCategory.value = category
            _storiesState.value = StoriesState.Loading
            try {
                contentRepository.getStoriesByCategory(category).collectLatest { stories ->
                    _storiesState.value = StoriesState.Success(stories)
                }
            } catch (e: Exception) {
                _storiesState.value = StoriesState.Error("加载故事失败: ${e.message}")
            }
        }
    }

    // 加载推荐故事
    private fun loadRecommendedStories() {
        viewModelScope.launch {
            try {
                contentRepository.getRecommendedStories().collectLatest { stories ->
                    _recommendedStories.value = stories
                }
            } catch (e: Exception) {
                // 处理错误
            }
        }
    }

    // 收藏/取消收藏故事
    fun toggleFavorite(storyId: Long) {
        viewModelScope.launch {
            contentRepository.toggleFavoriteStory(storyId)
        }
    }
}

// 故事列表状态
sealed class StoriesState {
    object Loading : StoriesState()
    data class Success(val stories: List<Story>) : StoriesState()
    data class Error(val message: String) : StoriesState()
}
