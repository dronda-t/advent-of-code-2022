
sealed class Item(val score: Int) : Comparable<Item> {
    companion object {
        fun lookupItem(itemString: String) = when (itemString) {
            "A",
            "X" -> Rock
            "B",
            "Y" -> Paper
            "C",
            "Z" -> Scissors
            else -> error("Invalid item")
        }
    }
    override fun compareTo(other: Item): Int {
        return when (this) {
            is Rock -> {
                when (other) {
                    is Rock -> 0
                    is Paper -> -1
                    is Scissors -> 1
                }
            }
            is Paper -> {
                when (other) {
                    is Rock -> 1
                    is Paper -> 0
                    is Scissors -> -1
                }
            }
            is Scissors -> {
                when (other) {
                    is Rock -> -1
                    is Paper -> 1
                    is Scissors -> 0
                }
            }
        }
    }
    object Rock : Item(1)
    object Paper : Item(2)
    object Scissors : Item(3)
}

sealed class Result(val score: Int) {
    object Win : Result(6)
    object Draw : Result(3)
    object Lose : Result(0)

    companion object {
        fun lookupResult(result: String) = when (result) {
            "X" -> Lose
            "Y" -> Draw
            "Z" -> Win
            else -> error("Invalid result")
        }
    }
}
fun main() {
    fun part1(input: List<String>): Int {
        var score = 0
        input.forEach {
            val splitStr = it.split(" ")
            val opponent = Item.lookupItem(splitStr[0])
            val you = Item.lookupItem(splitStr[1])

            score += if (you > opponent) {
                Result.Win.score + you.score
            } else if (you == opponent) {
                Result.Draw.score + you.score
            } else {
                Result.Lose.score + you.score
            }
        }

        return score
    }

    val combinations = hashMapOf<Pair<Item, Result>, Item>()
    fun determineResult(opponentItem: Item, result: Result): Item {
        val items = listOf(Item.Rock, Item.Paper, Item.Scissors)
        return combinations.computeIfAbsent(Pair(opponentItem, result)) { (opponentItem, result) ->
            when (result) {
                is Result.Draw -> opponentItem
                is Result.Win -> items.first { it > opponentItem }
                is Result.Lose -> items.first { it < opponentItem }
            }
        }
    }
    fun part2(input: List<String>): Int {
        var score = 0
        input.forEach {
            val splitStr = it.split(" ")
            val opponent = Item.lookupItem(splitStr[0])
            val result = Result.lookupResult(splitStr[1])
            val you = determineResult(opponent, result)
            score += you.score + result.score
        }
        return score
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day02_test")
//    println(part2(testInput))

    val input = readInput("Day02")
//    println(part1(input))
    println(part2(input))
}