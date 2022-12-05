import java.util.Scanner
import java.util.Stack

fun main() {
    fun buildStacks(startingStack: List<String>): MutableMap<Int, ArrayDeque<Char>> {
        val stacks = mutableMapOf<Int, ArrayDeque<Char>>()
        for (line in startingStack){
            line.windowed(3, 4, partialWindows = true).forEachIndexed() { index, value ->
                if (value.isNotBlank()) {
                    stacks[index]?.addLast(value[1]) ?: run {
                        stacks[index] = ArrayDeque(listOf(value[1]))
                    }
                }
            }
        }
        return stacks
    }
    fun part1(input: List<String>): String {
        var cursor = 0
        val startingStack = mutableListOf<String>()
        for (line in input) {
            cursor += 1
            if (line.contains("""\d""".toRegex())) break
            startingStack.add(line)
        }
        val stacks = buildStacks(startingStack)
        for (lineIndex in (cursor + 1)..input.lastIndex) {
            val lineParts = input[lineIndex].split(" ")
            val quantity = lineParts[1].toInt()
            val from = lineParts[3].toInt() - 1
            val to = lineParts[5].toInt() - 1
            stacks[from]?.let { charStack ->
                val chars = charStack.removeFirst(quantity)
                stacks[to]?.addFirst(chars)
            }
        }
        return buildString {
            stacks.entries.sortedBy { it.key }.forEach {
                append(it.value.first())
            }
        }
    }

    fun part2(input: List<String>): String {
        var cursor = 0
        val startingStack = mutableListOf<String>()
        for (line in input) {
            cursor += 1
            if (line.contains("""\d""".toRegex())) break
            startingStack.add(line)
        }
        val stacks = buildStacks(startingStack)
        for (lineIndex in (cursor + 1)..input.lastIndex) {
            val lineParts = input[lineIndex].split(" ")
            val quantity = lineParts[1].toInt()
            val from = lineParts[3].toInt() - 1
            val to = lineParts[5].toInt() - 1
            stacks[from]?.let { charStack ->
                val chars = charStack.removeFirst(quantity).asReversed()
                stacks[to]?.addFirst(chars)
            }
        }
        return buildString {
            stacks.entries.sortedBy { it.key }.forEach {
                append(it.value.first())
            }
        }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day05_test")
//    println(part2(testInput))

    val input = readInput("Day05")
//    println(part1(input))
    println(part2(input))
}

fun <T> ArrayDeque<T>.removeFirst(count: Int): List<T> {
    return buildList {
        for (i in 0 until count) {
            this@removeFirst.removeFirstOrNull()?.let { add(it) } ?: break
        }
    }
}
fun <T> ArrayDeque<T>.addFirst(entries: List<T>) {
    for (entry in entries) {
        addFirst(entry)
    }
}
