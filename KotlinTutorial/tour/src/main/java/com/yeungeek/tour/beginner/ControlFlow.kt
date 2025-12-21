package com.yeungeek.tour.beginner

/**
 *  Created by yeungeek on 2025/12/20.
 */
fun main() {
    val a = 1
    val b = 2
    println(if (a > b) a else b)


    val obj = "Hello"
    val result = when (obj) {
        "1" -> "One"
        "Hello" -> "Two"
        else -> "Unknown"
    }

    println("result: $result")
    // value
    val num = 1..<4
    val num2 = 1..4

    // for
    val cakes = listOf("carrot", "cheese", "chocolate")
    for (cake in cakes) {
        println("it's cake: $cake")
    }

    //while
    var cakesEaten = 0
    var cakesBaked = 0
    while (cakesEaten < 3) {
        println("Eat a cake")
        cakesEaten++
    }

    do {
        println("Bake a cake")
        cakesBaked++
    } while (cakesBaked < cakesEaten)

    var pizzaSlices = 0
    while (pizzaSlices < 8) {
        pizzaSlices++
        println("There's only $pizzaSlices slice/s of pizza :(")
        if (pizzaSlices == 8) {
            println("There are $pizzaSlices slices of pizza. Hooray! We have a whole pizza! :D")
        }
    }

    pizzaSlices = 0
    do {
        pizzaSlices++
        println("There's only $pizzaSlices slice/s of pizza :(")
        if (pizzaSlices == 8) {
            println("There are $pizzaSlices slices of pizza. Hooray! We have a whole pizza! :D")
        }
    } while (pizzaSlices < 8)


    val words = listOf("dinosaur", "limousine", "magazine", "language")
    for (word in words) {
        if (word.startsWith("l")) {
            println(word)
        }
    }
}