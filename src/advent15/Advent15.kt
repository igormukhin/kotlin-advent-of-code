package advent15

import java.io.File
import java.util.*
import kotlin.text.Regex

/**
 * Created by igor on 30.12.2015.
 */

data class Ingr (val props: List<Int>, val calo: Int)

fun main(args: Array<String>) {
    val input = File("data\\input15.txt").readLines()

    val numRegex = Regex("-?\\d+")
    val ingrs = input.map {
        val ints = numRegex.findAll(it).map{ it.groups[0]!!.value.toInt() }.toArrayList()
        Ingr(ints.subList(0, 4), ints[4])
    }

    // 1
    println(variations(ingrs.size, 100).map { portions ->
        val propVals = IntArray(4)
        for (pi in 0..3) {
            portions.forEachIndexed { ingrIndex, spoons ->
                propVals[pi] += ingrs[ingrIndex].props[pi] * spoons
            }
        }
        
        propVals.map { if (it < 0) 0 else it }.reduce { total, next -> total * next }
    }.max())

    // 2
    val exactCalories = 500
    println(variations(ingrs.size, 100).map { portions ->
        val cals = portions.mapIndexed { ingrIndex, spoons ->
            ingrs[ingrIndex].calo * spoons
        }.sum()

        if (cals == exactCalories) {
            val propVals = IntArray(4)
            for (pi in 0..3) {
                portions.forEachIndexed { ingrIndex, spoons ->
                    propVals[pi] += ingrs[ingrIndex].props[pi] * spoons
                }
            }

            propVals.map { if (it < 0) 0 else it }.reduce { total, next -> total * next }
        } else {
            0
        }
    }.max())
}

fun variations(numbers: Int, sum: Int): Sequence<List<Int>> {
    if (numbers == 1) {
        return sequenceOf(arrayListOf(sum))
    }

    var value = 1
    val maxValue = sum - numbers + 1;
    var subvars = variations(numbers - 1, sum - value).iterator()

    fun next(): List<Int>? {
        if (subvars.hasNext()) {
            return ArrayList<Int>() + value + subvars.next()
        } else if (value < maxValue) {
            value++
            subvars = variations(numbers - 1, sum - value).iterator()
            return next()
        } else {
            return null
        }
    }

    return sequence { next() }
}
