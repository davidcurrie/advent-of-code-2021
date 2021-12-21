package day21

import java.io.File

fun Int.mod(m: Int) = ((this - 1) % m) + 1

class Die(val sides: Int) {
    var value = 1
    var rolled = 0
    fun roll() : Int {
        return value.also {
            value = (value + 1).mod(sides)
            rolled++
        }
    }
}

fun partOne(initialPositions: List<Int>) {
    val positions = initialPositions.toTypedArray()
    val scores = arrayOf(0, 0)
    val die = Die(100)
    var player = 0

    while (scores.maxOrNull()!! < 1000) {
        val score = (0..2).map { die.roll() }.sum()
        positions[player] = (positions[player] + score).mod(10)
        scores[player] += positions[player]
        player = (player + 1) % 2
    }

    println(scores.minOrNull()!! * die.rolled)
}

data class PlayerState(val number: Int, val position: Int, val score: Int)

fun partTwo(initialPositions: List<Int>) {
    val states = mutableMapOf(
        listOf(PlayerState(0, initialPositions[0], 0), PlayerState(1, initialPositions[1], 0)) to 1L
    )
    val wins = arrayOf(0L, 0L)

    while (states.isNotEmpty()) {
        val iterator = states.iterator()
        val (players, count) = iterator.next()
        iterator.remove()
        // Next player always comes first in list
        val (player, position, score) = players[0]

        (1..3).map { die1 ->
            (1..3).map { die2 ->
                (1..3).map { die3 ->
                    val newPosition = (position + die1 + die2 + die3).mod(10)
                    val newScore = score + newPosition
                    if (newScore >= 21) {
                        wins[player] += count
                    } else {
                        val newPlayerState = PlayerState(player, newPosition, newScore)
                        val newGameState = listOf(players[1], newPlayerState)
                        states[newGameState] = count + (states[newGameState] ?: 0)
                    }
                }
            }
        }
    }

    println(wins.maxOrNull())
}

fun main() {
    val initialPositions = File("src/day21/input.txt").readLines().map { it.last().digitToInt() }
    partOne(initialPositions)
    partTwo(initialPositions)
}
