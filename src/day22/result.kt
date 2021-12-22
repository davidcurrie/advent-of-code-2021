package day22

import java.io.File
import kotlin.math.max
import kotlin.math.min

data class Line(val min: Int, val max: Int) {
    fun overlap(other: Line): Line? {
        val start = max(min, other.min)
        val end = min(max, other.max)
        return if (start <= end) Line(start, end) else null
    }
}

data class Cuboid(val on: Boolean, val x: Line, val y: Line, val z: Line) {
    fun volume() = (if (on) 1L else -1L) * (1 + x.max - x.min) * (1 + y.max - y.min) * (1 + z.max - z.min)
    fun intersection(other: Cuboid): Cuboid? {
        val overlapX = x.overlap(other.x)
        val overlapY = y.overlap(other.y)
        val overlapZ = z.overlap(other.z)
        if (overlapX != null && overlapY != null && overlapZ != null) {
            return Cuboid(!on, overlapX, overlapY, overlapZ)
        }
        return null
    }
}

fun calculate(cuboids: List<Cuboid>) =
    cuboids.fold(mutableListOf<Cuboid>()) { acc, c ->
        acc.addAll(acc.mapNotNull { existing -> existing.intersection(c) })
        if (c.on) {
            acc.add(c)
        }
        acc
    }.sumOf { it.volume() }

fun main() {
    val cuboids = File("src/day22/input.txt").readLines()
        .map {
            val (on, minX, maxX, minY, maxY, minZ, maxZ) =
                Regex("(on|off) x=(-?[0-9]*)..(-?[0-9]*),y=(-?[0-9]*)..(-?[0-9]*),z=(-?[0-9]*)..(-?[0-9]*)")
                    .matchEntire(it)!!.destructured
            Cuboid(
                on == "on",
                Line(minX.toInt(), maxX.toInt()),
                Line(minY.toInt(), maxY.toInt()),
                Line(minZ.toInt(), maxZ.toInt())
            )
        }

    val cuboidsInRegion = cuboids
        .filter { c ->
            c.x.min >= -50 && c.x.max <= 50
                    && c.y.min >= -50 && c.y.max <= 50
                    && c.z.min >= -50 && c.z.max <= 50
        }

    println(calculate(cuboidsInRegion))
    println(calculate(cuboids))
}