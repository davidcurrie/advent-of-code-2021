package day11

import java.io.File

val deltas = (-1..1).map { i -> (-1..1).map { j -> Pair(i, j) } }.flatten().filter { (i, j) -> i != 0 || j != 0 }

fun main() {
    val grid = File("src/day11/input.txt")
        .readLines()
        .mapIndexed { i, line -> line.toList().mapIndexed { j, ch -> Pair(i, j) to Character.getNumericValue(ch) } }
        .flatten()
        .toMap()
        .toMutableMap()

    var totalFlashes = 0
    var step = 0
    do {
        step++
        grid.forEach { (coord, value) -> grid[coord] = value + 1 }
        val flashed = mutableSetOf<Pair<Int, Int>>()
        do {
            val flashes = grid.filter { (_, value) -> value > 9 }.map { (coord, _) -> coord }.toSet() - flashed
            flashes.forEach { coord ->
                deltas.map { (dx, dy) -> Pair(coord.first + dx, coord.second + dy) }
                    .filter { it in grid }
                    .forEach { grid[it] = grid[it]!! + 1 }
            }
            flashed.addAll(flashes)
        } while (flashes.isNotEmpty())
        grid.forEach { (coord, value) -> if (value > 9) grid[coord] = 0 }
        totalFlashes += flashed.size
        if (step == 100) println(totalFlashes)
    } while (flashed.size != grid.size)
    println(step)
}