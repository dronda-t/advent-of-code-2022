
fun day06() {
    fun part1(input: List<String>): Int {
        input.first().windowed(4).forEachIndexed { index, s ->
            if (s.toHashSet().size == 4) return index + 4
        }
        error("")
    }

    fun part2(input: List<String>): Int {
        input.first().windowed(14).forEachIndexed { index, s ->
            if (s.toHashSet().size == 14) return index + 14
        }
        error("")
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day06_test")
//    println(part2(testInput))

    val input = readInput("Day06")
//    println(part1(input))
    println(part2(input))
}