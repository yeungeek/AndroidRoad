package com.yeungeek.tour.intermediate

/**
 *  Created by yeungeek on 2025/12/25.
 */
fun main() {
    println("hello".bold())
    println(1.isPositive())
    println("Hello World".toLowercaseString())
}

fun String.bold() = "<b>$this</b>"

fun String.toLowercaseString() = this.lowercase()

fun Int.isPositive() = this > 0