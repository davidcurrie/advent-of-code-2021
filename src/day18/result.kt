package day18

import java.io.File
import java.lang.IllegalStateException

interface Element

class RegularNumber(val value: Int) : Element
object Open : Element
object Close : Element

fun main() {
    val elements = File("src/day18/input.txt").readLines().map { line ->
        line.toList().map { ch ->
            when (ch) {
                '[' -> Open
                ']' -> Close
                ',' -> null
                else -> RegularNumber(ch.digitToInt())
            }
        }.filterNotNull()
    }

    println(elements.reduce { x, y -> x + y }.magnitude())

    println(sequence {
        for ((i, x) in elements.withIndex()) {
            for ((j, y) in elements.withIndex()) {
                if (i != j) yield(x + y)
            }
        }
    }.maxOf { it.magnitude() })
}

private operator fun List<Element>.plus(other: List<Element>): List<Element> {
    val result = ArrayList<Element>(this.size + other.size + 2)
    result.add(Open)
    result.addAll(this)
    result.addAll(other)
    result.add(Close)

    outer@ while (true) {
        var depth = 0
        for (i in 0 until result.size - 4) {
            if (depth > 3 && result[i] is Open && result[i + 3] is Close) { // Explode
                val left = result[i + 1]
                val right = result[i + 2]
                if (left is RegularNumber && right is RegularNumber) {
                    result[i] = RegularNumber(0)
                    result.subList(i + 1, i + 4).clear()
                    for (j in i - 1 downTo 0) {
                        val number = result[j] as? RegularNumber ?: continue
                        result[j] = RegularNumber(number.value + left.value)
                        break
                    }
                    for (j in i + 1 until result.size) {
                        val number = result[j] as? RegularNumber ?: continue
                        result[j] = RegularNumber(number.value + right.value)
                        break
                    }
                    continue@outer
                }
            }
            depth += when (result[i]) {
                Open -> 1
                Close -> -1
                else -> 0
            }
        }
        for ((i, element) in result.withIndex()) {
            if (element is RegularNumber && element.value > 9) { // Split
                result.removeAt(i)
                result.addAll(
                    i,
                    listOf(Open, RegularNumber(element.value / 2), RegularNumber((element.value + 1) / 2), Close)
                )
                continue@outer
            }
        }
        break@outer
    }
    return result
}

fun List<Element>.magnitude() = recurse(0).first

fun List<Element>.recurse(index: Int): Pair<Int, Int> {
    return when (val element = this[index]) {
        Open -> {
            val (left, leftEnd) = recurse(index + 1)
            val (right, rightEnd) = recurse(leftEnd)
            Pair(3 * left + 2 * right, rightEnd + 1)
        }
        is RegularNumber -> Pair(element.value, index + 1)
        else -> throw IllegalStateException()
    }
}