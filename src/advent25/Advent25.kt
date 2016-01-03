package advent25

import java.io.File
import kotlin.text.Regex

/**
 * Created by igor on 03.01.2016.
 *
 * In programming competition coding style.
 */

fun main(args: Array<String>) {
    val input = File("data\\input25.txt").readLines()
    val (tr, tc) = Regex("\\d+").findAll(input[0]).map { it.groups[0]!!.value.toInt() }.toList()
    var (r, c) = 1 to 1
    var n = 20151125L
    val m = 252533L
    val d = 33554393L
    while (r to c != tr to tc) {
        n = (n * m) % d
        r--
        c++
        if (r == 0) {
            r = c
            c = 1
        }
    }
    println(n)
}
