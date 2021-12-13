package day13

import java.io.File
import kotlin.math.abs

fun main() {
    val input = File("src/day13/input.txt").readText()

    val coords = Regex("([0-9]*),([0-9]*)").findAll(input)
        .map { Pair(it.groupValues[1].toInt(), it.groupValues[2].toInt()) }
        .toSet()
    val folds = Regex("fold along ([xy])=([0-9]*)").findAll(input)
        .map { Pair(it.groupValues[1], it.groupValues[2].toInt()) }
        .toList()

    println(coords.map { p -> p.fold(folds[0]) }.toSet().size)
    folds.fold(coords) { c, f -> c.map { p -> p.fold(f) }.toSet() }.output()
}

fun Pair<Int, Int>.fold(fold: Pair<String, Int>) = Pair(
    if (fold.first == "x") fold.second - abs(first - fold.second) else first,
    if (fold.first == "y") fold.second - abs(second - fold.second) else second
)

fun Set<Pair<Int, Int>>.output() {
    (0..maxOf { it.second }).forEach { y ->
        (0..maxOf { it.first }).forEach { x ->
            print(if (contains(Pair(x, y))) "#" else ".")
        }
        println()
    }
    println()
}