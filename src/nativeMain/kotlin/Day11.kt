import platform.posix.exit

private data class Monkey(
    val items: ArrayDeque<Long>,
    val operation: (Long) -> Long,
    val test: Long,
    val ifTrue: Int,
    val ifFalse: Int,
    var itemInspections: ULong = 0uL
)
fun day11() {
    fun buildMonkey(iterator: ListIterator<String>): Monkey {
        val startingItems = iterator.next().substringAfter(": ").split(",").map { it.trim().toLong() }
        val (n1, operator, n2) = iterator.next().substringAfter("= ").split(" ")
        val operation: (Long) -> Long = { old ->
            val x = if (n1 == "old") old else n1.toLong()
            val y = if (n2 == "old") old else n2.toLong()
            if (operator == "+") {
                x.toLong() + y
            } else {
                x.toLong() * y
            }
        }
        val test = iterator.next().substringAfterLast(" ").toLong()
        val ifTrue = iterator.next().substringAfterLast(" ").toInt()
        val ifFalse = iterator.next().substringAfterLast(" ").toInt()
        return Monkey(
            items = ArrayDeque(startingItems),
            operation = operation,
            test = test,
            ifTrue = ifTrue,
            ifFalse = ifFalse
        )
    }

    fun part1(input: List<String>): ULong {
        val iterator = input.listIterator()
        val monkeys = mutableListOf<Monkey>()
        while (iterator.hasNext()) {
            val nextLine = iterator.next()
            if (nextLine.startsWith("Monkey")) {
                monkeys.add(buildMonkey(iterator))
            }
        }
        repeat(20) {
            for (monkey in monkeys) {
                while (monkey.items.isNotEmpty()) {
                    var level = monkey.items.removeFirst()
                    monkey.itemInspections += 1uL
                    level = monkey.operation(level) / 3
                    if (level % monkey.test == 0L) {
                        monkeys[monkey.ifTrue].items.addLast(level)
                    } else {
                        monkeys[monkey.ifFalse].items.addLast(level)
                    }
                }
            }
        }
        return monkeys.sortedByDescending { it.itemInspections }
            .take(2)
            .fold(1uL) { acc, monkey -> acc * monkey.itemInspections }
    }

    fun part2(input: List<String>): ULong {
        val iterator = input.listIterator()
        val monkeys = mutableListOf<Monkey>()
        while (iterator.hasNext()) {
            val nextLine = iterator.next()
            if (nextLine.startsWith("Monkey")) {
                monkeys.add(buildMonkey(iterator))
            }
        }
        val scale = monkeys.map { it.test }.reduce { acc, value -> acc * value }
        repeat(10000) {
            for (monkey in monkeys) {
                while (monkey.items.isNotEmpty()) {
                    var level = monkey.items.removeFirst()
                    monkey.itemInspections += 1uL
                    level = (monkey.operation(level) % scale)
                    if (level % monkey.test == 0L) {
                        monkeys[monkey.ifTrue].items.addLast(level)
                    } else {
                        monkeys[monkey.ifFalse].items.addLast(level)
                    }
                }
            }
        }
        return monkeys.sortedByDescending { it.itemInspections }
            .take(2)
            .fold(1uL) { acc, monkey -> acc * monkey.itemInspections }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day11_test")
//    println(part2(testInput))

    val input = readInput("Day11")
//    println(part1(input))
    println(part2(input)) // 32059801242
}