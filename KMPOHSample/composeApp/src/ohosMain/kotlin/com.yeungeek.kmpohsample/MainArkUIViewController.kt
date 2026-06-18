

package com.yeungeek.kmpohsample

import androidx.compose.ui.window.ComposeArkUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.ArkTS.ArkTS_Napi_NativeModule.napi_value
import platform.ArkTS.ArkTS_Napi_NativeModule.napi_env
import kotlin.experimental.ExperimentalNativeApi
import kotlinx.coroutines.initMainHandler

@OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
@CName("MainArkUIViewController")
fun MainArkUIViewController(env: napi_env): napi_value {
    initMainHandler(env)
    return ComposeArkUIViewController(env) {
        App()
    }
}