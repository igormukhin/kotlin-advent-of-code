package advent11

import java.io.File
import java.util.*
import kotlin.text.Regex

/**
 * Created by igor on 30.12.2015.
 */

fun main(args: Array<String>) {
    val input = File("data\\input11.txt").readText()

    var pass = nextSecurePass(input)
    println(pass)
    println(nextSecurePass(pass))
}

fun nextSecurePass(pass: String): String {
    var pass1 = pass
    do {
        pass1 = nextPass(pass1)
    } while (!isSecure(pass1))
    return pass1
}

val badLetters = Regex("i|o|l")

fun isSecure(pass: String): Boolean {
    if (!hasSequence(pass)) {
        return false
    }

    if (badLetters.containsMatchIn(pass)) {
        return false
    }

    if (countPairs(pass) < 2) {
        return false
    }

    return true
}

fun countPairs(pass: String): Int {
    val found = HashSet<Char>()
    for (i in 0..pass.length - 2) {
        if (pass[i] == pass[i + 1]) {
            found.add(pass[i])
        }
    }
    return found.size
}

fun hasSequence(pass: String): Boolean {
    var ch = pass[0]
    var len = 1
    for (i in 1..pass.length - 1) {
        if (pass[i] == ch + 1) {
            len++
            if (len >= 3) return true
        } else {
            len = 1
        }
        ch = pass[i]
    }
    return false
}

fun nextPass(pass: String): String {
    var next = StringBuilder(pass)
    var i = next.length - 1
    while (i > 0) {
        if (next[i] == 'z') {
            next[i] = 'a'
        } else {
            next[i]++
            break
        }
        i--
    }
    return next.toString()
}
