import kotlin.math.absoluteValue

// jvm is so much faster on this one
fun day15() {
    data class Point(val x: Int, val y: Int)
    data class SensorInfo(
        val x: Int,
        val y: Int,
        val beaconX: Int,
        val beaconY: Int,
    ) {
        fun determineCross(): Int {
            val xDistance = (x - beaconX).absoluteValue
            val yDistance = (y - beaconY).absoluteValue
            val xCross = (xDistance * 2) + (2 * yDistance)
            val yCross = (yDistance * 2) + (2 * xDistance)
            require(xCross == yCross)
            return xCross
        }
    }


    val inputRegex = """x=(-?\d*), y=(-?\d*):.*x=(-?\d*), y=(-?\d*)""".toRegex()
    fun part1(input: List<String>, rowToCheck: Int): Int {
        // parse input
        val beacons = hashSetOf<Point>()
        val sensorInfoInRow = hashSetOf<SensorInfo>()
        val info = input.flatMap { inputRegex.findAll(it) }
            .map { match ->
                SensorInfo(
                    match.groupValues[1].toInt(),
                    match.groupValues[2].toInt(),
                    match.groupValues[3].toInt(),
                    match.groupValues[4].toInt(),
                )
            }.onEach { info ->
                beacons.add(Point(info.beaconX, info.beaconY))
            }
        // build rings
        info.forEach {
            val crossSize = it.determineCross()
            val yRange = (it.y - (crossSize / 2))..(it.y + (crossSize / 2))
            if (rowToCheck in yRange) {
                sensorInfoInRow.add(it)
            }
        }
        var notBeacon = sensorInfoInRow.fold(setOf<Int>()) { acc, sensorInfo ->
            val crossSize = sensorInfo.determineCross()
            val xSize = crossSize - ((sensorInfo.y - rowToCheck).absoluteValue * 2)
            acc union ((sensorInfo.x - (xSize / 2))..(sensorInfo.x + (xSize / 2)))
        }
        notBeacon = notBeacon.minus(beacons.filter { it.y == rowToCheck }.map { it.x }.toSet())
        // check row
        return notBeacon.size
    }

    fun IntRange.overlap(other: IntRange): Boolean {
        val a = other.first in this
        val b = other.last in this
        val c = first in other
        val d = last in other
        return a || b || c || d
    }

    fun MutableList<IntRange>.addRange(newRange: IntRange) {
        val ranges = filter { it.overlap(newRange) }.plusElement(newRange)
        if (ranges.drop(1).isNotEmpty()) {
            val max = ranges.maxOf { it.last }
            val min = ranges.minOf { it.first }
            this.removeAll(ranges)
            add(min..max)
        } else {
            add(newRange)
        }
    }

    fun part2(input: List<String>, lowerBound: Int, upperBound: Int): Long {
        val beacons = hashSetOf<Point>()
        val info = input.flatMap { inputRegex.findAll(it) }
            .map { match ->
                SensorInfo(
                    match.groupValues[1].toInt(),
                    match.groupValues[2].toInt(),
                    match.groupValues[3].toInt(),
                    match.groupValues[4].toInt(),
                )
            }.onEach { info ->
                beacons.add(Point(info.beaconX, info.beaconY))
            }

        for (y in lowerBound..upperBound) {
            val ranges = mutableListOf<IntRange>()
            for (sensorInfo in info) {
                val crossSize = sensorInfo.determineCross()
                val xSize = (crossSize - ((sensorInfo.y - y).absoluteValue * 2)).coerceAtLeast(0)
                if (xSize == 0) continue
                val newRange = (((sensorInfo.x - (xSize / 2)).coerceAtLeast(lowerBound))..((sensorInfo.x + (xSize / 2)).coerceAtMost(upperBound)))
                ranges.addRange(newRange)
            }

            ranges.sortBy { it.first }
            ranges.fold(lowerBound) { prev, range ->
                for (x in prev until range.first) return (x.toLong() * 4000000) + y
                range.last + 1
            }
            for (x in ranges.last().last until upperBound) return x.toLong() * 4000000 + y
        }
        error("should've returned")
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day15_test")
//    println(part1(testInput, 10))
//    println(part2(testInput, 0, 20))

    val input = readInput("Day15")
//    println(part1(input, 2000000))
    println(part2(input, 0, 4000000))
}