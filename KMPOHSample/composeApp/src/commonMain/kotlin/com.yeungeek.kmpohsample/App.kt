package com.yeungeek.kmpohsample

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kmpohsample.composeapp.generated.resources.*

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showCustomLayout by remember { mutableStateOf(false) }

        if (showCustomLayout) {
            Box(modifier = Modifier.fillMaxSize()) {
                CustomLayoutSample()
                Button(
                    onClick = { showCustomLayout = false },
                    modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
                ) {
                    Text(stringResource(Res.string.back))
                }
            }
        } else {
            var showContent by remember { mutableStateOf(false) }
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

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { showCustomLayout = true }) {
                    Text(stringResource(Res.string.show_custom_layout))
                }

                Spacer(modifier = Modifier.height(16.dp))

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
}
