
fun day10() {
    fun part1(input: List<String>): Int {
        val iterator = input.iterator()
        val instructions = ArrayDeque<Int>(1)
        var x = 1
        var cycle = 1
        var signalStrengths = 0
        while (iterator.hasNext() || instructions.isNotEmpty()) {
            if ((cycle - 20) % 40 == 0) {
                signalStrengths += cycle * x
            }
            val instruction = instructions.removeLastOrNull()
            if (instruction != null) {
                x += instruction
            } else {
                val nextInstruction = iterator.next()
                if (nextInstruction.startsWith("addx")) {
                    instructions.addLast(nextInstruction.split(" ").last().toInt())
                }
            }
            cycle += 1
        }
        return signalStrengths
    }

    fun part2(input: List<String>) {
        val iterator = input.iterator()
        val instructions = ArrayDeque<Int>(1)
        var x = 1
        var cycle = 1
        fun range() = (x - 1).coerceAtLeast(0)..(x + 1).coerceAtMost(40)
        var signalStrengths = 0
        val display = mutableListOf<MutableList<Char>>()
        while (iterator.hasNext() || instructions.isNotEmpty()) {
            if (cycle % 40 == 1) display.add(mutableListOf())
            if (display.last().size in range()) {
                display.last().add('#')
            } else {
                display.last().add('.')
            }

            val instruction = instructions.removeLastOrNull()
            if (instruction != null) {
                x += instruction
            } else {
                val nextInstruction = iterator.next()
                if (nextInstruction.startsWith("addx")) {
                    instructions.addLast(nextInstruction.split(" ").last().toInt())
                }
            }
            cycle += 1
        }
        display.forEach { row ->
            println(row.joinToString(" "))
        }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day10_test")
//    println(part2(testInput))

    val input = readInput("Day10")
//    println(part1(input))
    println(part2(input))
}