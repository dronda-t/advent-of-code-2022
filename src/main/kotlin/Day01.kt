import java.util.TreeSet

fun main() {
    fun part1(input: List<String>): Int {
        val calorieSums = mutableListOf<Int>()
        var currentSum = 0
        input.forEach {
            if (it.isNotBlank()) {
                currentSum += it.toInt()
            } else {
                calorieSums.add(currentSum)
                currentSum = 0
            }
        }
        return calorieSums.max()
    }

    fun part2(input: List<String>): Int {
        val calorieSums = TreeSet<Int>()
        var currentSum = 0
        input.forEach {
            if (it.isNotBlank()) {
                currentSum += it.toInt()
            } else {
                calorieSums.add(currentSum)
                currentSum = 0
            }
        }
        return listOf(
            calorieSums.pollLast()!!,
            calorieSums.pollLast()!!,
            calorieSums.pollLast()!!,
        ).sum()
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    println(part1(testInput))

    val input = readInput("Day01")
//    println(part1(input))
    println(part2(input))
}