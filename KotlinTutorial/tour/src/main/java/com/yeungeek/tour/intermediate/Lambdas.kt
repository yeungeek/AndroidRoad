package com.yeungeek.tour.intermediate

/**
 *  Created by yeungeek on 2025/12/25.
 */
fun main() {
    val appendText: StringBuilder.() -> Unit = { append("Hello") }
    val sb = StringBuilder()
    sb.appendText()
    println(sb.toString())

    val mainMenu = menu("Main") {
        item("New")
        item("Open")
        item("Save")
    }

    printMenu(mainMenu)
}

fun printMenu(menu: Menu) {
    println("Menu: ${menu.name}")
    menu.items.forEach { println("  Item: ${it.name}") }
}

class MenuItem(val name: String)
class Menu(val name: String) {
    val items = mutableListOf<MenuItem>()

    fun item(name: String) {
        items.add(MenuItem(name))
    }
}
fun menu(name: String, init: Menu.() -> Unit): Menu {
    val menu = Menu(name)
    menu.init()
    return menu
}