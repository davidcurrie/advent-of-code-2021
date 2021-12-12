package day3

import java.io.File
import kotlin.reflect.KFunction1

fun main() {
    val lines = File("src/day3/input.txt")
        .readLines()
        .map { line -> line.toList().map { char -> Character.getNumericValue(char) } }
    val columns = lines.transpose()
    println(columns.map { it.gamma() }.toDecimal() * columns.map { it.epsilon() }.toDecimal())
    println(filter(lines, List<Int>::gamma).toDecimal() * filter(lines, List<Int>::epsilon).toDecimal())
}

fun List<Int>.gamma() = if (sum() >= size / 2.0) 1 else 0
fun List<Int>.epsilon() = 1 - gamma()

fun List<Int>.toDecimal() = reduce { a, b -> a * 2 + b }
fun <T> List<List<T>>.transpose(): List<List<T>> = (first().indices).map { column(it) }
fun <T> List<List<T>>.column(index: Int): List<T> = fold(listOf()) { acc, row -> acc + row[index] }

fun filter(lines: List<List<Int>>, fn: KFunction1<List<Int>, Int>): List<Int> =
    lines.indices.fold(lines) { candidates, index ->
        if (candidates.size == 1) candidates
        else candidates.filter { line -> line[index] == fn(candidates.column(index)) } }.single()