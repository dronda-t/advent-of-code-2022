private typealias HeightCheck = (newHeight: Int, currentHeight: Int) -> Boolean

fun day12() {
    data class Point(
        val row: Int,
        val column: Int,
    )

    fun height(
        point: Point,
        grid: List<List<Char>>
    ): Int {
        var char = grid[point.row][point.column]
        if (char == 'S') {
            char = 'a'
        } else if (char == 'E') {
            char = 'z'
        }
        return char - 'a'
    }

    fun findValidNeighbors(
        grid: List<List<Char>>,
        point: Point,
        visited: Set<Point>,
        heightCheck: HeightCheck,
    ): List<Point> {
        val currentHeight = height(point, grid)
        return buildList {
            fun tryAdd(newPoint: Point) {
                if (newPoint.row >= 0
                    && newPoint.column >= 0
                    && newPoint.row < grid.size
                    && newPoint.column < (grid.getOrNull(0)?.size ?: 0)
                    && newPoint !in visited
                    && heightCheck(height(newPoint, grid), currentHeight)
                ) {
                    add(newPoint)
                }
            }
            tryAdd(point.copy(row = point.row - 1))
            tryAdd(point.copy(row = point.row + 1))
            tryAdd(point.copy(column = point.column - 1))
            tryAdd(point.copy(column = point.column + 1))
        }
    }

    fun getCount(
        startPoint: Point,
        endPoint: Point,
        trail: Map<Point, Point>
    ): Int {
        var nextPoint = endPoint
        var count = 0
        while (nextPoint != startPoint) {
            count += 1
            nextPoint = trail[nextPoint] ?: error("Trail not complete")
        }
        return count
    }

    fun breadthFirstSearch(
        grid: List<List<Char>>,
        startingPoint: Point,
        endingChar: Char,
        heightCheck: HeightCheck,
    ): Int {
        val visited = hashSetOf<Point>()
        val toVisit = ArrayDeque<Point>()
        val trail = hashMapOf<Point, Point>()
        toVisit.addLast(startingPoint)

        while (toVisit.isNotEmpty()) {
            val check = toVisit.removeFirst()

            if (grid[check.row][check.column] == endingChar) {
                return getCount(startingPoint, check, trail)
            } else {
                findValidNeighbors(grid, check, visited, heightCheck).forEach { neighbor ->
                    visited.add(neighbor)
                    toVisit.addLast(neighbor)
                    trail[neighbor] = check
                }
            }
        }
        error("Should've returned already")
    }

    fun part1(input: List<String>): Int {
        val grid = mutableListOf<MutableList<Char>>()
        var start = Point(0, 0)
        input.forEachIndexed { index, row ->
            row.indexOf('S').takeIf { it >= 0 }?.let {
                start = start.copy(row = index, column = it)
            }
            grid.add(row.toMutableList())
        }

        return breadthFirstSearch(
            grid = grid,
            startingPoint = start,
            endingChar = 'E',
            heightCheck = { newHeight, currentHeight -> newHeight - 1 <= currentHeight }
        )
    }

    fun part2(input: List<String>): Int {
        val grid = mutableListOf<MutableList<Char>>()
        var start = Point(0, 0)
        input.forEachIndexed { index, row ->
            row.indexOf('E').takeIf { it >= 0 }?.let {
                start = start.copy(row = index, column = it)
            }
            grid.add(row.toMutableList())
        }


        return breadthFirstSearch(
            grid = grid,
            startingPoint = start,
            endingChar = 'a',
            heightCheck = { newHeight, currentHeight -> newHeight + 1 >= currentHeight }
        )
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day12_test")
//    println(part2(testInput))

    val input = readInput("Day12")
//    println(part1(input)) // 440
    println(part2(input)) // 439
}