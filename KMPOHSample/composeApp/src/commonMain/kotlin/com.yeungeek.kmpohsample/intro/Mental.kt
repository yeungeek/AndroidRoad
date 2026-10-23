package com.yeungeek.kmpohsample.intro

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ClickCounter(clicks: Int, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "I've been clicked $clicks times")
    }
}

@Composable
fun MyScreen() {
    val pulseRateMs = remember { mutableLongStateOf(3000L) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(pulseRateMs) {
        while (isActive) {
            delay(pulseRateMs.longValue.milliseconds)
            alpha.animateTo(0f)
            alpha.animateTo(1f)
        }
    }

    Text(text = "Compose", modifier = Modifier.alpha(alpha.value))
}


@Composable
fun WithConstraintsComposable(){
    BoxWithConstraints {
        Text("My minHeight is $minHeight while my maxWidth is $maxWidth")
    }
}