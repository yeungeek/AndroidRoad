package com.yeungeek.tour.intermediate

/**
 *  Created by jian.yang on 2026/4/10.
 */

fun printObjectType(obj: Any) {
    when(obj){
        is Int -> println("It's an Integer with value $obj")
        !is Double -> println("It's NOT a Double")
        else -> println("Unknown type")
    }
}

fun main(){
    val intObj = 42
    val doubleObj = 3.1
    val myList =listOf(1,2,3)

    printObjectType(intObj)
    printObjectType(doubleObj)
    printObjectType(myList)

    val a: String? = null
    val b = a as? String
    println(b)
}