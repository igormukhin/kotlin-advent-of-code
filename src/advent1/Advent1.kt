package advent1

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by igor on 29.12.2015.
 */

fun main(args: Array<String>) {
    val input = Files.newBufferedReader(Paths.get("data\\input1.txt")).readLine()

    println(finishedOnFloor(input));
    println(gotToBasementOnMove(input));
}

fun finishedOnFloor(input: String): Int {
    var result = input.asSequence().map {
        when (it) {
            '(' -> 1
            ')' -> -1
            else -> throw IllegalArgumentException(it.toString())
        }
    }.sum()
    return result
}

fun gotToBasementOnMove(input: String): Int {
    var floor = 0;
    var pos = 1;
    input.asSequence().forEach {
        when (it) {
            '(' -> floor++
            ')' -> floor--
        }
        if (floor < 0) {
            return pos
        }
        pos++
    }

    throw IllegalArgumentException("Never got to the basement")
}
