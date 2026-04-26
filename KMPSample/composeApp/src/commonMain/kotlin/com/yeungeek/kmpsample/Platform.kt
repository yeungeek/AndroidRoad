package com.yeungeek.kmpsample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform