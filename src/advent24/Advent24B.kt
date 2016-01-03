package advent24B

import java.io.File

/**
 * Created by igor on 03.01.2016.
 *
 * In programming competition coding style.
 */

fun f1(ap: List<Int>, f: List<Int>, w: Int, cb: (List<Int>) -> Unit) {
    var apm = ap
    while (!apm.isEmpty()) {
        val n = apm[0]
        apm = apm.drop(1)
        val nf = f + n
        if (n == w) {
            cb(nf)
        } else if (n < w) {
            f1(apm, nf, w - n, cb)
        }
    }
}

fun cqe(nl: List<Int>): Long = nl.map { it.toLong() }.reduce { n, t -> n * t }

fun main(args: Array<String>) {
    val input = File("data\\input24.txt").readLines()
    val pl = input.map { it.toInt() }.reversed()
    val w = pl.sum() / 4
    var bl = Int.MAX_VALUE
    var bqe = Long.MAX_VALUE
    f1(pl, listOf(), w) { fp1 ->
        val qe = cqe(fp1)
        if (fp1.size < bl || (fp1.size == bl && qe < bqe)) {
            bl = fp1.size
            bqe = qe
        }
    }
    println(bqe)
}
