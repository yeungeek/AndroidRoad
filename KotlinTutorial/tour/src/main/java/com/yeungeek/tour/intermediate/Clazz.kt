package com.yeungeek.tour.intermediate

/**
 *  Created by yeungeek on 2025/12/27.
 */
fun main() {
    val car = Car("Toyota", "Corolla", 4)
    println("Car1: make=${car.make}, model=${car.model}, numberOfDoors=${car.numberOfDoors}")

    val laptop = Electronic(name = "Laptop", price = 1000.0, warrantyYears = 2)
    println(laptop.productInfo())
    laptop.pay(200.0)

    val redCircle = RedCircle(Circle(100.0))
    redCircle.draw()
    println(redCircle.color)
}

class Car(val make: String, val model: String, val numberOfDoors: Int)

abstract class Product(val name: String, var price: Double) {
    abstract val category: String

    fun productInfo(): String {
        return "Product: $name, Category: $category, Price: $price"
    }
}

class Electronic(name: String, price: Double, val warrantyYears: Int) : Product(name, price),
    PaymentMethod {
    override val category: String = "Electronic"
    override fun pay(amount: Double) {
        println("Paying $amount for $name")
    }
}

interface PaymentMethod {
    fun pay(amount: Double)
}

interface Drawable {
    fun draw()
    fun resize()
    val color: String?
}

class Circle(val radius: Double) : Drawable {
    override fun draw() {
        println("Drawing a circle with radius $radius")
    }

    override fun resize() {
        println("Resizing a circle")
    }

    override val color = "Color"
}

class RedCircle(param: Circle) : Drawable by param {
    override val color = "Red"
}


