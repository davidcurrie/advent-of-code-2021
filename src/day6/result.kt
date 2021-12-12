package day6

import java.io.File
import java.math.BigInteger

fun main() {
    val fish = File("src/day6/input.txt").readLines()[0].split(",")
        .map { it.toInt() }.groupingBy { it }.eachCount()
    println(solve(fish, 80))
    println(solve(fish, 256))
}

fun solve(input: Map<Int, Int>, days: Int): BigInteger {
    val fish = Array(9) { (input[it] ?: 0).toBigInteger() }
    (1 until days).forEach { fish[(it + 7) % 9] += fish[it % 9] }
    return fish.reduce { a, b -> a + b }
}