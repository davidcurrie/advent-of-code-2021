import java.io.File

fun main() {
    val programs = File("src/day24/input.txt").readText().split("inp w\n").drop(1)
        .map { it.split("\n").dropLast(1).map { it.split(" ") } }
    val variables = programs.map { Variables(it[3][2].toInt(), it[4][2].toInt(), it[14][2].toInt()) }
    val solutions = solve(variables)
    println(solutions.maxOf { it })
    println(solutions.minOf { it })
}

data class Variables(val zDiv: Int, val xAdd: Int, val yAdd: Int) {
    fun execute(input: Int, zReg: Int): Int {
        val x = if (((zReg % 26) + xAdd) != input) 1 else 0
        return ((zReg / zDiv) * ((25 * x) + 1)) + ((input + yAdd) * x)
    }
}

fun solve(variablesList: List<Variables>): List<String> {

    var validZOutputs = setOf(0)
    val validZsByIndex = variablesList.reversed().map { variables ->
        (1..9).associateWith { input ->
            (0..10000000).filter { z -> variables.execute(input, z) in validZOutputs }.toSet()
        }.also { validZs ->
            validZOutputs = validZs.values.flatten().toSet()
        }
    }.reversed()

    fun findModelNumbers(index: Int, z: Int): List<String> {
        val inputs = validZsByIndex[index].entries.filter { z in it.value }.map { it.key }
        return if (index == 13) inputs.map { it.toString() } else inputs.flatMap { input ->
            val nextZ = variablesList[index].execute(input, z)
            findModelNumbers(index + 1, nextZ).map { input.toString() + it }
        }
    }

    return findModelNumbers(0, 0)
}