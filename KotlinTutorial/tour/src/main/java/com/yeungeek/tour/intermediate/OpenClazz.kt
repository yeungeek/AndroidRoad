package com.yeungeek.tour.intermediate

/**
 *  Created by jian.yang on 2026/3/23.
 */
open class Vehicle(val make: String, val model: String) {
    open fun displayInfo() {
        println("Make: $make, Model: $model")
    }
}

open class Car1(make: String, model: String, val numberOfDoors: Int) : Vehicle(make, model) {
    override fun displayInfo() {
        println("Car Info: Make - $make, Model - $model, Number of doors - $numberOfDoors")
    }
}

interface EcoFriendly {
    val emissionLevel: String
}

interface ElectricVehicle {
    val batteryCapacity: Double
}

class ElectricCar(
    make: String,
    model: String,
    numberOfDoors: Int,
    val capacity: Double,
    val emission: String
) : Car1(make, model, numberOfDoors),
    EcoFriendly, ElectricVehicle {
    override val emissionLevel: String = emission
    override val batteryCapacity: Double = capacity
}

/// sealed class
sealed class Mammal(val name: String)
class Cat(val catName: String) : Mammal(catName)
class Human(val humanName: String, val job: String) : Mammal(humanName)

/// enum class
enum class State {
    IDLE, RUNNING, FINISHED
}

fun greetMammal(mammal: Mammal): String {
    return when (mammal) {
        is Human -> "Hello ${mammal.name}; You're working as a ${mammal.job}"
        is Cat -> "Hello ${mammal.name}"
    }
}

/// value class
@JvmInline
value class Email(val address: String)

fun sendEmail(email: Email) {
    println("Sending email to ${email.address}")
}

fun main() {
    val car1 = Car1("Toyota", "Corolla", 4)
    val car2 = Car1("Honda", "Civic", 2)

    car1.displayInfo()
    car2.displayInfo()
//    println("Car Info: Make - ${car.make}, Model - ${car.model}, Number of doors - ${car.numberOfDoors}")

    println(greetMammal(Cat("yellow")))

    val state = State.RUNNING
    val message = when (state) {
        State.IDLE -> "It's idle"
        State.RUNNING -> "It's running"
        State.FINISHED -> "It's finished"
    }

    println("message: $message")

    val email = Email("hangzhou")
    sendEmail(email)
}