package day2

import day2.Direction.*
import java.io.File
import java.util.*

fun main() {
    val moves = File("src/day2/input.txt")
        .readLines()
        .map { it.split(" ") }
        .map { Move(valueOf(it[0].uppercase(Locale.getDefault())), it[1].toInt()) }

    println(calculate(PartOneState(), moves))
    println(calculate(PartTwoState(), moves))
}

fun calculate(initialState: State, moves: List<Move>): Int {
    val finalState = moves.fold(initialState) { state, move -> state.transform(move) }
    return finalState.horizontal * finalState.depth
}

enum class Direction { UP, DOWN, FORWARD }
data class Move(val direction: Direction, val units: Int)
interface State {
    val horizontal: Int
    val depth: Int
    fun transform(move: Move): State
}

data class PartOneState(override val horizontal: Int = 0, override val depth: Int = 0) : State {
    override fun transform(move: Move): PartOneState {
        return when (move.direction) {
            UP -> copy(depth = depth - move.units)
            DOWN -> copy(depth = depth + move.units)
            FORWARD -> copy(horizontal = horizontal + move.units)
        }
    }
}

data class PartTwoState(override val horizontal: Int = 0, override val depth: Int = 0, val aim: Int = 0) : State {
    override fun transform(move: Move): PartTwoState {
        return when (move.direction) {
            UP -> copy(aim = aim - move.units)
            DOWN -> copy(aim = aim + move.units)
            FORWARD -> copy(horizontal = horizontal + move.units, depth = depth + aim * move.units)
        }
    }
}