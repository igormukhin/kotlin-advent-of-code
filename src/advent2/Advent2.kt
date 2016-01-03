package advent2

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by igor on 29.12.2015.
 */

data class Box(val l: Int, val w: Int, val h: Int)

fun String.parseBox(): Box {
    val tokens = this.split('x')
    return Box(tokens[0].toInt(), tokens[1].toInt(), tokens[2].toInt())
}

fun Box.wrappingPaper(): Int {
    val sides = arrayListOf(l * w, w * h, h * l)
    val smallestSide = sides.min() ?: 0
    val surface = sides.sum() * 2
    return surface + smallestSide
}

fun Box.ribbonLength(): Int {
    val lengths = arrayListOf(l + w, w + h, h + l)
    val ribbon = 2 * (lengths.min() ?: 0)
    val bow = l * w * h
    return ribbon + bow
}

fun main(args: Array<String>) {
    val inputLines = Files.readAllLines(Paths.get("data\\input2.txt"))
    val boxes = inputLines.map(String::parseBox)

    println(paperToOrder(boxes));
    println(ribbonToOrder(boxes));
}

fun paperToOrder(boxes: List<Box>): Int =
        boxes.map(Box::wrappingPaper).sum()

fun ribbonToOrder(boxes: List<Box>) =
        boxes.map(Box::ribbonLength).sum()

