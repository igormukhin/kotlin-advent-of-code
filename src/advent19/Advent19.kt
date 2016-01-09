package advent19

import java.io.File
import java.util.*

/**
 * Created by igor on 01.01.2016.
 */

fun main(args: Array<String>) {
    val input = File("data\\input19.txt").readLines()

    val trans = input.subList(0, input.size - 2).map {
        val (from, to) = it.split(" => ")
        Pair(from, to)
    }
    val molec = input.last()

    // 1
    val created = HashSet<String>()
    trans.forEach { trans ->
        val (from, to) = trans
        var doneIdx = 0
        while (true) {
            val at = molec.indexOf(from, doneIdx)
            if (at == -1) break
            created.add(molec.substring(0, at) + to + molec.substring(at + from.length))
            doneIdx = at + 1
        }
    }

    println(created.size)

    // 2
    val sortedTrans = trans.sortedBy { - (it.second.length - it.first.length) } // by descending reduction in length

    var steps = Int.MAX_VALUE
    walkDown(molec, sortedTrans, 1, "e", {
        if (it < steps) {
            steps = it
            println("Part 2 Hit: $steps (wait a little then kill the programm, cause this may be the best value)")
        }
    }, HashMap<String, Int>())

    println(steps)
}

fun walkDown(source: String, sortedTrans: List<Pair<String, String>>, stepNumber: Int, target: String, onHit: (Int) -> Unit,
             tries: MutableMap<String, Int>) {
    if (source.length <= 10) {
        if (tries.containsKey(source)) {
            val onStep = tries[source]!!
            if (stepNumber >= onStep) {
                return
            } else {
                tries.put(source, stepNumber)
            }
        } else {
            tries.put(source, stepNumber)
        }
    }

    sortedTrans.forEach { trans ->
        val (from, to) = trans
        var doneIdx = 0
        while (true) {
            val at = source.indexOf(to, doneIdx)
            if (at == -1) break
            val modified = source.substring(0, at) + from + source.substring(at + to.length)
            if (modified == target) {
                onHit(stepNumber)
            } else {
                walkDown(modified, sortedTrans, stepNumber + 1, target, onHit, tries)
            }
            doneIdx = at + 1
        }
    }
}
