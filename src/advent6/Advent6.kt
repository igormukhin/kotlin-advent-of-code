package advent6

import java.io.File
import kotlin.text.MatchResult
import kotlin.text.Regex

/**
 * Created by igor on 29.12.2015.
 */

data class Point (val x: Int, val y: Int)

val commandRegex = Regex("(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)")

fun main(args: Array<String>) {
    val input = File("data\\input6.txt").readLines()

    solveFirst(input)
    solveSecond(input)
}

fun solveFirst(input: List<String>) {
    val grid = Array(1000, { BooleanArray(1000) })

    input.forEach {
        val mr = commandRegex.matchEntire(it) ?: throw RuntimeException(it)
        applyCommand(grid,
                (mr.groups[1] ?: throw RuntimeException()).value,
                toPoint(mr, 2, 3),
                toPoint(mr, 4, 5))
    }

    println(grid.map { it.count { it } }.sum())
}

fun applyCommand(grid: Array<BooleanArray>, command: String, corner1: Point, corner2: Point) {
    assert(corner1.x <= corner2.x)
    assert(corner1.y <= corner2.y)

    val operation = when (command) {
        "turn on" -> { i: Int, j: Int -> grid[i][j] = true }
        "turn off" -> { i: Int, j: Int -> grid[i][j] = false }
        "toggle" -> { i: Int, j: Int -> grid[i][j] = !grid[i][j] }
        else -> throw IllegalArgumentException(command)
    }

    for (i in corner1.x..corner2.x) {
        for (j in corner1.y..corner2.y) {
            operation(i, j)
        }
    }
}

fun toPoint(mr: MatchResult, xGroup: Int, yGroup: Int): Point =
        Point(
                (mr.groups[xGroup] ?: throw RuntimeException()).value.toInt(),
                (mr.groups[yGroup] ?: throw RuntimeException()).value.toInt()
        )

fun solveSecond(input: List<String>) {
    val grid = Array(1000, { IntArray(1000) })

    input.forEach {
        val mr = commandRegex.matchEntire(it) ?: throw RuntimeException(it)
        val command = (mr.groups[1] ?: throw RuntimeException()).value
        applyLightnessCommand(
                toPoint(mr, 2, 3),
                toPoint(mr, 4, 5),
                when (command) {
                    "turn on" -> { i: Int, j: Int -> grid[i][j]++ }
                    "turn off" -> { i: Int, j: Int -> if (grid[i][j] > 0) grid[i][j]-- }
                    "toggle" -> { i: Int, j: Int -> grid[i][j] += 2 }
                    else -> throw IllegalArgumentException(command)
                })
    }

    println(grid.map { it.sum() }.sum())
}

fun applyLightnessCommand(corner1: Point, corner2: Point, operation: (Int, Int) -> Unit) {
    for (i in corner1.x..corner2.x) {
        for (j in corner1.y..corner2.y) {
            operation(i, j)
        }
    }
}
