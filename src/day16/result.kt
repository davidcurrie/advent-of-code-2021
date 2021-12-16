package day16

import java.io.File
import java.lang.IllegalStateException
import kotlin.math.max
import kotlin.math.min

data class Packet(val length: Int, val versionSum: Int, val value: Long)
data class Literal(val length: Int, val value: Long)

data class Bits(val input: String) {
    private var index = 0
    private fun getString(length: Int): String {
        val result = input.substring(index, index + length)
        index += length
        return result
    }
    private fun getInt(length: Int) = getString(length).toInt(2)
    private fun getLong(length: Int) = getString(length).toLong(2)
    private fun getLiteral(): Literal {
        val startIndex = index
        var result = ""
        do {
            val hasNext = getInt(1) == 1
            result += getString(4)
        } while (hasNext)
        return Literal(index - startIndex, result.toLong(2))
    }

    fun getPacket(): Packet {
        val version = getInt(3)
        val type = getInt(3)

        if (type == 4) {
            return getLiteral().let { Packet(it.length + 6, version, it.value) }
        } else {
            val lengthType = getInt(1)
            val subPackets = mutableListOf<Packet>()
            if (lengthType == 0) {
                val totalLength = getLong(15)
                val startIndex = index
                while (index < startIndex + totalLength) {
                    subPackets += getPacket()
                }
            } else {
                val numSubPackets = getLong(11)
                for (i in 0 until numSubPackets) {
                    subPackets += getPacket()
                }
            }
            val fn: (Long, Long) -> Long = when (type) {
                0 -> Long::plus
                1 -> Long::times
                2 -> ::min
                3 -> ::max
                5 -> { p1, p2 -> if (p1 > p2) 1 else 0 }
                6 -> { p1, p2 -> if (p1 < p2) 1 else 0 }
                7 -> { p1, p2 -> if (p1 == p2) 1 else 0 }
                else -> throw IllegalStateException()
            }
            return subPackets
                .reduce { p1, p2 ->
                    Packet(
                        p1.length + p2.length,
                        p1.versionSum + p2.versionSum,
                        fn(p1.value, p2.value)
                    )
                }
                .let { it.copy(length = it.length + 6) }
        }
    }
}

fun main() {
    val bits = Bits(File("src/day16/input.txt").readText()
        .map { hex -> Integer.parseInt(hex.toString(), 16) }
        .map { int -> Integer.toBinaryString(int) }
        .map { str -> str.padStart(4, '0') }
        .joinToString(""))
    bits.getPacket().let {
        println(it.versionSum)
        println(it.value)
    }
}