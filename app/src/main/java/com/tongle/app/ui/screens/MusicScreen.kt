package com.tongle.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tongle.app.ui.theme.TongLeTheme

@Composable
fun MusicScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // 顶部搜索栏
        TopSearchBar(placeholder = "搜索儿歌")
        
        // 内容区域
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // 分类选择器
            item {
                MusicCategorySelector()
            }
            
            // 热门儿歌
            item {
                SectionTitle(title = "热门儿歌")
                SongCarousel(songs = sampleSongs)
            }
            
            // 经典儿歌
            item {
                SectionTitle(title = "经典儿歌")
                SongList(songs = sampleSongs.shuffled())
            }
            
            // 英文儿歌
            item {
                SectionTitle(title = "英文儿歌")
                SongList(songs = sampleSongs.shuffled())
            }
        }
    }
}

@Composable
fun TopSearchBar(placeholder: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "搜索",
                tint = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = placeholder,
                style = MaterialTheme.typography.body1,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun MusicCategorySelector() {
    val categories = listOf("全部", "经典儿歌", "英文儿歌", "摇篮曲", "儿童歌谣", "节日歌曲")
    
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories) { category ->
            CategoryChip(
                category = category,
                selected = category == "全部",
                onSelected = { /* 处理分类选择 */ }
            )
        }
    }
}

@Composable
fun CategoryChip(
    category: String,
    selected: Boolean,
    onSelected: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(end = 8.dp)
            .clickable(onClick = onSelected),
        elevation = 2.dp,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
    ) {
        Text(
            text = category,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SongCarousel(songs: List<SongItem>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(songs) { song ->
            SongCard(
                song = song,
                modifier = Modifier.width(160.dp)
            )
        }
    }
}

@Composable
fun SongList(songs: List<SongItem>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        songs.take(5).forEach { song ->
            SongListItem(song = song)
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun SongCard(song: SongItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { /* 处理点击事件 */ },
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 歌曲封面
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
                    .background(song.coverColor),
                contentAlignment = Alignment.Center
            ) {
                // 播放按钮
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "播放",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            // 歌曲标题和描述
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun SongListItem(song: SongItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* 处理点击事件 */ }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 歌曲封面
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(song.coverColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "播放",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        
        // 歌曲信息
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = song.title,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = song.artist,
                style = MaterialTheme.typography.caption,
                color = Color.Gray
            )
        }
        
        // 歌曲时长
        Text(
            text = song.duration,
            style = MaterialTheme.typography.caption,
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

// 示例数据
data class SongItem(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: String,
    val coverColor: Color // 实际应用中会使用图片URL
)

val sampleSongs = listOf(
    SongItem(1, "两只老虎", "儿童歌曲", "2:30", Color(0xFFE57373)),
    SongItem(2, "小星星", "儿童歌曲", "1:45", Color(0xFF81C784)),
    SongItem(3, "小毛驴", "儿童歌曲", "2:15", Color(0xFF64B5F6)),
    SongItem(4, "数鸭子", "儿童歌曲", "2:05", Color(0xFFFFB74D)),
    SongItem(5, "世上只有妈妈好", "儿童歌曲", "3:10", Color(0xFF9575CD)),
    SongItem(6, "找朋友", "儿童歌曲", "1:55", Color(0xFF4FC3F7))
)

@Preview(showBackground = true)
@Composable
fun MusicScreenPreview() {
    TongLeTheme {
        MusicScreen()
    }
}
