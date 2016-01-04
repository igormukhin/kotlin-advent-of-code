package advent16

import java.io.File

/**
 * Created by igor on 31.12.2015.
 */

fun main(args: Array<String>) {
    val input = File("data\\input16.txt").readLines()

    // 1
    val detects = ("children: 3, cats: 7, samoyeds: 2, pomeranians: 3, " +
            "akitas: 0, vizslas: 0, goldfish: 5, trees: 3, cars: 2, perfumes: 1").split(", ")
    input.forEachIndexed { i, line ->
        if (detects.count { line.contains(it) } == 3) {
            println(i + 1)
        }
    }

    // 2
    val detects2 = detects.fold(mapOf<String, Int>()) { map, detect ->
        val (p, n) = detect.split(": ")
        map + (p to n.toInt())
    }

    val actuallyMore = listOf("cats", "trees")
    val actuallyLess = listOf("pomeranians", "goldfish")
    input.forEachIndexed { i, line ->
        var hits = 0
        line.substringAfter(": ").split(", ").forEach {
            val (prop, sValue) = it.split(": ")
            val value = sValue.toInt()

            if (when (prop) {
                in actuallyMore -> value > detects2[prop] ?: Int.MAX_VALUE
                in actuallyLess -> value < detects2[prop] ?: Int.MIN_VALUE
                else -> value == detects2[prop]
            }) hits++
        }

        if (hits >= 3) {
            println(i + 1)
        }
    }

}
