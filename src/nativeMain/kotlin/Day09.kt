import kotlin.math.absoluteValue

private data class Point(
    var x: Int,
    var y: Int
)
fun day09() {

    fun part1(input: List<String>): Int {
        fun moveTail(head: Point, tail: Point) {
            val xDistance = (head.x - tail.x)
            val yDistance = (head.y - tail.y)
            // if both within 1 do nothing
            // if one is 0 and the other is 2 move in the direction of 2
            // if one is 1 and the other is 2 move diagonally
            when {
                xDistance.absoluteValue < 1 && yDistance.absoluteValue < 1 -> { /* do nothing */ }
                xDistance == 0 && yDistance.absoluteValue >= 2 -> {
                    tail.y = if (yDistance > 0) head.y - 1 else head.y + 1
                }
                yDistance == 0 && xDistance.absoluteValue >= 2 -> {
                    tail.x = if (xDistance > 0) head.x - 1 else head.x + 1
                }
                xDistance.absoluteValue == 1 && yDistance.absoluteValue >= 2 -> {
                    tail.x = head.x
                    tail.y = if (yDistance > 0) head.y - 1 else head.y + 1
                }
                yDistance.absoluteValue == 1 && xDistance.absoluteValue >= 2 -> {
                    tail.y = head.y
                    tail.x = if (xDistance > 0) head.x - 1 else head.x + 1
                }
            }
        }
        fun movePoints(head: Point, tail: Point, visited: MutableSet<Point>, count: Int, modify: (Point) -> Unit) {
            for (i in 0 until count) {
                modify(head)
                moveTail(head, tail)
                visited.add(tail.copy()) // Copy is fix for kotlin-native hashcode implementation?
            }
        }

        val visited = hashSetOf(Point(0, 0))
        val head = Point(0, 0)
        val tail = Point(0, 0)
        for (instruction in input) {
            val (direction, countStr) = instruction.split(" ")
            val count = countStr.toInt()
            when (direction) {
                "U" -> movePoints(head, tail, visited, count) { it.y += 1 }
                "R" -> movePoints(head, tail, visited, count) { it.x += 1 }
                "D" -> movePoints(head, tail, visited, count) { it.y -= 1 }
                "L" -> movePoints(head, tail, visited, count) { it.x -= 1 }
            }
        }
        return visited.size
    }

    fun part2(input: List<String>): Int {
        fun moveTail(head: Point, tail: Point) {
            val xDistance = (head.x - tail.x)
            val yDistance = (head.y - tail.y)
            // if both within 1 do nothing
            // if one is 0 and the other is 2 move in the direction of 2
            // if one is 1 and the other is 2 move diagonally
            when {
                xDistance.absoluteValue < 1 && yDistance.absoluteValue < 1 -> { /* do nothing */ }
                xDistance == 0 && yDistance.absoluteValue >= 2 -> {
                    tail.y = if (yDistance > 0) head.y - 1 else head.y + 1
                }
                yDistance == 0 && xDistance.absoluteValue >= 2 -> {
                    tail.x = if (xDistance > 0) head.x - 1 else head.x + 1
                }
                xDistance.absoluteValue == 1 && yDistance.absoluteValue >= 2 -> {
                    tail.x = head.x
                    tail.y = if (yDistance > 0) head.y - 1 else head.y + 1
                }
                yDistance.absoluteValue == 1 && xDistance.absoluteValue >= 2 -> {
                    tail.y = head.y
                    tail.x = if (xDistance > 0) head.x - 1 else head.x + 1
                }
                xDistance.absoluteValue >= 2 && yDistance.absoluteValue >= 2 -> {
                    tail.x = if (xDistance > 0) head.x - 1 else head.x + 1
                    tail.y = if (yDistance > 0) head.y - 1 else head.y + 1
                }
            }
        }

        fun movePoints(knots: List<Point>, visited: MutableSet<Point>, count: Int, modify: (Point) -> Unit) {
            repeat(count) {
                modify(knots.first())
                for (index in 1..knots.lastIndex) {
                    val firstKnot = knots[index - 1]
                    val secondKnot = knots[index]
                    moveTail(firstKnot, secondKnot)
                }
                visited.add(knots.last().copy())
            }
        }

        val knots = List(10) { Point(0, 0) }
        val visited = hashSetOf(Point(0, 0))

        for (instruction in input) {
            val (direction, countStr) = instruction.split(" ")
            val count = countStr.toInt()
            when (direction) {
                "U" -> movePoints(knots, visited, count) { it.y += 1 }
                "R" -> movePoints(knots, visited, count) { it.x += 1 }
                "D" -> movePoints(knots, visited, count) { it.y -= 1 }
                "L" -> movePoints(knots, visited, count) { it.x -= 1 }
            }
        }

        return visited.size
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day09_test")
//    println(part2(testInput))

    val input = readInput("Day09")
//    println(part1(input)) // 5883
    println(part2(input))
}