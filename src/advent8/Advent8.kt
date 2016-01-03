package advent8

import java.io.File
import kotlin.text.Regex

/**
 * Created by igor on 30.12.2015.
 */

fun main(args: Array<String>) {
    val input = File("data\\input8.txt").readLines()

    val symb = Regex("\\\\\\\\|\\\\\"|\\\\x[0-9a-f]{2}|.")

    println(input.map { it.length - symb.findAll(it.substring(1, it.length - 1)).count() }.sum())

    println(input.map { -it.length + 6 +
            symb.findAll(it.substring(1, it.length - 1)).map {
                it.groups[0] ?: throw RuntimeException()
            }.map {
                when (it.value.length) {
                    2 -> 4
                    4 -> 5
                    else -> 1
                }
            }.sum()
    }.sum());
}
