package day1

import java.io.File

fun main() {
    val numbers = File("src/day1/input.txt")
        .readLines()
        .map(String::toInt)

    println(numbers.windowed(2).count { (a, b) -> a < b })
    println(numbers.windowed(4).count { it.first() < it.last() })
}