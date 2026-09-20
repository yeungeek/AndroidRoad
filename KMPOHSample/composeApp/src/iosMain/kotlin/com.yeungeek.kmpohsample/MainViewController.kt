package com.yeungeek.kmpohsample

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    App()
}

/**
 * Keeps the default 375dp iPhone scale as the reference and adapts the whole
 * Compose tree on wider iOS windows. Both dp-based layout and sp-based text
 * therefore scale together without touching common UI code.
 */
//@Composable
//@Suppress("unused")
//private fun IosAdaptiveContent(content: @Composable () -> Unit) {
//    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
//        val baseDensity = LocalDensity.current
//        val widthScale = (maxWidth / 375.dp).coerceIn(1f, 1.2f)
//        val adaptiveDensity = Density(
//            density = baseDensity.density * widthScale,
//            fontScale = baseDensity.fontScale * widthScale,
//        )
//        CompositionLocalProvider(
//            LocalDensity provides adaptiveDensity,
//            content = content,
//        )
//    }
//}
