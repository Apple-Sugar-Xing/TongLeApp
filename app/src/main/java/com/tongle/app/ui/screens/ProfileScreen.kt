package com.tongle.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tongle.app.ui.theme.TongLeTheme

@Composable
fun ProfileScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // 用户信息区域
        item {
            UserInfoSection()
        }
        
        // 功能区域
        item {
            FunctionsSection()
        }
        
        // 设置区域
        item {
            SettingsSection()
        }
        
        // 关于区域
        item {
            AboutSection()
        }
    }
}

@Composable
fun UserInfoSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 用户头像
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "小朋友",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h6
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 用户名
            Text(
                text = "快乐的小朋友",
                style = MaterialTheme.typography.h6
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 用户描述
            Text(
                text = "喜欢听故事和儿歌的小朋友",
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun FunctionsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "我的收藏",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FunctionItem(
                    icon = Icons.Default.Favorite,
                    title = "故事收藏",
                    color = MaterialTheme.colors.primary
                )
                
                FunctionItem(
                    icon = Icons.Default.MusicNote,
                    title = "儿歌收藏",
                    color = MaterialTheme.colors.secondary
                )
                
                FunctionItem(
                    icon = Icons.Default.History,
                    title = "播放历史",
                    color = Color(0xFF9C27B0)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FunctionItem(
                    icon = Icons.Default.Download,
                    title = "我的下载",
                    color = Color(0xFF009688)
                )
                
                FunctionItem(
                    icon = Icons.Default.Timer,
                    title = "定时设置",
                    color = Color(0xFFFF5722)
                )
                
                FunctionItem(
                    icon = Icons.Default.Star,
                    title = "我的收藏",
                    color = Color(0xFFFFEB3B)
                )
            }
        }
    }
}

@Composable
fun FunctionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable { /* 处理点击事件 */ }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SettingsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "设置",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            SettingItem(
                icon = Icons.Default.Notifications,
                title = "通知设置",
                subtitle = "管理应用通知"
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            SettingItem(
                icon = Icons.Default.DarkMode,
                title = "主题设置",
                subtitle = "切换日间/夜间模式"
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            SettingItem(
                icon = Icons.Default.Lock,
                title = "家长控制",
                subtitle = "设置使用时间和内容限制"
            )
        }
    }
}

@Composable
fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* 处理点击事件 */ }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colors.primary
        )
        
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1
            )
            
            Text(
                text = subtitle,
                style = MaterialTheme.typography.caption,
                color = Color.Gray
            )
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "进入",
            tint = Color.Gray
        )
    }
}

@Composable
fun AboutSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "关于",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            SettingItem(
                icon = Icons.Default.Info,
                title = "关于童乐听听",
                subtitle = "版本 1.0.0"
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            SettingItem(
                icon = Icons.Default.Email,
                title = "联系我们",
                subtitle = "feedback@tongle.com"
            )
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            
            SettingItem(
                icon = Icons.Default.Star,
                title = "评分",
                subtitle = "在应用商店给我们评分"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    TongLeTheme {
        ProfileScreen()
    }
}
