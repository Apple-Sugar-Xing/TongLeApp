package com.tongle.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tongle.app.ui.theme.TongLeTheme

@Composable
fun PlayScreen(
    isStory: Boolean = true,
    onBackClick: () -> Unit = {}
) {
    val title = if (isStory) "小红帽" else "两只老虎"
    val author = if (isStory) "格林童话" else "儿童歌曲"
    val coverColor = if (isStory) Color(0xFFE57373) else Color(0xFF81C784)
    
    var isPlaying by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0.3f) }
    var showTimerDialog by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // 内容区域
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 顶部栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "返回"
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                IconButton(onClick = { /* 收藏 */ }) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "收藏"
                    )
                }
                
                IconButton(onClick = { showTimerDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "定时"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 封面
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(coverColor),
                contentAlignment = Alignment.Center
            ) {
                if (isStory) {
                    Text(
                        text = "故事",
                        style = MaterialTheme.typography.h3,
                        color = Color.White
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(120.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 标题和作者
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = author,
                style = MaterialTheme.typography.subtitle1,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 进度条
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Slider(
                    value = progress,
                    onValueChange = { progress = it },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colors.primary,
                        activeTrackColor = MaterialTheme.colors.primary
                    )
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime((progress * 180).toInt()),
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray
                    )
                    
                    Text(
                        text = formatTime(180),
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 控制按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* 上一个 */ }) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "上一个",
                        modifier = Modifier.size(40.dp)
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary)
                        .clickable { isPlaying = !isPlaying },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "暂停" else "播放",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                IconButton(onClick = { /* 下一个 */ }) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "下一个",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 播放模式和速度
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { /* 循环模式 */ }) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "循环",
                        tint = MaterialTheme.colors.primary
                    )
                }
                
                IconButton(onClick = { /* 播放列表 */ }) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "播放列表"
                    )
                }
                
                IconButton(onClick = { /* 播放速度 */ }) {
                    Text(
                        text = "1.0x",
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
        
        // 定时器对话框
        if (showTimerDialog) {
            TimerDialog(
                onDismiss = { showTimerDialog = false },
                onConfirm = { minutes ->
                    // 设置定时器
                    showTimerDialog = false
                }
            )
        }
    }
}

@Composable
fun TimerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val timerOptions = listOf(15, 30, 45, 60, 90)
    var selectedOption by remember { mutableStateOf(30) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "设置定时关闭",
                style = MaterialTheme.typography.h6
            )
        },
        text = {
            Column {
                Text(
                    text = "选择多长时间后自动停止播放",
                    style = MaterialTheme.typography.body1
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    timerOptions.forEach { minutes ->
                        TimerOption(
                            minutes = minutes,
                            selected = minutes == selectedOption,
                            onClick = { selectedOption = minutes }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = true,
                        onCheckedChange = { /* 启用睡眠模式 */ }
                    )
                    
                    Text(
                        text = "启用睡眠模式（逐渐降低音量）",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedOption) }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("取消")
            }
        }
    )
}

@Composable
fun TimerOption(
    minutes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(
                if (selected) MaterialTheme.colors.primary
                else MaterialTheme.colors.surface
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${minutes}分钟",
            style = MaterialTheme.typography.caption,
            color = if (selected) MaterialTheme.colors.onPrimary
                   else MaterialTheme.colors.onSurface
        )
    }
}

// 格式化时间（秒 -> MM:SS）
fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}

@Preview(showBackground = true)
@Composable
fun PlayScreenPreview() {
    TongLeTheme {
        PlayScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun TimerDialogPreview() {
    TongLeTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TimerDialog(
                onDismiss = {},
                onConfirm = {}
            )
        }
    }
}
