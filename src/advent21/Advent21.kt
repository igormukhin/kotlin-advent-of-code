package advent21

import java.io.File
import java.util.*

/**
 * Created by igor on 01.01.2016.
 */

data class ShopItem(val cost: Int, val damage: Int, val armor: Int)
data class Fighter(val hitPoints: Int, val damage: Int, val armor: Int) {
    fun lost() = hitPoints <= 0

    fun hit(opponent: Fighter): Fighter {
        val hit = Math.max(1, damage - opponent.armor)
        return opponent.copy(hitPoints = opponent.hitPoints - hit)
    }
}

fun main(args: Array<String>) {
    val input = File("data\\input21.txt").readLines()

    val boss = Fighter(
            input[0].split(": ")[1].toInt(),
            input[1].split(": ")[1].toInt(),
            input[2].split(": ")[1].toInt()
    )

    val shopWeapons = listOf(
            ShopItem(8, 4, 0),
            ShopItem(10, 5, 0),
            ShopItem(25, 6, 0),
            ShopItem(40, 7, 0),
            ShopItem(74, 8, 0)
        )

    val shopArmors = listOf(
            ShopItem(13, 0, 1),
            ShopItem(31, 0, 2),
            ShopItem(53, 0, 3),
            ShopItem(75, 0, 4),
            ShopItem(102, 0, 5)
        )

    val shopRings = listOf(
            ShopItem(25, 1, 0),
            ShopItem(50, 2, 0),
            ShopItem(100, 3, 0),
            ShopItem(20, 0, 1),
            ShopItem(40, 0, 2),
            ShopItem(80, 0, 3)
        )

    val myHitpoints = 100

    // 1
    var minPriceFTW = Int.MAX_VALUE
    var maxPriceStillLose = 0
    shopWeapons.forEach { myWeapon ->
        (0..1).forEach { toBuyArmors ->
            variations(shopArmors, toBuyArmors).forEach { myArmors ->
                (0..2).forEach { toBuyRings ->
                    variations(shopRings, toBuyRings).forEach { myRings ->
                        val allItems = ArrayList<ShopItem>()
                        allItems.add(myWeapon)
                        allItems.addAll(myArmors)
                        allItems.addAll(myRings)

                        val me = Fighter (
                                myHitpoints,
                                allItems.map{ it.damage }.sum(),
                                allItems.map{ it.armor }.sum()
                        )

                        val totalCost = allItems.map{ it.cost }.sum()
                        if (isFirstFighterWins(me, boss)) {
                            minPriceFTW = Math.min(totalCost, minPriceFTW)
                        } else {
                            maxPriceStillLose = Math.max(totalCost, maxPriceStillLose)
                        }
                    }
                }
            }
        }
    }

    // 1
    println(minPriceFTW)

    // 2
    println(maxPriceStillLose)
}

fun isFirstFighterWins(player: Fighter, opponent: Fighter): Boolean {
    var p1 = player
    var p2 = opponent
    while (true) {
        p2 = p1.hit(p2)
        if (p2.lost()) return true

        p1 = p2.hit(p1)
        if (p1.lost()) return false
    }
}

fun variations(shopItems: List<ShopItem>, toBuy: Int): Sequence<List<ShopItem>> {
    if (toBuy == 0) {
        return sequenceOf(ArrayList<ShopItem>())
    } else if (toBuy == shopItems.size) {
        return sequenceOf(shopItems)
    } else if (shopItems.isEmpty()) {
        return sequenceOf()
    }

    var itemsPool = shopItems
    var item: ShopItem
    var subiter: Iterator<List<ShopItem>>

    fun nextItem() {
        item = itemsPool.first()
        itemsPool = itemsPool.drop(1)
        subiter = variations(itemsPool, toBuy - 1).iterator()
    }

    nextItem()

    fun nextResult(): List<ShopItem>? {
        if (subiter.hasNext()) {
            return ArrayList<ShopItem>() + item + subiter.next()
        } else if (!itemsPool.isEmpty()) {
            nextItem()
            return nextResult()
        } else {
            return null
        }
    }

    return sequence { nextResult() }
}

