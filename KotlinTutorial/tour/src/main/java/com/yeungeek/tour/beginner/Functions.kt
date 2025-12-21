package com.yeungeek.tour.beginner

/**
 *  Created by yeungeek on 2025/12/21.
 */
fun main() {
    println("Sum: ${sum(3, 4)}")
    println("Sum: ${sum2(3, 4)}")

    //lambda
    val upperCaseString = { text: String -> text.uppercase() }
    println("upper string: ${upperCaseString("hello")}")

    val numbers = listOf(1, -2, 3, -4, 5, -6)
    val positives = numbers.filter { it > 0 }
    val isNegative = { x: Int -> x < 0 }
    val negatives = numbers.filter(isNegative)

    println("positives: $positives")
    println("negatives: $negatives")

    val mapNumbers = listOf(1, -2, 3, -4, 5, -6)
    val doubled = mapNumbers.map({ it * 2 })
    println("doubled: $doubled")
    val tripled = mapNumbers.map { it * 3 }
    println("tripled: $tripled")

    println({ text: String -> text.uppercase() }("hello")) // inline
    println(listOf(1,2,3).fold(1, { x, i -> x + i }))
    println(listOf(1,2,3).fold(1){ x, i -> x + i })
}

fun sum(x: Int, y: Int): Int {
    return x * y
}

fun sum2(x: Int, y: Int) = x * y