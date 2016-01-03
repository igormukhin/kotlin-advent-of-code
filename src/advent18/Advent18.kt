package advent18

import java.io.File
import java.util.*

/**
 * Created by igor on 01.01.2016.
 */

class Lights(val size: Int) {
    private val lights: BitSet

    init {
        lights = BitSet(size * size)
    }

    fun set(row: Int, col: Int, value: Boolean) = lights.set(row * size + col, value)

    fun get(row: Int, col: Int) = if (row < 0 || row >= size || col < 0 || col >= size) false
            else lights.get(row * size + col)

    fun countOnNeightbours(row: Int, col: Int): Int =
            bool2int(get(row - 1, col - 1)) +
                    bool2int(get(row - 1, col)) +
                    bool2int(get(row - 1, col + 1)) +
                    bool2int(get(row, col - 1)) +
                    bool2int(get(row, col + 1)) +
                    bool2int(get(row + 1, col - 1)) +
                    bool2int(get(row + 1, col)) +
                    bool2int(get(row + 1, col + 1))

    fun countOn() = (1..(size * size)).map { bool2int(lights.get(it - 1)) }.sum()

    fun print() {
        (0..size - 1).forEach { row ->
            (0..size - 1).forEach { col ->
                val on = get(row, col)
                print(if (on) '#' else '.')
            }
            println()
        }
        println()
    }

    fun import(lines: List<String>) {
        lines.forEachIndexed { row, line ->
            line.forEachIndexed { col, ch ->
                set(row, col, ch == '#')
            }
        }
    }
}

internal fun bool2int(value: Boolean) = if (value) 1 else 0

fun main(args: Array<String>) {
    val input = File("data\\input18.txt").readLines()

    val size = 100
    var lights = Lights(size)
    val steps = 100

    // 1
    lights.import(input)
    (1..steps).forEach {
        val updated = Lights(size)
        (0..size - 1).forEach { row ->
            (0..size - 1).forEach { col ->
                val on = lights.get(row, col)
                val n = lights.countOnNeightbours(row, col)
                updated.set(row, col, (on && n in 2..3) || (!on && n == 3))
            }
        }
        lights = updated
    }

    println(lights.countOn())

    // 2
    lights.import(input)
    val skips = arrayListOf(Pair(0, 0), Pair(size - 1, 0), Pair(size - 1, size - 1), Pair(0, size - 1))
    (1..steps).forEach {
        val updated = Lights(size)
        (0..size - 1).forEach { row ->
            (0..size - 1).forEach { col ->
                val on = lights.get(row, col)
                val n = lights.countOnNeightbours(row, col)
                updated.set(row, col, (on && n in 2..3) || (!on && n == 3) || Pair(row, col) in skips)
            }
        }
        lights = updated
    }

    println(lights.countOn())
}
