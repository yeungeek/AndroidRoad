package com.yeungeek.kmpsample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private enum class Screen {
    Entry,
    Home,
    Login,
}

@Composable
@Preview
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Entry) }

    MaterialTheme {
        when (currentScreen) {
            Screen.Entry -> EntryScreen(
                onNavigateToHome = { currentScreen = Screen.Home },
                onNavigateToLogin = { currentScreen = Screen.Login },
            )
            Screen.Home -> HomeScreen(
                onBack = { currentScreen = Screen.Entry },
                onNavigateToLogin = { currentScreen = Screen.Login },
            )
            Screen.Login -> LoginScreen(
                onBack = { currentScreen = Screen.Home }
            )
        }
    }
}

@Composable
private fun EntryScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val greeting = remember { Greeting().greet() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.32f))
                .safeContentPadding()
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "KMP Sample",
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "当前平台：${getPlatform().name}",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = greeting,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(28.dp))
            Button(
                onClick = onNavigateToHome,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("打开设备页 HomeScreen")
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("前往登录页")
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
