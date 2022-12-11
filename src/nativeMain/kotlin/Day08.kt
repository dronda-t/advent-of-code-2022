
/*
30373
25512
65332
33549
35390
 */
private typealias Grid = List<List<Int>>
fun day08() {
    fun isOverEdge(row: Int, column: Int, grid: List<List<Int>>): Boolean {
        return when {
            row < 0 ||
            row > grid.lastIndex ||
            column < 0 ||
            column > grid[row].lastIndex -> true
            else -> false
        }
    }

    fun viewScoreOneDirection(
        rowColumn: Pair<Int, Int>,
        treeValue: Int,
        grid: Grid,
        transform: (Pair<Int, Int>) -> Pair<Int, Int>
    ): Int {
        if (isOverEdge(rowColumn.first, rowColumn.second, grid)) return 0
        val (row, column) = rowColumn
        return if (treeValue > grid[row][column]) {
            1 + viewScoreOneDirection(transform(rowColumn), treeValue, grid, transform)
        } else {
            1
        }
    }

    fun calcViewScore(row: Int, column: Int, treeValue: Int, grid: List<List<Int>>): Int {
        var viewScore = viewScoreOneDirection(Pair(row - 1, column), treeValue, grid) {
            it.copy(first = it.first - 1)
        }
        viewScore *= viewScoreOneDirection(Pair(row + 1, column), treeValue, grid) {
            it.copy(first = it.first + 1)
        }
        viewScore *= viewScoreOneDirection(Pair(row, column - 1), treeValue, grid) {
            it.copy(second = it.second - 1)
        }
        viewScore *= viewScoreOneDirection(Pair(row, column + 1), treeValue, grid) {
            it.copy(second = it.second + 1)
        }
        return viewScore
    }

    fun part2(input: List<String>): Int {
        val grid = mutableListOf<MutableList<Int>>()
        input.forEach { y ->
            val row = mutableListOf<Int>()
            y.forEach { x ->
                row.add(x.digitToInt())
            }
            grid.add(row)
        }

        return input.flatMapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, _ ->
                calcViewScore(rowIndex, columnIndex, grid[rowIndex][columnIndex], grid)
            }
        }.max()
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day08_test")
//    println(part2(testInput))

    val input = readInput("Day08")
//    println(part1(input)) // 1533
    println(part2(input))
}