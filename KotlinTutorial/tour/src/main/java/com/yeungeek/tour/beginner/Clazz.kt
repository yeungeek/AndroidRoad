package com.yeungeek.tour.beginner

/**
 *  Created by yeungeek on 2025/12/21.
 */
fun main() {
    val contact = Contact(1, "yeungeek")
    println(contact.name)
    contact.name = "Hello yeungeek"
    println(contact.name)
    contact.showId()
    // data class
    val user = User(1, "yeungeek")
    println(user)
    println(user.toString())

    val user2 = User(1, "yeungeek")
    val user3 = User(2, "yeungeek")
    println("user == user2: ${user == user2}")
    println("user == user3: ${user == user3}")

    println(user.copy())
    println(user.copy(id = 3))
    println(user.copy(name = "Hello yeungeek"))
}

class Contact(val id: Int, var name: String) {
    fun showId() {
        println(id)
    }
}

data class User(val id: Int, val name: String)