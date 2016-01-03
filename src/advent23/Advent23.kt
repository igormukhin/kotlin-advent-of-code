package advent23

import java.io.File
import java.math.BigDecimal
import kotlin.math.div
import kotlin.math.mod
import kotlin.math.plus
import kotlin.math.times

/**
 * Created by igor on 03.01.2016.
 *
 * In programming competition coding style.
 */

fun Int.bd() : BigDecimal = BigDecimal(this)

fun main(args: Array<String>) {
    val input = File("data\\input23.txt").readLines()

    fun p1(s: String) = s.substringAfter(' ').substringBefore(',')
    fun p1r(s: String) = (p1(s)[0] - 'a').toInt()
    fun p1n(s: String) = p1(s).toInt()
    fun p2n(s: String) = s.substringAfter(", ").toInt()

    val r = Array(2, { BigDecimal.ZERO })

    fun exec() {
        var i = 0
        w@ while (i < input.size) {
            var c = input[i]
            i = when (c.substring(0, 3)) {
                "hlf" -> { r[p1r(c)] /= 2.bd(); i + 1 }
                "tpl" -> { r[p1r(c)] *= 3.bd(); i + 1 }
                "inc" -> { r[p1r(c)] += 1.bd(); i + 1 }
                "jmp" -> i + p1n(c)
                "jie" -> if (r[p1r(c)] % 2.bd() == 0.bd()) i + p2n(c) else i + 1
                "jio" -> if (r[p1r(c)] == 1.bd()) i + p2n(c) else i + 1
                else -> break@w
            }
        }

        println(r[1])
    }

    // 1
    exec()

    // 2
    r[0] = BigDecimal.ONE
    r[1] = BigDecimal.ZERO
    exec()
}
