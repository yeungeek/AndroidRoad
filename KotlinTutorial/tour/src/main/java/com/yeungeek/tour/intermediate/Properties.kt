package com.yeungeek.tour.intermediate

import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

/**
 *  Created by yeungeek on 2026/3/23.
 */
class Person {
    var name: String = ""
        set(value) {
            field = value.replaceFirstChar { firstChar -> firstChar.uppercase() }
        }
}

data class Person1(val firstName: String, val secondName: String)

val Person1.fullName: String
    get() = "$firstName $secondName"


class CachedStringDelegate {
    var cachedValue: String? = null

    operator fun getValue(thisRef: User, property: KProperty<*>): String {
        if (cachedValue == null) {
            cachedValue = "${thisRef.firstName} ${thisRef.lastName}"
            println("Computed and cached: $cachedValue")
        } else {
            println("Accessed from cache: $cachedValue")
        }
        return cachedValue ?: "Unknown"
    }
}

class User(val firstName: String, val lastName: String) {
    val displayName: String by CachedStringDelegate()
}

class Thermostat {
    var temperature: Double by observable(20.0) { _, oldValue, newValue ->
        if (newValue > 25) {
            println("Warning: Temperature is too high! ($oldValue°C -> $newValue°C)")
        } else {
            println("Temperature updated: $oldValue°C -> $newValue°C")
        }
    }
}

fun main() {
    val person = Person()
    person.name = "yeungeek"
    println(person.name)

    val person1 = Person1("first", "second")
    println(person1.fullName)

    val user = User("John", "Doe")
    println(user.displayName)
    //cached
    println(user.displayName)


    val thermostat = Thermostat()
    thermostat.temperature = 22.5
    thermostat.temperature = 30.0
}