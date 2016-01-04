package advent20A

import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by igor on 01.01.2016.
 */

fun main(args: Array<String>) {
    val input = File("data\\input20.txt").readLines()

    val targetPresents = input.first().toInt()

    // 2
    val solutions = Collections.synchronizedSet(HashSet<Int>())
    val nextChunk = AtomicInteger(0)
    val threadGroup = ThreadGroup("workers")

    (1..Runtime.getRuntime().availableProcessors()).forEach { threadNum ->
        Thread(threadGroup, {
            println("Starting thread $threadNum")
            while (solutions.isEmpty()) {
                val myChunk = nextChunk.andIncrement
                var lastPresents = 0
                for (house in (myChunk * 1000)..((myChunk + 1) * 1000) - 1) {
                    var presents = 0
                    var elf = 1
                    var maxElf = Math.sqrt(house.toDouble()).toInt()
                    while (elf <= maxElf) {
                        if (house % elf == 0) {
                            val otherElf = house / elf
                            if (otherElf <= 50) {
                                presents += elf * 11
                            }

                            if (elf != otherElf && elf <= 50) {
                                presents += otherElf * 11
                            }
                        }
                        elf++
                    }

                    lastPresents = presents
                    if (presents > targetPresents) {
                        solutions.add(house)
                        break
                    }
                }

                println("Thread $threadNum finished with chunk $myChunk where last house had $lastPresents presents");
            }
        }).start()
    }

    while (threadGroup.activeCount() > 0) {
        try {
            Thread.sleep(100L)
        } catch(e: InterruptedException) {
            break;
        }
    }

    println("Lowest house is ${solutions.min()}")

    // 2
}
