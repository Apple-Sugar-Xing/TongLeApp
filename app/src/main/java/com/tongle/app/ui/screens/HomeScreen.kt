package com.tongle.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tongle.app.ui.theme.TongLeTheme

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // 顶部搜索栏
        TopSearchBar()
        
        // 内容区域
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // 分类选择器
            item {
                CategorySelector()
            }
            
            // 推荐故事
            item {
                SectionTitle(title = "推荐故事")
                StoryCarousel(stories = sampleStories)
            }
            
            // 睡前故事
            item {
                SectionTitle(title = "睡前故事")
                StoryGrid(stories = sampleStories.shuffled())
            }
            
            // 科普故事
            item {
                SectionTitle(title = "科普故事")
                StoryGrid(stories = sampleStories.shuffled())
            }
        }
    }
}

@Composable
fun TopSearchBar() {
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
                text = "搜索故事",
                style = MaterialTheme.typography.body1,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CategorySelector() {
    val categories = listOf("全部", "睡前故事", "童话故事", "科普故事", "成语故事", "历史故事")
    
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
fun StoryCarousel(stories: List<StoryItem>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(stories) { story ->
            StoryCard(
                story = story,
                modifier = Modifier.width(160.dp)
            )
        }
    }
}

@Composable
fun StoryGrid(stories: List<StoryItem>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        stories.take(4).chunked(2).forEach { rowStories ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowStories.forEach { story ->
                    StoryCard(
                        story = story,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    )
                }
                // 如果一行只有一个故事，添加一个空的占位符
                if (rowStories.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun StoryCard(story: StoryItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(0.75f)
            .clickable { /* 处理点击事件 */ },
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            // 故事封面
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
            ) {
                // 在实际应用中，这里会使用Coil加载图片
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(story.coverColor)
                )
            }
            
            // 故事标题和描述
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = story.title,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = story.description,
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
            }
        }
    }
}

// 示例数据
data class StoryItem(
    val id: Long,
    val title: String,
    val description: String,
    val coverColor: Color // 实际应用中会使用图片URL
)

val sampleStories = listOf(
    StoryItem(1, "小红帽", "经典童话故事", Color(0xFFE57373)),
    StoryItem(2, "三只小猪", "勤劳的小猪们", Color(0xFF81C784)),
    StoryItem(3, "白雪公主", "美丽的公主", Color(0xFF64B5F6)),
    StoryItem(4, "灰姑娘", "善良的女孩", Color(0xFFFFB74D)),
    StoryItem(5, "小马过河", "勇敢尝试", Color(0xFF9575CD)),
    StoryItem(6, "龟兔赛跑", "坚持不懈", Color(0xFF4FC3F7))
)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TongLeTheme {
        HomeScreen()
    }
}
