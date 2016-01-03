package advent22

import java.io.File
import java.util.*

/**
 * Created by igor on 02.01.2016.
 *
 * **I hate it. - me**
 *
 * The phase "At the start of each player turn" killed me.
 */

data class Spell(val cost: Int, val effectTurns: Int = 0, val damage: Int = 0, val heal: Int = 0,
                 val armor: Int = 0, val mana: Int = 0)

data class Boss(val hitPoints: Int, val damage: Int, val armor: Int) {
    fun lost() = (hitPoints <= 0)
}

data class Wizard(val hitPoints: Int, val damage: Int = 0, val armor: Int = 0, val mana: Int = 0) {
    fun lost() = (hitPoints <= 0)
}

fun main(args: Array<String>) {
    val input = File("data\\input22.txt").readLines()

    val boss = Boss(
            input[0].split(": ")[1].toInt(),
            input[1].split(": ")[1].toInt(),
            0
    )

    val me = Wizard(hitPoints = 50, mana = 500)

    val spells = listOf(
            Spell(53, damage = 4),
            Spell(73, damage = 2, heal = 2),
            Spell(113, effectTurns = 6, armor = 7),
            Spell(173, effectTurns = 6, damage = 3),
            Spell(229, effectTurns = 5, mana = 101))

    // 1
    var minSpentFTW = Int.MAX_VALUE
    var bestHistory: List<Spell> = listOf()
    play(me, boss, spells, hashMapOf(), 0, 1, Int.MAX_VALUE, listOf(), { spent, history ->
        if (spent < minSpentFTW) {
            minSpentFTW = spent
            bestHistory = history
        }
    }, null)

    println(minSpentFTW)
    bestHistory.forEachIndexed { i, spell -> println("${i + 1}: ${spell.cost}") }
    println()

    // 2
    minSpentFTW = Int.MAX_VALUE
    play(me, boss, spells, hashMapOf(), 0, 1, Int.MAX_VALUE, listOf(), { spent, history ->
        if (spent < minSpentFTW) {
            minSpentFTW = spent
            bestHistory = history
        }
    }, null, { me -> me.copy(hitPoints = me.hitPoints - 1) })

    println(minSpentFTW)
    bestHistory.forEachIndexed { i, spell -> println("${i + 1}: ${spell.cost}") }

}

fun play(pWizard: Wizard, pBoss: Boss, spells: List<Spell>, pEffects: Map<Spell, Int>, manaSpent: Int,
         round: Int, pMaxRound: Int, history: List<Spell>, onMeWin: (Int, List<Spell>) -> Unit,
         pReplay: List<Int>? = null, customizer: (Wizard) -> Wizard = { it }): Int {

    var maxRound = pMaxRound
    if (maxRound < round) return maxRound

    var trySpells = spells
    var replay = pReplay
    if (replay != null) {
        if (replay.isEmpty()) {
            return maxRound
        } else {
            val replayCost = replay.first()
            trySpells = listOf(spells.find { it.cost == replayCost }!!)
            replay = replay.drop(1)
        }
    }

    trySpells.forEach nextSpell@ { spell ->
        var me = pWizard.copy()
        var boss = pBoss.copy()
        var effects: Map<Spell, Int> = HashMap(pEffects)

        // my turn

        me = customizer(me)
        if (me.lost()) {
            return@nextSpell
        }

        effects.filterValues{ it > 0 }.map{ it.key }.forEach { effect ->
            boss = boss.copy(hitPoints = boss.hitPoints - effect.damage)
            me = me.copy(hitPoints = me.hitPoints + effect.heal, mana = me.mana + effect.mana)
        }

        effects = tick(effects)

        if (boss.lost()) {
            //println("I won with ${manaSpent} on before my move in round ${round}")
            onMeWin(manaSpent, history)
            maxRound = Math.min(maxRound, round)
            return@nextSpell
        }

        // do I have enough mana?
        if (me.mana < spell.cost) {
            return@nextSpell
        }

        // is the spell still active?
        if (effects.getOrElse(spell, { 0 }) > 0) {
            return@nextSpell
        }

        val newSpent = manaSpent + spell.cost
        val newHistory = history + spell
        me = me.copy(mana = me.mana - spell.cost)

        if (spell.effectTurns == 0) {
            boss = boss.copy(hitPoints = boss.hitPoints - spell.damage)
            me = me.copy(hitPoints = me.hitPoints + spell.heal)
        } else {
            effects += spell to spell.effectTurns
        }

        if (boss.lost()) {
            onMeWin(newSpent, newHistory)
            maxRound = Math.min(maxRound, round)
            return@nextSpell
        }

        // boss' turn

        var myExtraArmor = 0
        effects.filterValues{ it > 0 }.map{ it.key }.forEach { effect ->
            boss = boss.copy(hitPoints = boss.hitPoints - effect.damage)
            me = me.copy(hitPoints = me.hitPoints + effect.heal, mana = me.mana + effect.mana)
            myExtraArmor += effect.armor
        }

        effects = tick(effects)

        if (boss.lost()) {
            onMeWin(newSpent, newHistory)
            maxRound = Math.min(maxRound, round)
            return@nextSpell
        }

        me = me.copy(hitPoints = me.hitPoints - Math.max(1, boss.damage - me.armor - myExtraArmor))

        if (me.lost()) {
            return@nextSpell
        }

        maxRound = Math.min(maxRound, play(me, boss, spells, effects, newSpent, round + 1, maxRound,
                newHistory, onMeWin, replay, customizer))
    }

    return maxRound
}

fun tick(effects: Map<Spell, Int>) = effects.mapValues { entry -> Math.max(0, entry.value - 1) }
