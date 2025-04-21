package com.tongle.app.data.repository

import com.tongle.app.data.model.Song
import com.tongle.app.data.model.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 内容仓库，负责提供故事和儿歌数据
 * 在实际应用中，这里会从Room数据库或网络API获取数据
 * 目前使用模拟数据进行开发
 */
@Singleton
class ContentRepository @Inject constructor() {
    
    // 模拟的故事数据
    private val stories = listOf(
        Story(
            id = 1,
            title = "小红帽",
            description = "经典童话故事",
            category = "童话故事",
            audioUrl = "https://example.com/stories/little_red_riding_hood.mp3",
            coverImageUrl = "https://example.com/images/little_red_riding_hood.jpg",
            duration = 300,
            ageRange = "3-6岁"
        ),
        Story(
            id = 2,
            title = "三只小猪",
            description = "勤劳的小猪们",
            category = "童话故事",
            audioUrl = "https://example.com/stories/three_little_pigs.mp3",
            coverImageUrl = "https://example.com/images/three_little_pigs.jpg",
            duration = 240,
            ageRange = "3-6岁"
        ),
        Story(
            id = 3,
            title = "白雪公主",
            description = "美丽的公主",
            category = "童话故事",
            audioUrl = "https://example.com/stories/snow_white.mp3",
            coverImageUrl = "https://example.com/images/snow_white.jpg",
            duration = 360,
            ageRange = "3-6岁"
        ),
        Story(
            id = 4,
            title = "龟兔赛跑",
            description = "坚持不懈的故事",
            category = "寓言故事",
            audioUrl = "https://example.com/stories/tortoise_and_hare.mp3",
            coverImageUrl = "https://example.com/images/tortoise_and_hare.jpg",
            duration = 180,
            ageRange = "3-6岁"
        ),
        Story(
            id = 5,
            title = "小马过河",
            description = "勇敢尝试的故事",
            category = "寓言故事",
            audioUrl = "https://example.com/stories/horse_crossing_river.mp3",
            coverImageUrl = "https://example.com/images/horse_crossing_river.jpg",
            duration = 210,
            ageRange = "3-6岁"
        ),
        Story(
            id = 6,
            title = "狼来了",
            description = "诚实的重要性",
            category = "寓言故事",
            audioUrl = "https://example.com/stories/boy_who_cried_wolf.mp3",
            coverImageUrl = "https://example.com/images/boy_who_cried_wolf.jpg",
            duration = 240,
            ageRange = "3-6岁"
        ),
        Story(
            id = 7,
            title = "月亮的秘密",
            description = "关于月球的科普故事",
            category = "科普故事",
            audioUrl = "https://example.com/stories/moon_secret.mp3",
            coverImageUrl = "https://example.com/images/moon_secret.jpg",
            duration = 270,
            ageRange = "6-9岁"
        ),
        Story(
            id = 8,
            title = "恐龙世界",
            description = "恐龙的生活方式",
            category = "科普故事",
            audioUrl = "https://example.com/stories/dinosaur_world.mp3",
            coverImageUrl = "https://example.com/images/dinosaur_world.jpg",
            duration = 300,
            ageRange = "6-9岁"
        ),
        Story(
            id = 9,
            title = "睡前故事：小熊的梦",
            description = "帮助入睡的温馨故事",
            category = "睡前故事",
            audioUrl = "https://example.com/stories/bear_dream.mp3",
            coverImageUrl = "https://example.com/images/bear_dream.jpg",
            duration = 240,
            ageRange = "0-3岁"
        ),
        Story(
            id = 10,
            title = "睡前故事：星星的旅行",
            description = "帮助入睡的温馨故事",
            category = "睡前故事",
            audioUrl = "https://example.com/stories/star_journey.mp3",
            coverImageUrl = "https://example.com/images/star_journey.jpg",
            duration = 210,
            ageRange = "0-3岁"
        )
    )
    
    // 模拟的儿歌数据
    private val songs = listOf(
        Song(
            id = 1,
            title = "两只老虎",
            description = "经典儿歌",
            category = "经典儿歌",
            audioUrl = "https://example.com/songs/two_tigers.mp3",
            coverImageUrl = "https://example.com/images/two_tigers.jpg",
            duration = 120,
            hasLyrics = true,
            lyricsUrl = "https://example.com/lyrics/two_tigers.lrc"
        ),
        Song(
            id = 2,
            title = "小星星",
            description = "经典儿歌",
            category = "经典儿歌",
            audioUrl = "https://example.com/songs/twinkle_star.mp3",
            coverImageUrl = "https://example.com/images/twinkle_star.jpg",
            duration = 90,
            hasLyrics = true,
            lyricsUrl = "https://example.com/lyrics/twinkle_star.lrc"
        ),
        Song(
            id = 3,
            title = "小毛驴",
            description = "经典儿歌",
            category = "经典儿歌",
            audioUrl = "https://example.com/songs/little_donkey.mp3",
            coverImageUrl = "https://example.com/images/little_donkey.jpg",
            duration = 150,
            hasLyrics = true,
            lyricsUrl = "https://example.com/lyrics/little_donkey.lrc"
        ),
        Song(
            id = 4,
            title = "数鸭子",
            description = "经典儿歌",
            category = "经典儿歌",
            audioUrl = "https://example.com/songs/counting_ducks.mp3",
            coverImageUrl = "https://example.com/images/counting_ducks.jpg",
            duration = 135,
            hasLyrics = true,
            lyricsUrl = "https://example.com/lyrics/counting_ducks.lrc"
        ),
        Song(
            id = 5,
            title = "Twinkle Twinkle Little Star",
            description = "英文儿歌",
            category = "英文儿歌",
            audioUrl = "https://example.com/songs/twinkle_twinkle_english.mp3",
            coverImageUrl = "https://example.com/images/twinkle_twinkle_english.jpg",
            duration = 105,
            hasLyrics = true,
            lyricsUrl = "https://example.com/lyrics/twinkle_twinkle_english.lrc"
        ),
        Song(
            id = 6,
            title = "Old MacDonald Had a Farm",
            description = "英文儿歌",
            category = "英文儿歌",
            audioUrl = "https://example.com/songs/old_macdonald.mp3",
            coverImageUrl = "https://example.com/images/old_macdonald.jpg",
            duration = 180,
            hasLyrics = true,
            lyricsUrl = "https://example.com/lyrics/old_macdonald.lrc"
        ),
        Song(
            id = 7,
            title = "摇篮曲",
            description = "帮助宝宝入睡",
            category = "摇篮曲",
            audioUrl = "https://example.com/songs/lullaby.mp3",
            coverImageUrl = "https://example.com/images/lullaby.jpg",
            duration = 240,
            hasLyrics = false,
            lyricsUrl = null
        ),
        Song(
            id = 8,
            title = "世上只有妈妈好",
            description = "感恩母亲的歌曲",
            category = "节日歌曲",
            audioUrl = "https://example.com/songs/mother_is_the_best.mp3",
            coverImageUrl = "https://example.com/images/mother_is_the_best.jpg",
            duration = 210,
            hasLyrics = true,
            lyricsUrl = "https://example.com/lyrics/mother_is_the_best.lrc"
        ),
        Song(
            id = 9,
            title = "新年好",
            description = "庆祝新年的歌曲",
            category = "节日歌曲",
            audioUrl = "https://example.com/songs/happy_new_year.mp3",
            coverImageUrl = "https://example.com/images/happy_new_year.jpg",
            duration = 120,
            hasLyrics = true,
            lyricsUrl = "https://example.com/lyrics/happy_new_year.lrc"
        ),
        Song(
            id = 10,
            title = "找朋友",
            description = "互动儿歌",
            category = "儿童歌谣",
            audioUrl = "https://example.com/songs/find_friends.mp3",
            coverImageUrl = "https://example.com/images/find_friends.jpg",
            duration = 150,
            hasLyrics = true,
            lyricsUrl = "https://example.com/lyrics/find_friends.lrc"
        )
    )
    
    // 收藏的内容ID
    private val _favoriteStoryIds = MutableStateFlow<Set<Long>>(setOf())
    private val _favoriteSongIds = MutableStateFlow<Set<Long>>(setOf())
    
    // 获取所有故事
    fun getAllStories(): Flow<List<Story>> {
        return MutableStateFlow(stories)
    }
    
    // 按分类获取故事
    fun getStoriesByCategory(category: String): Flow<List<Story>> {
        return MutableStateFlow(stories.filter { it.category == category })
    }
    
    // 获取推荐故事
    fun getRecommendedStories(): Flow<List<Story>> {
        return MutableStateFlow(stories.shuffled().take(5))
    }
    
    // 获取故事详情
    fun getStoryById(id: Long): Flow<Story?> {
        return MutableStateFlow(stories.find { it.id == id })
    }
    
    // 获取所有儿歌
    fun getAllSongs(): Flow<List<Song>> {
        return MutableStateFlow(songs)
    }
    
    // 按分类获取儿歌
    fun getSongsByCategory(category: String): Flow<List<Song>> {
        return MutableStateFlow(songs.filter { it.category == category })
    }
    
    // 获取推荐儿歌
    fun getRecommendedSongs(): Flow<List<Song>> {
        return MutableStateFlow(songs.shuffled().take(5))
    }
    
    // 获取儿歌详情
    fun getSongById(id: Long): Flow<Song?> {
        return MutableStateFlow(songs.find { it.id == id })
    }
    
    // 收藏故事
    suspend fun toggleFavoriteStory(id: Long) {
        val currentFavorites = _favoriteStoryIds.value.toMutableSet()
        if (currentFavorites.contains(id)) {
            currentFavorites.remove(id)
        } else {
            currentFavorites.add(id)
        }
        _favoriteStoryIds.value = currentFavorites
    }
    
    // 收藏儿歌
    suspend fun toggleFavoriteSong(id: Long) {
        val currentFavorites = _favoriteSongIds.value.toMutableSet()
        if (currentFavorites.contains(id)) {
            currentFavorites.remove(id)
        } else {
            currentFavorites.add(id)
        }
        _favoriteSongIds.value = currentFavorites
    }
    
    // 获取收藏的故事
    fun getFavoriteStories(): Flow<List<Story>> {
        return _favoriteStoryIds.map { favoriteIds ->
            stories.filter { favoriteIds.contains(it.id) }
        }
    }
    
    // 获取收藏的儿歌
    fun getFavoriteSongs(): Flow<List<Song>> {
        return _favoriteSongIds.map { favoriteIds ->
            songs.filter { favoriteIds.contains(it.id) }
        }
    }
    
    // 检查故事是否已收藏
    fun isStoryFavorite(id: Long): Flow<Boolean> {
        return _favoriteStoryIds.map { it.contains(id) }
    }
    
    // 检查儿歌是否已收藏
    fun isSongFavorite(id: Long): Flow<Boolean> {
        return _favoriteSongIds.map { it.contains(id) }
    }
}
