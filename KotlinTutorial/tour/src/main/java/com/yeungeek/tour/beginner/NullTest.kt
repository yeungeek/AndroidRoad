package com.yeungeek.tour.beginner

/**
 *  Created by yeungeek on 2025/12/21.
 */
fun main() {
    var neverNull: String = "This can't be null"
//    neverNull = null
    var nullable: String? = "You can add null"
    nullable = null

    fun strLen(notNull: String): Int {
        return notNull.length
    }

    strLen(neverNull)
//    strLen(nullable)

    println(lengthString(neverNull))
    println(lengthString(nullable))

    // elvis  ?:
    val nullString: String? = null
    println(nullString?.length ?: 0)
}

fun lengthString(maybeString: String?) = maybeString?.length