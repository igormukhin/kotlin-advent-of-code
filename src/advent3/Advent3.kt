package advent3

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * Created by igor on 29.12.2015.
 */

data class House(val x: Int = 0, val y: Int = 0)

fun House.move(dir: Char): House {
    return when (dir) {
        '^' -> House(x, y + 1)
        'v' -> House(x, y - 1)
        '>' -> House(x + 1, y)
        '<' -> House(x - 1, y)
        else -> throw IllegalArgumentException(dir.toString())
    }
}

fun main(args: Array<String>) {
    val input = Files.newBufferedReader(Paths.get("data\\input3.txt")).readLine()

    println(visitedHousesSantaOnly(input))
    println(visitedHousesSantaAndRoboSanta(input))
}

fun visitedHousesSantaOnly(moves: String): Int {
    val visited = HashSet<House>()

    followInstruction(moves.asSequence(), visited, House())

    return visited.size
}

fun followInstruction(moves: Sequence<Char>, visited: MutableSet<House>, startIn: House) {
    var lastHouse = startIn
    visited.add(lastHouse)

    moves.forEach {
        lastHouse = lastHouse.move(it)
        visited.add(lastHouse)
    }
}

fun visitedHousesSantaAndRoboSanta(moves: String): Int {
    val visited = HashSet<House>()

    followInstruction(moves.asSequence().filterIndexed { i, c -> i % 2 == 0 }, visited, House())
    followInstruction(moves.asSequence().filterIndexed { i, c -> i % 2 == 1 }, visited, House())

    return visited.size
}
