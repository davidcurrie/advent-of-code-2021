package day15

import java.io.File
import java.util.*

val deltas = listOf(Coord(-1, 0), Coord(1, 0), Coord(0, -1), Coord(0, 1))

data class Coord(val x: Int, val y: Int)

fun Map<Coord, Int>.explore() : Int {
    val end = Coord(keys.maxOf { it.x }, keys.maxOf {it.y })
    val paths = PriorityQueue<Pair<List<Coord>, Int>> { p1, p2 -> p1.second - p2.second }
    paths.add(Pair(listOf(Coord(0,0)), 0))
    val visited = mutableSetOf<Coord>()

    while (paths.isNotEmpty()) {
        val path = paths.poll()
        if (path.first.last() == end) {
            return path.second
        }
        deltas.map { delta -> Coord(path.first.last().x + delta.x, path.first.last().y + delta.y) }
            .filter { it in this.keys }
            .filter { it !in visited }
            .forEach {
                visited += it
                paths.add(Pair(path.first + it, path.second + this[it]!!))
            }
    }

    return -1
}

fun main() {
    val grid = File("src/day15/input.txt").readLines()
        .mapIndexed { y, line -> line.toList().mapIndexed { x, ch -> Coord(x,y) to ch.digitToInt()  } }
        .flatten().toMap()

    val end = Coord(grid.keys.maxOf { it.x }, grid.keys.maxOf {it.y })
    println(grid.explore())

    val extendedGrid = mutableMapOf<Coord, Int>()
    for (x in 0..4) {
        for (y in 0..4) {
            extendedGrid.putAll(grid.map { (coord, value) -> Coord(coord.x + (x * (end.x + 1)), coord.y + (y * (end.y + 1))) to ((value + x + y - 1) % 9) + 1}.toMap())
        }
    }

    println(extendedGrid.explore())
}