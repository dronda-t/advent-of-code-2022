
/*
30373
25512
65332
33549
35390
 */
private typealias Grid = List<List<Int>>
fun main() {
    fun isOnEdge(row: Int, column: Int, grid: List<List<Int>>): Boolean {
        return when {
            row - 1 < 0 ||
            row + 1 > grid.lastIndex ||
            column - 1 < 0 ||
            column + 1 > grid[row].lastIndex -> true
            else -> false
        }
    }

    fun isVisibleOneDirection(
        rowColumn: Pair<Int, Int>,
        treeValue: Int,
        grid: Grid,
        transform: (Pair<Int, Int>) -> Pair<Int, Int>
    ): Boolean {
        val (row, column) = rowColumn
        return if (treeValue > grid[row][column]) {
            if (isOnEdge(rowColumn.first, rowColumn.second, grid)) return true
            isVisibleOneDirection(transform(rowColumn), treeValue, grid, transform)
        } else {
            false
        }
    }

    fun isVisible(row: Int, column: Int, treeValue: Int, grid: List<List<Int>>): Boolean {
        if (treeValue > grid[row - 1][column] && isVisibleOneDirection(Pair(row - 1, column), treeValue, grid) {
                it.copy(first = it.first - 1)
            }) {
            return true
        }
        if (treeValue > grid[row + 1][column] && isVisibleOneDirection(Pair(row + 1, column), treeValue, grid) {
                it.copy(first = it.first + 1)
            }) {
            return true
        }
        if (treeValue > grid[row][column - 1] && isVisibleOneDirection(Pair(row, column - 1), treeValue, grid) {
                it.copy(second = it.second - 1)
            }) {
            return true
        }
        if (treeValue > grid[row][column + 1] && isVisibleOneDirection(Pair(row, column + 1), treeValue, grid) {
                it.copy(second = it.second + 1)
            }) {
            return true
        }
        return false
    }

    fun part1(input: List<String>): Int {
        val grid = mutableListOf<MutableList<Int>>()
        input.forEach { y ->
            val row = mutableListOf<Int>()
            y.forEach { x ->
                row.add(x.digitToInt())
            }
            grid.add(row)
        }

        var totalVisible = 0
        for (rowIndex in input.indices) {
            for (columnIndex in input[rowIndex].indices) {
                when {
                    isOnEdge(rowIndex, columnIndex, grid) ||
                    isVisible(rowIndex, columnIndex, grid[rowIndex][columnIndex], grid) -> totalVisible += 1
                    else -> { /* noop */ }
                }
            }
        }

        return totalVisible
    }

    fun part2(input: List<String>): Int {
        return 1
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day08_test")
//    println(part1(testInput))

    val input = readInput("Day08")
    println(part1(input)) // 1533
//    println(part2(input))
}