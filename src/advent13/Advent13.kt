package advent13

import java.io.File
import java.util.*
import kotlin.text.Regex

/**
 * Created by igor on 30.12.2015.
 */

fun main(args: Array<String>) {
    val input = File("data\\input13.txt").readLines()

    val lineRegex = Regex("(\\w+) would (gain|lose) (\\d+) happiness units by sitting next to (\\w+).")
    val persons = input.map { lineRegex.matchEntire(it)!!.groups[1]!!.value }.distinct()

    // " + 1" is needed for the second task of the day
    val points = Array(persons.size + 1, { IntArray(persons.size + 1) })
    input.map { lineRegex.matchEntire(it)!!.groups }.forEach { mgc ->
        points[persons.indexOf(mgc[1]!!.value)][persons.indexOf(mgc[4]!!.value)] =
                mgc[3]!!.value.toInt() * (if (mgc[2]!!.value == "gain") 1 else -1)
    }

    // 1
    println((0..persons.size - 1).toArrayList().permutations().map {
        happiness(it, points)
    }.max())

    // 2
    println((0..persons.size).toArrayList().permutations().map {
        happiness(it, points)
    }.max())
}

fun happiness(persons: List<Int>, points: Array<IntArray>): Int {
    var result = 0

    persons.forEachIndexed { i, p ->
        val onTheRight = persons[if (i + 1 >= persons.size) 0 else i + 1]
        val onTheLeft = persons[if (i - 1 < 0) persons.size - 1 else i - 1]
        result += points[p][onTheRight] + points[p][onTheLeft]
    }

    return result
}

// honestly stolen from https://github.com/kotlin-projects/kotlin-euler/blob/4e883bcf9c15f4330f5db181a1a33773cdcdc62e/src/main/kotlin/euler/Iterators.kt
// and reformatted
fun <T : Any> List<T>.permutations() : Sequence<List<T>> {
    if (size == 1) {
        return sequenceOf(this)
    } else {
        val iterator = iterator()
        var head = iterator.next()
        var permutations = (this - head).permutations().iterator()

        fun nextPermutation(): List<T>? {
            if (permutations.hasNext()) {
                return ArrayList<T>() + head + permutations.next()
            } else {
                if (iterator.hasNext()) {
                    head = iterator.next()
                    permutations = (this - head).permutations().iterator()
                    return nextPermutation()
                } else {
                    return null
                }
            }
        }

        return sequence { nextPermutation() }
    }
}
