package com.yeungeek.kmpsample

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import com.yeungeek.kmpsample.getPlatform

import kmpsample.composeapp.generated.resources.Res
import kmpsample.composeapp.generated.resources.compose_multiplatform

private enum class Screen {
    Home,
    Login,
}

@Composable
@Preview
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    MaterialTheme {
        when (currentScreen) {
            Screen.Home -> HomeScreen(
                onNavigateToLogin = { currentScreen = Screen.Login }
            )
            Screen.Login -> LoginScreen(
                onBack = { currentScreen = Screen.Home }
            )
        }
    }
}

@Composable
private fun HomeScreen(
    onNavigateToLogin: () -> Unit,
) {
    var showContent by remember { mutableStateOf(false) }
    var showPlatform by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { showPlatform = !showPlatform }) {
            Text("Display platform")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onNavigateToLogin) {
            Text("Go to Login")
        }
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
        AnimatedVisibility(showPlatform) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("显示当前平台: ${getPlatform().name}")
            }
        }
    }
}

@Composable
private fun LoginScreen(
    onBack: () -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "登录",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("用户名") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密码") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("登录")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("返回首页")
        }
    }
}
