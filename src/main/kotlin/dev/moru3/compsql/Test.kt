package dev.moru3.compsql

import kotlin.concurrent.thread

fun main() {
    println("Start: ${Thread.currentThread().id}")
    thread { Thread.sleep(1000);println("test") }
    println("End: ${Thread.currentThread().id}")
}