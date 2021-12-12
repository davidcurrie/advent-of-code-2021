package day10

import java.io.File

val closings = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
val partOneScores = mapOf(null to 0, ')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
val partTwoScores = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

fun main() {
    val lines = File("src/day10/input.txt").readLines().map { it.toList() }
    val results = lines.map { line ->
        line.let {
            line.fold(ArrayDeque<Char>()) { stack, ch ->
                if (ch in closings.keys) stack.addLast(closings[ch]!!)
                else if (ch != stack.removeLast()) return@let Pair(ch, null)
                stack
            }.let { stack -> Pair(null, stack) }
        }
    }

    println(results.map { it.first }.sumOf { partOneScores[it]!! })

    println(results.map { it.second }
        .filterNotNull()
        .map { it.reversed().fold(0L) { acc, ch -> (acc * 5) + partTwoScores[ch]!! } }
        .sorted()
        .let { it[it.size / 2] })
}