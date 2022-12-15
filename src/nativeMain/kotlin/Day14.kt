import platform.posix.err

fun day14() {
    data class Point(
        val x: Int,
        val y: Int,
    )
    fun buildPath(particles: MutableSet<Point>, from: Point, to: Point) {
        val xRange = IntProgression.fromClosedRange(from.x, to.x, if (to.x - from.x >= 0) 1 else -1)
        val yRange = IntProgression.fromClosedRange(from.y, to.y, if (to.y - from.y >= 0) 1 else -1)
        for (x in xRange) {
            for (y in yRange) {
                particles.add(Point(x, y))
            }
        }
    }
    fun dropSand(particles: Set<Point>, startingPoint: Point, lowestPoint: Int): Point {
        var point = startingPoint
        while (true) {
            when {
                point.y > lowestPoint -> break
                point.copy(y = point.y + 1) !in particles -> {
                    point = point.copy(y = point.y + 1)
                }
                point.copy(x = point.x - 1, y = point.y + 1) !in particles -> {
                    point = point.copy(x = point.x - 1, y = point.y + 1)
                }
                point.copy(x = point.x + 1, y = point.y + 1) !in particles -> {
                    point = point.copy(x = point.x + 1, y = point.y + 1)
                }
                else -> break
            }
        }
        return point
    }
    fun part1(input: List<String>): Int {
        var lowest = 0
        val particles = hashSetOf<Point>()
        input.forEach {
            it.split(" -> ")
                .map { stringPair ->
                    val (x, y) = stringPair.split(",").let { (x, y) -> x.toInt() to y.toInt() }
                    if (y > lowest) lowest = y
                    Point(x, y)
                }
                .zipWithNext()
                .forEach { (pair1, pair2) ->
                    buildPath(particles, pair1, pair2)
                }
        }

        var iterations = 0
        while (true) {
            iterations += 1
            val point = dropSand(
                particles = particles,
                startingPoint = Point(500, 0),
                lowestPoint = lowest
            )
            particles.add(point)
            if (point.y > lowest) break
        }
        return iterations - 1
    }

    fun part2(input: List<String>): Int {
        var lowest = 0
        val particles = hashSetOf<Point>()
        input.forEach {
            it.split(" -> ")
                .map { stringPair ->
                    val (x, y) = stringPair.split(",").let { (x, y) -> x.toInt() to y.toInt() }
                    if (y > lowest) lowest = y
                    Point(x, y)
                }
                .zipWithNext()
                .forEach { (pair1, pair2) ->
                    buildPath(particles, pair1, pair2)
                }
        }

        var iterations = 0
        val startingPoint = Point(500, 0)
        while (true) {
            iterations += 1
            val point = dropSand(
                particles = particles,
                startingPoint = startingPoint,
                lowestPoint = lowest
            )
            particles.add(point)
            if (particles.contains(startingPoint)) break
        }
        return iterations
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day14_test")
//    println(part2(testInput))

    val input = readInput("Day14")
//    println(part1(input))
    println(part2(input))
}