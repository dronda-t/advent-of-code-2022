
fun day04() {
    fun part1(input: List<String>): Int {
        return input.map { it.split(",") }.map { (assignment1, assignment2) ->
            val range1 = assignment1.split("-")
                .map { it.toInt() }
                .let { (part1, part2) -> part1..part2 }
            val range2 = assignment2.split("-")
                .map { it.toInt() }
                .let { (part1, part2) -> part1..part2 }
            if (range1.all { it in range2 } || range2.all { it in range1 }) 1 else 0
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map { it.split(",") }.map { (assignment1, assignment2) ->
            val range1 = assignment1.split("-")
                .map { it.toInt() }
                .let { (part1, part2) -> part1..part2 }
            val range2 = assignment2.split("-")
                .map { it.toInt() }
                .let { (part1, part2) -> part1..part2 }
            if (range1.any { it in range2 }) 1 else 0
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day04_test")
//    println(part2(testInput))

    val input = readInput("Day04")
    println(part1(input)) // 534
    println(part2(input)) // 841
}