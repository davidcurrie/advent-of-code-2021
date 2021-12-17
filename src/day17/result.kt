package day17

import java.io.File
import kotlin.math.abs
import kotlin.math.max

fun main() {
    val input = Regex("([0-9]*)..([0-9]*), y=(-?[0-9]*)..(-?[0-9]*)")
        .find(File("src/day17/input.txt").readText())
    val (targetX1, targetX2, targetY1, targetY2) = input!!.destructured.toList().map { it.toInt() }

    var overallMaxY = 0
    var hits = 0

    for (initVX in 1..targetX2) {
        for (initVY in targetY1..abs(targetY1)) {

            var x = 0
            var y = 0
            var vx = initVX
            var vy = initVY
            var maxY = 0

            while (x <= targetX2 && y >= targetY1) {
                x += vx
                y += vy
                maxY = max(maxY, y)
                vx = max(0, vx - 1)
                vy -= 1

                if (x in targetX1..targetX2 && y in targetY1..targetY2) {
                    overallMaxY = max(overallMaxY, maxY)
                    hits++
                    break
                }
            }
        }
    }

    println(overallMaxY)
    println(hits)
}