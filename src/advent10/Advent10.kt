package advent10

import java.io.File

/**
 * Created by igor on 30.12.2015.
 */

fun main(args: Array<String>) {
    val input = File("data\\input10.txt").readText()

    var word = input
    for (i in 1..40) {
        word = lookAndSay(word)
    }

    println(word.length)

    for (i in 1..10) {
        word = lookAndSay(word)
    }

    println(word.length)
}

fun lookAndSay(word: String): String {
    val result = StringBuilder()
    var i = 0;
    while (i < word.length) {
        val ch = word[i]
        var rep = 1
        while (i + rep < word.length && word[i + rep] == ch) rep++
        result.append('0' + rep).append(ch)
        i += rep
    }
    return result.toString()
}
