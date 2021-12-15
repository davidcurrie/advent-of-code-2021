package day14

import java.io.File

fun solve(rules: Map<Pair<Char, Char>, Char>, initialPairCounts: Map<Pair<Char, Char>, Long>, steps: Int): Long {

    var pairCounts = initialPairCounts
    for (step in 1..steps) {
        pairCounts = pairCounts.entries.fold(emptyMap()) { acc, pairCount ->
            val ch = rules[pairCount.key]
            if (ch != null) {
                acc + Pair(pairCount.key.first, ch).let { it to (acc[it] ?: 0L) + pairCount.value } +
                        Pair(ch, pairCount.key.second).let { it to (acc[it] ?: 0L) + pairCount.value }
            } else {
                acc + (pairCount.key to pairCount.value)
            }
        }
    }

    val counts = pairCounts.entries
        .fold(emptyMap<Char, Long>()) { acc, pairCount ->
            acc + (pairCount.key.first to (acc[pairCount.key.first] ?: 0L) + pairCount.value)
        }
    return counts.values.let { it.maxOrNull()!! - it.minOrNull()!! }
}

fun main() {
    val lines = File("src/day14/input.txt").readLines()
    val template = lines[0]
    val rules = lines.drop(2).map { it.split(" -> ") }.associate { Pair(it[0][0], it[0][1]) to it[1][0] }
    val pairCounts = "$template ".zipWithNext().groupingBy { it }.eachCount().map { (k, v) -> k to v.toLong() }.toMap()

    println(solve(rules, pairCounts, 10))
    println(solve(rules, pairCounts, 40))
}