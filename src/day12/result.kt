package day12

import java.io.File

fun Map<String, List<String>>.partOne(path: List<String>) : List<List<String>> {
    if (path.last() == "end") {
        return listOf(path)
    }
    return this[path.last()]!!
        .filter { it.uppercase() == it || !path.contains(it) }
        .map{ partOne( path + it) }
        .flatten()
}

fun Map<String, List<String>>.partTwo(path: List<String>, revisited: String? = null) : List<List<String>> {
    if (path.last() == "end") {
        return listOf(path)
    }
    return this[path.last()]!!
        .filter { it.uppercase() == it || !path.contains(it) || (it != "start" && path.contains(it) && revisited == null) }
        .map{ partTwo( path + it, if (it.lowercase() == it && path.contains(it)) it else revisited ) }
        .flatten()
}

fun main() {
    val input = File("src/day12/input.txt")
        .readLines()
        .map { it.split("-") }
    val bidirectional = input.map { (a, b) -> listOf(b, a) } + input
    val links = bidirectional.groupBy( keySelector = { it[0] }, valueTransform = { it[1] } )

    println(links.partOne(listOf("start")).size)
    println(links.partTwo(listOf("start")).size)
}