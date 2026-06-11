package com.yeungeek.kmpsample

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.RowScope
import kmpsample.composeapp.generated.resources.Res
import kmpsample.composeapp.generated.resources.mastergo_glasses_secondary
import kmpsample.composeapp.generated.resources.mastergo_icon_album
import kmpsample.composeapp.generated.resources.mastergo_icon_capture
import kmpsample.composeapp.generated.resources.mastergo_icon_space
import kmpsample.composeapp.generated.resources.mastergo_rokid_glasses
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val DeviceBackground = Color(0xFFF5F5F5) // token: 整体背景
private val DevicePrimaryText = Color(0xFF191919) // token: 一级文字色
private val DeviceSecondaryText = Color(0xFF4C4C4C) // token: 二级文字色
private val DeviceWhite = Color(0xFFFFFFFF) // token: 白色背景色100
private val DeviceGreen = Color(0xFF52BF8F) // token: 一级绿色
private val DeviceGreenSoft = Color(0x3352BF8F) // token: 三级绿色
private val DeviceAssistGreen = Color(0xFF52BF8F) // token: 辅助绿色
private val DeviceTopGlowBlue = Color(0x6653ACFF) // token: 头部渐变
private val DeviceTopGlowCyan = Color(0x6600DBED) // token: 头部渐变
private val DeviceTopGlowMint = Color(0x6600FAA6) // token: 头部渐变
private val DeviceGlassFill = Color(0x80FFFFFF) // token: 玻璃风填充1
private val DeviceGlassStroke = Color(0xCCFFFFFF) // token: 玻璃风描边
private val DeviceAppPurple = listOf(Color(0xCC8165EF), Color(0x668165EF)) // token: 紫色app
private val DeviceAppBlue = listOf(Color(0xCC2184FF), Color(0x662184FF)) // token: 蓝色app
private val DeviceAppYellow = listOf(Color(0xCCFF9900), Color(0xCCFFDA5F)) // token: 黄色app
private val DeviceAppOrange = listOf(Color(0xB3FF4800), Color(0x59FF4800)) // token: 橙色app
private val DeviceAppGreen = listOf(Color(0xE62BD086), Color(0xE695FBCD)) // token: 绿色app

private enum class DevicePageState(
    val layerId: String,
    val subtitle: String,
    val pullHint: String,
) {
    Hero(
        layerId = "992:57664",
        subtitle = "新的设计稿更偏向设备首屏展示，信息聚焦在玻璃面板和快捷入口。",
        pullHint = "继续下拉展开控制台",
    ),
    Console(
        layerId = "992:56131",
        subtitle = "展开态会把控制台内容带出来，保留首屏氛围并强化操作入口。",
        pullHint = "轻触收起控制台",
    ),
}

private data class DeviceMetric(
    val label: String,
    val value: String,
)

private data class ShortcutApp(
    val name: String,
    val image: DrawableResource,
)

private data class ConsoleAction(
    val title: String,
    val badge: String,
    val image: DrawableResource,
)

private val heroMetrics = listOf(
    DeviceMetric("连接", "稳定"),
    DeviceMetric("电量", "86%"),
    DeviceMetric("存储", "64GB"),
)

private val shortcutApps = listOf(
    ShortcutApp("空间", Res.drawable.mastergo_icon_space),
    ShortcutApp("相册", Res.drawable.mastergo_icon_album),
    ShortcutApp("拍摄", Res.drawable.mastergo_icon_capture),
    ShortcutApp("眼镜", Res.drawable.mastergo_glasses_secondary),
)

private val consoleActions = listOf(
    ConsoleAction("拍照", "语音", Res.drawable.mastergo_icon_capture),
    ConsoleAction("录影", "HD", Res.drawable.mastergo_glasses_secondary),
    ConsoleAction("AR", "实时", Res.drawable.mastergo_icon_space),
    ConsoleAction("相册", "最近", Res.drawable.mastergo_icon_album),
)

@Composable
fun HomeScreen(
    onBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    var pageState by remember { mutableStateOf(DevicePageState.Hero) }
    val arrowRotation by animateFloatAsState(
        targetValue = if (pageState == DevicePageState.Console) 180f else 0f,
        label = "pull-arrow",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeviceBackground),
    ) {
        TopAtmosphere()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { DeviceStatusBar() }
            item { HeroBanner() }
            item {
                DeviceGlassCard(
                    pageState = pageState,
                    onToggleState = {
                        pageState = if (pageState == DevicePageState.Hero) {
                            DevicePageState.Console
                        } else {
                            DevicePageState.Hero
                        }
                    },
                )
            }
            item {
                PullHint(
                    text = pageState.pullHint,
                    arrowRotation = arrowRotation,
                    onClick = {
                        pageState = if (pageState == DevicePageState.Hero) {
                            DevicePageState.Console
                        } else {
                            DevicePageState.Hero
                        }
                    },
                )
            }
            item {
                FooterActions(
                    onBack = onBack,
                    onNavigateToLogin = onNavigateToLogin,
                )
            }
            item { BottomNavigationMock() }
        }
    }
}

@Composable
private fun TopAtmosphere() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(318.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        DeviceTopGlowBlue,
                        DeviceTopGlowCyan,
                        DeviceTopGlowMint,
                        DeviceBackground,
                    ),
                ),
            ),
    )
    Box(
        modifier = Modifier
            .offset(x = 28.dp, y = (-112).dp)
            .size(width = 342.dp, height = 228.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.58f), Color.Transparent),
                ),
            ),
    )
}

@Composable
private fun DeviceStatusBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "9:41",
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
        )
        Box(
            modifier = Modifier
                .width(125.dp)
                .height(37.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(Color.Black),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .width(if (index == 2) 24.dp else 12.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .background(Color.Black.copy(alpha = 0.82f)),
                )
            }
        }
    }
}

@Composable
private fun HeroBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        GlassesHeroIllustration()
        Column(
            modifier = Modifier.width(132.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "Hi～ 可以试试说：\n“乐奇，拍照”",
                color = DevicePrimaryText,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}

@Composable
private fun GlassesHeroIllustration() {
    Box(
        modifier = Modifier
            .width(214.dp)
            .height(128.dp),
    ) {
        Image(
            painter = painterResource(Res.drawable.mastergo_rokid_glasses),
            contentDescription = "Rokid glasses",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(210.dp)
                .height(126.dp),
            contentScale = ContentScale.Fit,
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 16.dp, y = 12.dp)
                .width(52.dp)
                .height(7.dp)
                .clip(RoundedCornerShape(99.dp))
                .background(Color(0xFF8DEFD1).copy(alpha = 0.72f)),
        )
    }
}

@Composable
private fun DeviceGlassCard(
    pageState: DevicePageState,
    onToggleState: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            DeviceGlassFill,
                            DeviceWhite.copy(alpha = 0.92f),
                        ),
                    ),
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(DeviceGlassStroke.copy(alpha = 0.9f), DeviceGlassStroke.copy(alpha = 0.45f)),
                    ),
                    shape = RoundedCornerShape(32.dp),
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    TagChip(text = "MasterGo DSL ${pageState.layerId}")
                    Text(
                        text = "Rokid Glasses 设备页",
                        style = MaterialTheme.typography.headlineSmall,
                        color = DevicePrimaryText,
                    )
                    Text(
                        text = pageState.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeviceSecondaryText,
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(DeviceGreenSoft),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "已连接",
                        color = DeviceGreen,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                heroMetrics.forEach { metric ->
                    MetricPill(metric = metric)
                }
            }

            if (pageState == DevicePageState.Hero) {
                ShortcutAppsRow()
                FeatureMessageCard(
                    title = "今日建议",
                    content = "保持镜腿贴合，语音和拍摄会更稳定。轻触下拉提示可进入控制台态。",
                )
            }

            AnimatedVisibility(visible = pageState == DevicePageState.Console) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    ConsoleHeader()
                    ConsoleActionGrid()
                    FeatureMessageCard(
                        title = "控制台说明",
                        content = "这一态对应 `992:56131`，重点是把底部“下拉展开控制台”交互明确地落到可操作面板里。",
                    )
                }
            }

            Button(
                onClick = onToggleState,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (pageState == DevicePageState.Hero) "展开控制台" else "返回首屏态")
            }
        }
    }
}

@Composable
private fun RowScope.MetricPill(metric: DeviceMetric) {
    Surface(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(22.dp),
        color = DeviceWhite.copy(alpha = 0.64f),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = metric.label,
                style = MaterialTheme.typography.labelMedium,
                color = DeviceSecondaryText,
            )
            Text(
                text = metric.value,
                style = MaterialTheme.typography.titleMedium,
                color = DevicePrimaryText,
            )
        }
    }
}

@Composable
private fun ShortcutAppsRow() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "常用空间",
            style = MaterialTheme.typography.titleMedium,
            color = DevicePrimaryText,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            shortcutApps.forEach { app ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(DeviceWhite.copy(alpha = 0.78f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(app.image),
                            contentDescription = app.name,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Text(
                        text = app.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = DeviceSecondaryText,
                    )
                }
            }
        }
    }
}

@Composable
private fun ConsoleHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "控制台",
            style = MaterialTheme.typography.titleMedium,
            color = DevicePrimaryText,
        )
        Text(
            text = "从设计稿的展开态抽象出一组更直接的设备操作。",
            style = MaterialTheme.typography.bodyMedium,
            color = DeviceSecondaryText,
        )
    }
}

@Composable
private fun ConsoleActionGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        consoleActions.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                rowItems.forEach { action ->
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        color = DeviceWhite.copy(alpha = 0.78f),
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(DeviceWhite.copy(alpha = 0.84f)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter = painterResource(action.image),
                                    contentDescription = action.title,
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop,
                                )
                            }
                            Text(
                                text = action.title,
                                style = MaterialTheme.typography.titleSmall,
                                color = DevicePrimaryText,
                            )
                            Text(
                                text = action.badge,
                                style = MaterialTheme.typography.labelMedium,
                                color = DeviceSecondaryText,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureMessageCard(
    title: String,
    content: String,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = DeviceWhite.copy(alpha = 0.7f),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = DevicePrimaryText,
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = DeviceSecondaryText,
            )
        }
    }
}

@Composable
private fun PullHint(
    text: String,
    arrowRotation: Float,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = DevicePrimaryText.copy(alpha = 0.7f),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "⌄",
            modifier = Modifier.rotate(arrowRotation),
            color = DeviceSecondaryText,
            fontSize = 16.sp,
        )
    }
}

@Composable
private fun FooterActions(
    onBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.weight(1f),
        ) {
            Text("返回入口")
        }
        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier.weight(1f),
        ) {
            Text("前往登录")
        }
    }
}

@Composable
private fun BottomNavigationMock() {
    Surface(
        shape = RoundedCornerShape(32.dp),
        color = DeviceWhite,
        shadowElevation = 10.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            listOf("主页", "相册", "社区", "我的").forEachIndexed { index, item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (index == 0) DeviceGreenSoft else DeviceBackground),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = item.take(1),
                            color = DevicePrimaryText,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                    Text(
                        text = item,
                        color = if (index == 0) DevicePrimaryText else DeviceSecondaryText,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun TagChip(text: String) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = DeviceGreenSoft,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = DeviceAssistGreen,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(
            onBack = {},
            onNavigateToLogin = {},
        )
    }
}
