package com.yeungeek.tour.intermediate

/**
 *  Created by yeungeek on 2025/12/25.
 */
fun main() {
    // let, apply, run, also, with
    //1. let
    val address: String? = getNextAddress()
//    sendNotification(address)
    val confirm = address?.let {
        sendNotification(it)
    }
    println(confirm)
    //2. apply
    println(client.getData())

    //3. run
    val result = client1.run {
        connect()
        authenticate()
        getData()
    }
    println(result)

    //4. also
    val medals: List<String> = listOf("Gold", "Silver", "Bronze")
    val reversedLongUppercaseMedals: List<String> =
        medals
            .map { it.uppercase() }
            .also { println(it) }
            .filter { it.length > 4 }
            .reversed()
    println(reversedLongUppercaseMedals)

    //5. with
    val mainMonitorPrimaryBufferBackedCanvas = Canvas()
    with(mainMonitorPrimaryBufferBackedCanvas){
        text(10, 10, "Foo")
        rect(20, 30, 100, 50)
        circ(40, 60, 25)
        text(15, 45, "Hello")
        rect(70, 80, 150, 100)
        circ(90, 110, 40)
        text(35, 55, "World")
        rect(120, 140, 200, 75)
        circ(160, 180, 55)
        text(50, 70, "Kotlin")
    }
}

val client1 = Client().apply {
    token = "hello world"
}

val client = Client().apply {
    token = "hello"
    connect()
    authenticate()
}

fun sendNotification(recipientAddress: String): String {
    println("Yo $recipientAddress!")
    return "Notification sent!"
}

fun getNextAddress(): String {
    return "sebastian@jetbrains.com"
}

class Client() {
    var token: String? = null
    fun connect() = println("connected!")
    fun authenticate() = println("authenticated!")
    fun getData(): String = "Mock data"
}

class Canvas {
    fun rect(x: Int, y: Int, w: Int, h: Int): Unit = println("$x, $y, $w, $h")
    fun circ(x: Int, y: Int, rad: Int): Unit = println("$x, $y, $rad")
    fun text(x: Int, y: Int, str: String): Unit = println("$x, $y, $str")
}