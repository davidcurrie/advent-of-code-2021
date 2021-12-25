package day25

import java.io.File

fun main() {
    val lines = File("src/day25/input.txt").readLines()
    val seafloor = Seafloor(lines[0].length, lines.size,
        Herd.values().associateWith { herd -> herd.parse(lines) }.toMutableMap())

    while (seafloor.move()) {}

    println(seafloor.steps)
}

data class Seafloor(val width: Int, val height: Int, val cucumbers: MutableMap<Herd, Set<Coord>>, var steps: Int = 0)

data class Coord(val x: Int, val y: Int)

enum class Herd(val ch: Char, val delta: Coord) {
    EAST('>', Coord(1, 0)),
    SOUTH('v', Coord(0, 1))
}

fun Herd.parse(lines: List<String>) =
    lines.mapIndexed { j, row -> row.toList().mapIndexedNotNull { i, c -> if (c == ch) Coord(i, j) else null } }
        .flatten().toSet()

fun Seafloor.empty(coord: Coord) = cucumbers.values.none { coords -> coords.contains(coord) }

fun Seafloor.print() {
    for (y in 0 until height) {
        for (x in 0 until width) {
            print(Herd.values()
                .mapNotNull { if (cucumbers[it]!!.contains(Coord(x, y))) it.ch else null }
                .firstOrNull() ?: '.')
        }
        println()
    }
    println()
}

fun Seafloor.move(): Boolean {
    var moved = false
    Herd.values().forEach { herd ->
        cucumbers[herd] = cucumbers[herd]!!.map { coord ->
            Coord((coord.x + herd.delta.x) % width, (coord.y + herd.delta.y) % height)
                .let { newCoord -> if (empty(newCoord)) newCoord.also { moved = true } else coord }
        }.toSet()
    }
    steps++
    return moved
}