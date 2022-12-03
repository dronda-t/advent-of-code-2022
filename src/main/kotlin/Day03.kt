
fun main() {
    fun priority(char: Char): Int {
        var priority = (char.lowercaseChar() - 'a') + 1
        if (char.isUpperCase()) priority += 26
        return priority
    }
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val split = (line.length / 2)
            val compartment1 = line.substring(0, split).toHashSet()
            val compartment2 = line.substring(split, line.length).toHashSet()
            compartment1.retainAll(compartment2)

            compartment1.sumOf { priority(it) }
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf { window ->
            val commonLetter = HashSet<Char>(window[0].toSet())
            window.forEach { commonLetter.retainAll(it.toSet()) }
            priority(commonLetter.first())
        }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day03_test")
//    println(part2(testInput))

    val input = readInput("Day03")
//    println(part1(input))
    println(part2(input))
}