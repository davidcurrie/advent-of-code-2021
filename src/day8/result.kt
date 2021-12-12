package day8

import java.io.File

data class Line(val input:Set<Set<Char>>, val output: List<Set<Char>>) {
    private val from: Map<Set<Char>, Int>
    init {
        val to = mutableMapOf(
            1 to input.find { it.size == 2 }!!,
            4 to input.find { it.size == 4 }!!,
            7 to input.find { it.size == 3 }!!,
            8 to input.find { it.size == 7 }!!
        )
        to[6] = (input - to.values).find { it.size == 6 && (it - to[1]!!).size == 5 }!!
        to[3] = (input - to.values).find { it.size == 5 && (to[8]!! - it - to[1]!!).size == 2}!!
        to[5] = (input - to.values).find { it.size == 5 && (it - to[4]!!).size == 2}!!
        to[9] = (input - to.values).find { (it - to[4]!!).size == 2 }!!
        to[2] = (input - to.values).find { it.size == 5 }!!
        to[0] = (input - to.values).single()

        from = to.entries.associate { (k, v) -> v to k }
    }
    fun partOne() = output.map { segments -> if (setOf(1, 4, 7, 8).contains(from[segments])) 1 else 0 }.sum()
    fun partTwo() = output.fold(0) { acc, segments -> acc * 10 + from[segments]!! }
}

fun String.toSegments() = split(" ").map { it.toCharArray().toSet() }

fun main() {
    val lines = File("src/day8/input.txt").readLines()
        .map { it.split(" | ") }
        .map { line -> Line(line[0].toSegments().toSet(), line[1].toSegments())}

    println(lines.sumOf { it.partOne() })
    println(lines.sumOf { it.partTwo() })
}