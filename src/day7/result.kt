package day7

import java.io.File
import kotlin.math.abs

fun tri(i: Int) = (i * (i + 1)) / 2

fun main() {
    val input = File("src/day7/input.txt").readText().split(",").map { it.toInt() }

    println((input.minOrNull()!!..input.maxOrNull()!!).map { i -> input.sumOf { j -> abs(i - j) } }.minOrNull())
    println((input.minOrNull()!!..input.maxOrNull()!!).map { i -> input.sumOf { j -> tri(abs(i - j)) } }.minOrNull())
}