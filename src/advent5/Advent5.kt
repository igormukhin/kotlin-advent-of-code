package advent5

import java.io.File

/**
 * Far better solution using Regex can be found here: https://github.com/gumballhead/advent-of-code-kotlin/tree/master/Day5
 *
 * Created by igor on 29.12.2015.
 */

fun main(args: Array<String>) {
    val input = File("data\\input5.txt").readLines()

    println(input.asSequence().count(::isNiceWord))
    println(input.asSequence().count(::isNiceWordNew))

}

val BAD_STRINGS = arrayListOf("ab", "cd", "pq", "xy")

fun isNiceWord(word: String): Boolean =
        word.asSequence().count { it in "aeiou" } >= 3
            && word.substring(1).asSequence().filterIndexed { i, c -> c == word[i] }.count() != 0
            && BAD_STRINGS.none { it in word }


fun isNiceWordNew(word: String): Boolean =
        word.substring(1).asSequence().filterIndexed { i, c ->
                word.indexOf(word.substring(i, i + 2), startIndex = i + 2) != -1 }.count() != 0
            && word.substring(2).asSequence().filterIndexed { i, c -> c == word[i] }.count() != 0