package com.yeungeek.kmpohsample

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
class OhosPlatform : Platform {
    override val name: String = "HarmonyOS"
}

@OptIn(ExperimentalNativeApi::class)
actual fun getPlatform(): Platform = OhosPlatform()

