package com.yeungeek.tour.beginner

/**
 *  Created by yeungeek on 2025/12/19.
 */

fun main() {
    // list
    val readOnlyShapes = listOf("triangle", "square", "circle")
    println(readOnlyShapes)

    val shapes = mutableListOf("triangle", "square", "circle")
    println(shapes)

    println("First Item is: ${readOnlyShapes[0]}")

    //first,last
    println("First is ${readOnlyShapes.first()}")
    println("Last is ${readOnlyShapes.last()}")
    println("Count is ${readOnlyShapes.count()}")

    println("circle: ${"circle" in readOnlyShapes}")

    shapes.add("pentagon")
    println("Shapes: $shapes")
    shapes.remove("square")
    println("Shapes removed: $shapes")

    // set
    val readOnlyFruit = setOf("apple", "banana", "cherry", "cherry")
    println(readOnlyFruit)
    println("apple is in set: ${"apple" in readOnlyFruit}")

    val fruit = mutableSetOf("apple", "banana", "cherry", "cherry")
    println("set of fruit count ${readOnlyShapes.count()}")
    fruit.add("orange")
    println("add set: $fruit")
    fruit.remove("cherry")
    println("remove set: $fruit")

    // map
    val readOnlyJuiceMenu = mapOf("apple" to 100, "kiwi" to 190, "orange" to 100)
    println(readOnlyJuiceMenu)
    println("apple value: ${readOnlyJuiceMenu["apple"]}")
    println("map count: ${readOnlyJuiceMenu.count()}")
    println("orange is in: ${readOnlyJuiceMenu.containsKey("orange")}")
    println("keys: ${readOnlyJuiceMenu.keys}")
    println("values: ${readOnlyJuiceMenu.values}")

    val juiceMenu = mutableMapOf("apple" to 100, "kiwi" to 190, "orange" to 100)
    println(juiceMenu)

    juiceMenu["mango"] = 150
    println(juiceMenu)
    juiceMenu.remove("apple")
    println(juiceMenu)


    val SUPPORTED = setOf("HTTP", "HTTPS", "FTP")
    val requested = "smtp"
    val isSupported = requested in SUPPORTED
    println("Support for $requested: $isSupported")
}