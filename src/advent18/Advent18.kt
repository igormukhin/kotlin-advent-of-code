package advent18

import java.io.File
import java.util.*

/**
 * Created by igor on 01.01.2016.
 */

internal fun Boolean.toInt(): Int = if (this) 1 else 0

class Lights(val size: Int) {
    private val lights: BitSet

    init {
        lights = BitSet(size * size)
    }

    operator fun set(row: Int, col: Int, value: Boolean) = lights.set(row * size + col, value)

    operator fun get(row: Int, col: Int) =
            if (row < 0 || row >= size || col < 0 || col >= size) false
            else lights.get(row * size + col)

    fun countOnNeightbours(row: Int, col: Int): Int =
            get(row - 1, col - 1).toInt() +
                    get(row - 1, col).toInt() +
                    get(row - 1, col + 1).toInt() +
                    get(row, col - 1).toInt() +
                    get(row, col + 1).toInt() +
                    get(row + 1, col - 1).toInt() +
                    get(row + 1, col).toInt() +
                    get(row + 1, col + 1).toInt()

    fun countOn() = (1..(size * size)).map { lights[it - 1].toInt() }.sum()

    fun import(lines: List<String>) {
        lines.forEachIndexed { row, line ->
            line.forEachIndexed { col, ch ->
                this[row, col] = ch == '#'
            }
        }
    }
}

fun main(args: Array<String>) {
    val input = File("data\\input18.txt").readLines()

    val size = 100
    var lights = Lights(size)
    val steps = 100

    // 1
    lights.import(input)
    (1..steps).forEach {
        lights = Lights(size).apply {
            for (row in 0..size - 1) {
                for (col in 0..size -1) {
                    val on = lights.get(row, col)
                    val n = lights.countOnNeightbours(row, col)
                    this[row, col] = (on && n in 2..3) || (!on && n == 3)
                }
            }
        }
    }

    println(lights.countOn())

    // 2
    lights.import(input)
    val skips = arrayListOf(Pair(0, 0), Pair(size - 1, 0), Pair(size - 1, size - 1), Pair(0, size - 1))
    (1..steps).forEach {
        lights = Lights(size).apply {
            for (row in 0..size - 1) {
                for (col in 0..size -1) {
                    val on = lights.get(row, col)
                    val n = lights.countOnNeightbours(row, col)
                    this[row, col] = (on && n in 2..3) || (!on && n == 3) || Pair(row, col) in skips
                }
            }
        }
    }

    println(lights.countOn())
}
