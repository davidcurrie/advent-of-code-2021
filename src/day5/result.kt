package day5

import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

fun main() {
    val input = File("src/day5/input.txt").readLines()
    val regex = Regex("([0-9]*),([0-9]*) -> ([0-9]*),([0-9]*)")
    val lines = input.map { regex.matchEntire(it)!!.destructured.toList().map { it.toInt() } }
        .map { Line(it[0], it[1], it[2], it[3]) }

    val rectilinear = lines.filter { it.x1 == it.x2 || it.y1 == it.y2 }
    println(solve(rectilinear))

    println(solve(lines))
}

fun solve(lines: List<Line>): Int {
    val points = mutableMapOf<Pair<Int, Int>, Int>()
    lines.forEach { line ->
        val dx = (line.x2 - line.x1).sign
        val dy = (line.y2 - line.y1).sign
        val length = max((line.x1 - line.x2).absoluteValue, (line.y1 - line.y2).absoluteValue)
        (0..length).map { i -> Pair(line.x1 + i * dx, line.y1 + i * dy) }.forEach { point ->
            points[point] = (points[point] ?: 0).plus(1)
        }
    }
    return points.count { it.value > 1 }
}