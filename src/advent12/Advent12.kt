package advent12

import java.io.File
import java.util.*
import kotlin.text.Regex

/**
 * Created by igor on 30.12.2015.
 */

fun main(args: Array<String>) {
    val input = File("data\\input12.txt").readText()
    val numRegex = Regex("-?\\d+")
    println(numRegex.findAll(input).map { it.groups[0]!!.value.toInt() }.sum())

    val termRegex = Regex("-?\\d+|\\{|\\}|\\:\\\"red\\\"")
    val terms = termRegex.findAll(input).map { it.groups[0]!!.value }.toArrayList()
    println(sumNoRed(terms, 0).second)
}

fun sumNoRed(terms: ArrayList<String>, startAt: Int): Pair<Int, Int> {
    if (terms[startAt] != "{") throw IllegalStateException()

    var i = startAt + 1;
    var sum = 0;
    var red = false;
    while (true) {
        when (terms[i]) {
            "{" -> {
                val (j, subsum) = sumNoRed(terms, i)
                i = j
                sum += subsum
            }
            "}" -> return i to (if (red) 0 else sum)
            ":\"red\"" -> red = true
            else -> sum += terms[i].toInt()
        }
        i++
    }
}
