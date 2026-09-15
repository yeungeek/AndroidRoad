package com.yeungeek.kmpohsample

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

import kmpohsample.composeapp.generated.resources.*

@Composable
internal fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        // LocalDensity is supplied by each Compose platform (Android, iOS and
        // HarmonyOS). Reading it from commonMain keeps the value in sync with
        // the resource environment used by Compose Multiplatform.
        val density = LocalDensity.current
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            Text(
                text = "Density: ${density.density}, fontScale: ${density.fontScale}",
                fontSize = 18.sp
            )
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("${stringResource(Res.string.hello)},Compose: $greeting")
                }
            }
        }
    }
}
