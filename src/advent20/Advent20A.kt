package advent20A

import java.io.File
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by igor on 01.01.2016.
 */

fun main(args: Array<String>) {
    val input = File("data\\input20.txt").readLines()

    val targetPresents = input.first().toInt() / 10

    // 1
    val solutions = Collections.synchronizedSet(HashSet<Int>())
    val nextChunk = AtomicInteger(0)

    loadCPU { threadNum ->
        while (solutions.isEmpty()) {
            val myChunk = nextChunk.andIncrement
            var lastPresents = 0
            for (house in (myChunk * 1000)..((myChunk + 1) * 1000) - 1) {
                var presents = 1 + house
                var elf = 2
                var maxElf = Math.sqrt(house.toDouble()).toInt()
                while (elf <= maxElf) {
                    if (house % elf == 0) {
                        presents += elf
                        val otherElf = house / elf
                        if (elf != otherElf) presents += otherElf
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
    }

    println("Lowest house is ${solutions.min()}")

}

fun loadCPU(task: (Int) -> Unit) {
    val cores = Runtime.getRuntime().availableProcessors()
    executeTaskTimes(cores, task)
}

fun executeTaskTimes(times: Int, task: (Int) -> Unit) {
    val executor = Executors.newFixedThreadPool(times)

    repeat (times) { threadNum ->
        executor.submit {
            println("Thread $threadNum started")
            task(threadNum)
        }
    }

    executor.shutdown()
    executor.awaitTermination(1, TimeUnit.DAYS)
}
