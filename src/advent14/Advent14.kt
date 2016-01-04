package advent14

import java.io.File
import kotlin.text.Regex

/**
 * Created by igor on 30.12.2015.
 */

data class Reindeer (val speed: Int, val running: Int, val resting: Int)
data class State (var points: Int, var dist: Int, var toRun: Int, var toRest: Int)

fun main(args: Array<String>) {
    val input = File("data\\input14.txt").readLines()

    val numRegex = Regex("\\d+")
    val time = 2503
    val reindeers = input.map {
        val (s, r, w) = numRegex.findAll(it).map{ it.groups[0]!!.value.toInt() }.toArrayList()
        Reindeer(s, r, w)
    }

    // 1
    println(reindeers.map { it.ranOn(time) }.max())

    // 2
    val states = reindeers.map { State(0, 0, it.running, 0) }
    for (now in 1..time) {
        states.forEachIndexed { i, state ->
            val reindeer = reindeers[i]
            if (state.toRun > 0) {
                state.toRun--
                state.dist += reindeer.speed
                if (state.toRun == 0) {
                    state.toRest = reindeer.resting
                }
            } else {
                state.toRest--
                if (state.toRest == 0) {
                    state.toRun = reindeer.running
                }
            }
        }

        val maxDist = states.map { it.dist }.max()

        states.filter { it.dist == maxDist }.forEach { it.points++ }
    }

    println(states.map { it.points }.max())

}

fun Reindeer.ranOn(totalTime: Int): Int {
    var result = 0
    var time = totalTime
    while (time > 0) {
        val runFor = Math.min(time, running )
        time -= runFor
        result += runFor * speed

        val restFor = Math.min(time, resting )
        time -= restFor
    }
    return result
}
