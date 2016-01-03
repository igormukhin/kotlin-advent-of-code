package advent4

import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest

/**
 * Created by igor on 29.12.2015.
 */

fun main(args: Array<String>) {
    val input = Files.newBufferedReader(Paths.get("data\\input4.txt")).readLine()

    val with5Zeros = mineAdventCoins(input, 0,
            { it[0].toInt() == 0 && it[1].toInt() == 0 && it[2].toInt() in 0..15 })
    println(with5Zeros)

    println(mineAdventCoins(input, with5Zeros,
            { it[0].toInt() == 0 && it[1].toInt() == 0 && it[2].toInt() == 0 }))
}

fun mineAdventCoins(prefix: String, startWith: Int, predicate: (ByteArray) -> Boolean): Int {
    val digester: MessageDigest = MessageDigest.getInstance("MD5");

    for (n in startWith..Int.MAX_VALUE) {
        val digest = digester.digest((prefix + n).toByteArray()) ?: ByteArray(0)
        if (predicate(digest)) {
            return n
        }
    }

    throw IllegalArgumentException("No AdventCoin found");
}
