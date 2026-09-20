package com.yeungeek.kmpohsample

import co.touchlab.kermit.Logger

class Greeting {
    private val platform = getPlatform()
    private val logger = Logger.withTag("Greeting")

    fun greet(): String {
        val greetingText = "Hello, ${platform.name}!"
        logger.d { "Generated greeting for platform ${platform.name}: $greetingText" }
        return greetingText
    }
}