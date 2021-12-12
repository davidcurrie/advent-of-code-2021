package day9

import java.io.File

data class Coord(val x: Int, val y: Int)

fun Map<Coord, Int>.neighbours(coord: Coord) =
    listOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1))
        .map { Coord(coord.x + it.first, coord.y + it.second) }
        .filter { contains(it) }
        .toSet()

fun Map<Coord, Int>.isLowest(coord: Coord) =
    neighbours(coord).map { this[coord]!! < this[it]!! }.all { it }

fun Map<Coord, Int>.basin(coord: Coord, visited: Set<Coord>): Set<Coord> {
    if (visited.contains(coord)) {
        return visited
    }
    return neighbours(coord)
        .filter { this[it] != 9 && this[coord]!! < this[it]!! }
        .fold(visited + coord) { acc, neighbour -> acc + basin(neighbour, acc) }
}

fun main() {
    val grid = File("src/day9/input.txt").readLines()
        .mapIndexed { i, row -> row.toList().mapIndexed { j, ch -> Coord(i, j) to Character.getNumericValue(ch) } }
        .flatten()
        .toMap()

    val lowest = grid.filter { (coord, _) -> grid.isLowest(coord) }
    println(lowest.values.sumOf { it + 1 })
    println(lowest.keys
        .map { grid.basin(it, emptySet()).size }
        .sortedDescending()
        .take(3)
        .reduce(Int::times))
}