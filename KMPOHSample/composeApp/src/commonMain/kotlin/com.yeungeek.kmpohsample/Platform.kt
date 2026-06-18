package com.yeungeek.kmpohsample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

