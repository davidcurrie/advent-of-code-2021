package day4

import java.io.File

data class Board(val numbers: Map<Int, Pair<Int, Int>>) {
    private val rows = numbers.values.maxOf { it.first } + 1
    private val cols = numbers.values.maxOf { it.second } + 1
    private val matches = mutableListOf<Int>()
    private val rowCounts = MutableList(rows) { 0 }
    private val colCounts = MutableList(cols) { 0 }

    fun play(number: Int): Boolean {
        val coords = numbers[number]
        if (coords != null) {
            matches.add(number)
            rowCounts[coords.first] += 1
            colCounts[coords.second] += 1
            return rowCounts[coords.first] == rows || colCounts[coords.second] == cols
        }
        return false
    }

    fun score() = (numbers.keys.sum() - matches.sum()) * matches.last()
}

fun main() {
    val parts = File("src/day4/input.txt").readText().split("\n\n")
    val numbers = parts[0].split(",").map { it.toInt() }
    var boards = parts.subList(1, parts.size).map { it.readBoard() }

    val scores = mutableListOf<Int>()
    for (number in numbers) {
        val newBoards = mutableListOf<Board>()
        for (board in boards) {
            if (board.play(number)) {
                scores += board.score()
            } else {
                newBoards += board
            }
        }
        boards = newBoards
    }

    println(scores.first())
    println(scores.last())
}

fun String.readBoard() : Board {
    val numbers = mutableMapOf<Int, Pair<Int, Int>>()
    for ((row, line) in this.lines().withIndex()) {
        numbers.putAll(line.split(" ")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .mapIndexed { col, number -> number to Pair(row, col) })
    }
    return Board(numbers)
}