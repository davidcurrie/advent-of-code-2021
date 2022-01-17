package day23

import java.io.File
import java.util.*
import kotlin.math.abs

fun main() {
    println(solve("src/day23/input.txt"))
    println(solve("src/day23/input-part2.txt"))
}

enum class Amphipod(val stepEnergy: Int) { A(1), B(10), C(100), D(1000) }

data class Room(val natives: Amphipod, val size: Int, val entry: Int, val inhabitants: List<Amphipod>) {
    fun allHome() = full() && allNatives()
    fun full() = inhabitants.size == size
    fun allNatives() = inhabitants.all { it == natives }
    fun exit(): Pair<Amphipod, Room>? {
        if (allNatives()) return null
        if (inhabitants.isEmpty()) return null
        return Pair(inhabitants.first(), copy(inhabitants = inhabitants.subList(1, inhabitants.size)))
    }
    fun entry(a: Amphipod?): Room? {
        if (a != natives) return null
        if (!allNatives()) return null
        return copy(inhabitants = listOf(a) + inhabitants)
    }
}

data class Burrow(val hallway: List<Amphipod?>, val rooms: List<Room>, val energy: Int = 0) {
    fun allHome() = rooms.all { it.allHome() }
    fun hallwayClear(from: Int, to: Int) = (if (from < to) (from+1..to) else (to until from)).all { hallway[it] == null }
    fun entries() = rooms.map { it.entry }
    fun hallwayStops() = hallway.indices.filterNot { it in entries() }
    fun moves(): List<Burrow> {
        val roomExits = rooms.withIndex().map { (roomIndex, room) ->
            val exit = room.exit()
            if (exit == null) {
                emptyList()
            } else {
                hallwayStops().filter { hallwayClear(room.entry, it) }.map { to ->
                    val newHallway = hallway.mapIndexed { index, amphipod -> if (index == to) exit.first else amphipod }
                    val newRooms = rooms.mapIndexed { index, room ->
                        if (index == roomIndex) exit.second else room
                    }
                    val steps = 1 + abs(to - room.entry) + (room.size - room.inhabitants.size)
                    val energyUse = steps * exit.first.stepEnergy
                    Burrow(newHallway, newRooms, energy + energyUse)
                }
            }
        }.flatten()
        val roomEntries = hallway.mapIndexed { from, amphipod ->
            rooms.mapIndexedNotNull { roomIndex, room ->
                val newRoom = room.entry(amphipod)
                if (newRoom != null && hallwayClear(from, room.entry)) {
                    val newHallway = hallway.mapIndexed { index, amphipod -> if (index == from) null else amphipod }
                    val newRooms = rooms.mapIndexed { index, room -> if (index == roomIndex) newRoom else room }
                    val steps = abs(from - room.entry) + (room.size - room.inhabitants.size)
                    val energyUse = steps * amphipod!!.stepEnergy
                    Burrow(newHallway, newRooms, energy + energyUse)
                } else {
                    null
                }
            }
        }.flatten()
        return roomEntries + roomExits
    }

    fun print() {
        println("Energy: $energy")
        println(hallway.map { amphipod -> amphipod?.name ?: "." }.joinToString("") { it })
        (0 until rooms[0].size).forEach { row ->
            println(hallway.indices.map { index -> rooms.firstOrNull { room -> room.entry == index } }
                .map { room -> if (room == null) " " else (row + room.inhabitants.size - room.size).let { if (it >= 0) room.inhabitants[it].name else "." } }
                .joinToString("") { it })
        }
    }
}

fun solve(filename: String): Int? {
    val queue = PriorityQueue<List<Burrow>>(Comparator.comparingInt { it.last().energy })
    queue.add(listOf(parse(filename)))
    val visited = mutableMapOf<Pair<List<Amphipod?>, List<Room>>, Int>()
    while (queue.isNotEmpty()) {
        val burrows = queue.poll()
        val burrow = burrows.last()
        val signature = Pair(burrow.hallway, burrow.rooms)
        val previousBestEnergy = visited[signature]
        if ((previousBestEnergy != null) && (previousBestEnergy <= burrow.energy)) {
            continue
        }
        visited[signature] = burrow.energy
        if (burrow.allHome()) {
            burrows.forEach { it.print() }
            return burrow.energy
        }
        queue.addAll(burrow.moves().map { burrows + it })
    }
    return null
}

fun parse(filename: String): Burrow {
    val lines = File(filename).readLines()
    val hallway = lines[1].drop(1).dropLast(1).map { if (it == '.') null else Amphipod.valueOf(it.toString()) }
    val roomLines = lines.drop(2).dropLast(1)
    val rooms = (0..3).map { room ->
        val natives = Amphipod.values()[room]
        val inhabitants = roomLines.mapNotNull { if (it[3 + (room * 2)] == '.') null else Amphipod.valueOf(it[3 + (room * 2)].toString()) }
        Room(natives, roomLines.size, 2 + (room * 2), inhabitants)
    }
    return Burrow(hallway, rooms)
}
