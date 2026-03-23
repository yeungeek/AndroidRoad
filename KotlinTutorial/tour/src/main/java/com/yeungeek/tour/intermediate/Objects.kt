package com.yeungeek.tour.intermediate

/**
 *  Created by yeungeek on 2025/12/29.
 */

fun main() {
    NoAuth.takeParams("hello", "world")

    println(AppConfig)
    println(AppConfig.appName)

    Big.getBongs(10)
}

object NoAuth {
    fun takeParams(username: String, password: String) {
        println("input Auth parameters = $username:$password")
    }
}

data object AppConfig {
    var appName: String = "My Application"
    var version: String = "1.0.0"
}

class Big {
    companion object Bonger {
        fun getBongs(times: Int) {
            repeat(times) { print("BONG ") }
        }
    }
}