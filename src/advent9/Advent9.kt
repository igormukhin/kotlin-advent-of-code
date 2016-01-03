package advent9

import java.io.File
import java.util.*

/**
 * Created by igor on 30.12.2015.
 */

data class Distance (val from: String, val to: String, val value: Int)


fun main(args: Array<String>) {
    val input = File("data\\input9.txt").readLines()

    val placeSet = HashSet<String>()
    val dists = input.map {
        val (from, to, value) = it.splitToSequence(" to ", " = ").toArrayList()
        placeSet.add(from)
        placeSet.add(to)
        Distance(from, to, value.toInt())
    }

    val places = placeSet.toArrayList()
    val lookup = Array(places.size, { IntArray(places.size) })
    dists.forEach { dist ->
        lookup[places.indexOf(dist.from)][places.indexOf(dist.to)] = dist.value
        lookup[places.indexOf(dist.to)][places.indexOf(dist.from)] = dist.value
    }

    println(Walker(places, lookup).findShortest())
    println(Walker(places, lookup).findLongest())
}

class Walker(val places: ArrayList<String>, val lookup: Array<IntArray>) {

    private val visited = HashSet<String>()

    fun findShortest(): Int {
        var found = Int.MAX_VALUE

        places.forEach { place ->
            startIn(place) { dist ->
                found = Math.min(found, dist)
            }
        }

        return found
    }

    fun findLongest(): Int {
        var found = 0

        places.forEach { place ->
            startIn(place) { dist ->
                found = Math.max(found, dist)
            }
        }

        return found
    }

    private fun startIn(place: String, callback: (Int) -> Unit) {
        walk(place, 0, callback)
    }

    private fun walk(place: String, dist: Int, callback: (Int) -> Unit) {
        visited.add(place);
        if (places.size == visited.size) {
            callback(dist)
        } else {
            for (next in places - visited) {
                walk(next, dist + lookup[places.indexOf(place)][places.indexOf(next)], callback);
            }
        }
        visited.remove(place);
    }
}
