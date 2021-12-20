package day20

import java.io.File

fun Array<Array<Boolean>>.count() = sumOf { row -> row.count { it } }

fun Array<Array<Boolean>>.enhance(algo: List<Boolean>, infinity: Boolean) =
    (-1..size).map { y ->
        (-1..this[0].size).map { x ->
            algo[index(x, y, infinity)]
        }.toTypedArray()
    }.toTypedArray()

fun Array<Array<Boolean>>.index(x: Int, y: Int, infinity: Boolean) =
    (-1..1).map { dy -> (-1..1).map { dx -> if (get(x + dx, y + dy, infinity)) 1 else 0 } }
        .flatten().joinToString("").toInt(2)

fun Array<Array<Boolean>>.get(x: Int, y: Int, infinity: Boolean) =
    if (y < 0 || y >= size || x < 0 || x >= this[0].size) infinity else this[y][x]

fun main() {
    val lines = File("src/day20/input.txt").readLines()
    val algo = lines[0].toList().map { it == '#' }

    var pixels = lines.drop(2)
        .map { line -> line.toList().map { (it == '#') }.toTypedArray() }.toTypedArray()

    repeat(2) {
        pixels = pixels.enhance(algo, algo[0] && (it % 2 != 0))
    }
    println(pixels.count())

    repeat(48) {
        pixels = pixels.enhance(algo, algo[0] && (it % 2 != 0))
    }
    println(pixels.count())
}