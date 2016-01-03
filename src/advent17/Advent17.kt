package advent17

import java.io.File
import java.util.*

/**
 * Created by igor on 31.12.2015.
 */

fun main(args: Array<String>) {
    val input = File("data\\input17.txt").readLines()

    val conts = input.map(String::toInt).toArrayList()
    val total = 150

    var minLength = Int.MAX_VALUE
    var size1 = 0
    variations(conts, total).forEach {
        // 1
        size1++

        // 2
        minLength = Math.min(it.size, minLength)
    }
    // 1
    println(size1)

    // 2
    println(variations(conts, total).filter { it.size == minLength }.count())
}

fun variations(items: List<Int>, sum: Int): Sequence<List<Int>> {
    if (items.min() ?: Int.MAX_VALUE > sum) { // also considers .isEmpty
        return sequenceOf()
    }

    var value = 0
    var subsum = 0
    var subitems = items.filter { it <= sum }
    var subiter: Iterator<List<Int>>

    fun nextValue() {
        value = subitems.first()
        subsum = sum - value
        subitems = subitems.drop(1)
        subiter = variations(subitems, subsum).iterator()
    }

    fun nextResult(): List<Int>? {
        if (value == 0) {
            if (subitems.isEmpty()) {
                return null
            } else {
                nextValue()
            }
        }

        if (subsum == 0) {
            val result = ArrayList<Int>() + value
            value = 0
            return result
        } else if (subiter.hasNext()) {
            return ArrayList<Int>() + value + subiter.next()
        } else if (!subitems.isEmpty()) {
            nextValue()
            return nextResult()
        } else {
            return null
        }
    }

    return sequence { nextResult() }
}
