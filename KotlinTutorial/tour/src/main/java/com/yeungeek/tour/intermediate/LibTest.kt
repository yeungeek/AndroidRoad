package com.yeungeek.tour.intermediate

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
/**
 *  Created by jian.yang on 2026/4/10.
 */

fun main() {
    val text = "emosewa si niltoK"

    val reversedText = text.reversed()
    println(reversedText)

    val thirtyMinutes: Duration = 30.minutes
    val halfHours:Duration = 0.5.hours
    println(thirtyMinutes == halfHours)

    val now = Clock.System.now() // 得到当前时刻
    println("Current instant: $now")

    val zone = TimeZone.of("America/New_York")
    val localDateTime = now.toLocalDateTime(zone)
    println("Local date-time in NY: $localDateTime")
}