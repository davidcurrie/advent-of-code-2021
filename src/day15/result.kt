package day15

import java.io.File
import java.util.*

val deltas = listOf(Coord(-1, 0), Coord(1, 0), Coord(0, -1), Coord(0, 1))

data class Coord(val x: Int, val y: Int)
data class CoordRisk(val coord: Coord, val risk: Int)

fun Map<Coord, Int>.explore(): Int {
    val end = Coord(keys.maxOf { it.x }, keys.maxOf { it.y })
    val visited = mutableSetOf<Coord>()
    val queue = PriorityQueue<CoordRisk> { p1, p2 -> p1.risk - p2.risk }
    queue.add(CoordRisk(Coord(0, 0), 0))

    while (queue.peek().coord != end) {
        val pair = queue.poll()
        deltas.map { delta -> Coord(pair.coord.x + delta.x, pair.coord.y + delta.y) }
            .filter { it in this.keys }
            .filter { it !in visited }
            .forEach {
                visited += it
                queue.add(CoordRisk(it, pair.risk + this[it]!!))
            }
    }

    return queue.peek().risk
}

fun main() {
    val grid = File("src/day15/input.txt").readLines()
        .mapIndexed { y, line -> line.toList().mapIndexed { x, ch -> Coord(x, y) to ch.digitToInt() } }
        .flatten().toMap()

    println(grid.explore())

    val width = grid.keys.maxOf { it.x } + 1
    val height = grid.keys.maxOf { it.y } + 1
    val extendedGrid = mutableMapOf<Coord, Int>()
    for (x in 0..4) {
        for (y in 0..4) {
            extendedGrid.putAll(grid.map { (coord, value) ->
                Coord(coord.x + (x * width), coord.y + (y * height)) to ((value + x + y - 1) % 9) + 1
            }.toMap())
        }
    }

    println(extendedGrid.explore())
}