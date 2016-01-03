package advent7

import java.io.File
import java.util.*
import kotlin.text.Regex

/**
 * Created by igor on 30.12.2015.
 */

fun main(args: Array<String>) {
    val input = File("data\\input7.txt").readLines()

    val a = solve(input, {})
    println(a)
    println(solve(input, { it["b"]?.value = a }))
}

fun solve(input: List<String>, customizer: (HashMap<String, Node>) -> Unit): Int {
    val nodeRegistry = HashMap<String, Node>();
    val instructions = parseInstructions(input, nodeRegistry)
    customizer(nodeRegistry)

    return executeTillSignal(instructions, nodeRegistry, "a")
}

fun executeTillSignal(instructions: List<Instruct>, nodeRegistry: HashMap<String, Node>, resultNodeName: String): Int {
    val resultNode = nodeRegistry[resultNodeName] ?: throw IllegalArgumentException("Node $resultNodeName not registered")
    var changes: Int;
    do {
        changes = 0;

        instructions.forEach {
            if (it.execute()) changes++
        }

        if (resultNode.value != -1) {
            return resultNode.value;
        }

    } while(changes > 0);

    throw RuntimeException("No solution!")
}

data class Node(val name: String, var value: Int = -1)

enum class Operation {
    RSHIFT, LSHIFT, AND, OR, NOT, WIRE
}

data class Instruct(val source1: Node, val operation: Operation, val source2: Node, val target: Node) {
    fun execute(): Boolean {
        if (target.value != -1) return false
        if (source1.value == -1 || source2.value == -1) return false

        target.value = when (operation) {
            Operation.RSHIFT -> source1.value shr source2.value
            Operation.LSHIFT -> source1.value shl source2.value
            Operation.AND -> source1.value and source2.value
            Operation.OR -> source1.value or source2.value
            Operation.NOT -> source2.value.inv() and 0xFFFF
            Operation.WIRE -> source2.value
            else -> throw RuntimeException(operation.toString())
        }

        return true;
    }
}

fun parseInstructions(lines: List<String>, nodeRegistry: HashMap<String, Node>): List<Instruct> {
    return lines.map { line ->
        if (line.startsWith("NOT ")) {
            parseInstruction("1 " + line, nodeRegistry)
        } else if (line.count { it == ' ' } == 2) {
            parseInstruction("1 WIRE " + line, nodeRegistry);
        } else {
            parseInstruction(line, nodeRegistry)
        }
    }
}

val instructionRegex = Regex("(\\w+) (\\w+) (\\w+) -> (\\w+)")

fun parseInstruction(line: String, nodeRegistry: HashMap<String, Node>): Instruct {
    val mr = instructionRegex.matchEntire(line) ?: throw IllegalArgumentException(line)
    var source1 = (mr.groups[1] ?: throw RuntimeException()).value
    var oper = (mr.groups[2] ?: throw RuntimeException()).value
    var source2 = (mr.groups[3] ?: throw RuntimeException()).value
    var target = (mr.groups[4] ?: throw RuntimeException()).value

    return Instruct(
                source1 = parseNode(source1, nodeRegistry),
                operation = Operation.valueOf(oper),
                source2 = parseNode(source2, nodeRegistry),
                target = parseNode(target, nodeRegistry)
            )
}

val numberRegex = Regex("\\d+")

fun parseNode(value: String, nodeRegistry: HashMap<String, Node>): Node {
    return if (numberRegex.matchEntire(value) != null) {
        Node("NUMBER", value.toInt())
    } else {
        nodeRegistry.getOrPut(value, { Node(value, -1) })
    }
}

