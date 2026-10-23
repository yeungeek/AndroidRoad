package com.yeungeek.kmpohsample

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        val greetingText = "Hello, ${platform.name}!"
        println("[Greeting] Generated greeting for platform ${platform.name}: $greetingText")
        return greetingText
    }
}