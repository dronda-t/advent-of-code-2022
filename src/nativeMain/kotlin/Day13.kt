private interface PacketType
private value class PacketInt(val value: Int) : PacketType
private value class PacketList(val list: List<PacketType>) : PacketType

interface StringReader: Iterator<Char> {
    override fun next(): Char = nextChar()

    fun nextChar(): Char

    fun peekNextOrNull(): Char?
}
fun CharSequence.reader() = object : StringReader {
    private var index = 0

    override fun nextChar(): Char = get(index++)

    override fun hasNext(): Boolean = index < length

    override fun peekNextOrNull(): Char? = getOrNull(index)
}

fun day13() {
    data class Comparison(
        val left: PacketList,
        val right: PacketList
    )
    fun String.parsePacketList(): PacketList {
        val lists = ArrayDeque<MutableList<PacketType>>().apply { add(mutableListOf()) }
        val reader = reader()
        while (reader.hasNext()) {
            when (val char = reader.next()) {
                '[' -> lists.addLast(mutableListOf())
                ']' -> {
                    val completedList = lists.removeLast()
                    lists.last().add(
                        PacketList(completedList)
                    )
                }
                ',' -> { /* noop */ }
                else -> {
                    var digit = char.toString()
                    while (reader.peekNextOrNull()?.isDigit() == true) {
                        digit = "${digit}${reader.next()}"
                    }
                    lists.last().add(
                        PacketInt(digit.toInt())
                    )
                }
            }
        }
        return PacketList(lists.last())
    }
    fun String.stripEnds() = removeSurrounding("[", "]")

    fun runComparison(comparison: Comparison): ComparisonResult {
        var pos = 0
        // sooo gross
        while (true) {
            val left = comparison.left.list.getOrNull(pos)
            val right = comparison.right.list.getOrNull(pos)

            if (left == null && right == null) return ComparisonResult.NoAction
            if (comparison.left.list.size != comparison.right.list.size ) {
                if (left == null) return ComparisonResult.CorrectOrder
                if (right == null) return ComparisonResult.WrongOrder
            }

            if (left is PacketInt && right is PacketInt) {
                if (left.value < right.value) return ComparisonResult.CorrectOrder
                if (left.value > right.value) return ComparisonResult.WrongOrder
            } else if (left is PacketInt && right is PacketList) {
                runComparison(Comparison(PacketList(listOf(left)), right)).onTakeAction {
                    return it
                }
            } else if (left is PacketList && right is PacketInt) {
                runComparison(Comparison(left, PacketList(listOf(right)))).onTakeAction {
                    return it
                }
            } else if (left is PacketList && right is PacketList) {
                runComparison(Comparison(left, right)).onTakeAction { return it }
            }
            pos += 1
        }
    }

    fun part1(input: List<String>): Int {
        val iterator = input.listIterator()
        val comparisons = buildList {
            while (iterator.hasNext()) {
                val comparison = Comparison(
                    iterator.next().stripEnds().parsePacketList(),
                    iterator.next().stripEnds().parsePacketList(),
                )
                add(comparison)
                if (iterator.hasNext()) {
                    iterator.next()
                }
            }
        }

        val rightOrder = mutableListOf<Int>()
        for (index in comparisons.indices) {
            if (runComparison(comparisons[index]) == ComparisonResult.CorrectOrder) {
                rightOrder.add(index + 1)
            }
        }

        return rightOrder.sum()
    }

    fun part2(input: List<String>): Int {
        val packets = input.let {it + listOf("[[2]]", "[[6]]") }
            .filterNot { it.isEmpty() }
            .map { it.stripEnds().parsePacketList() }

        val sortedPackets = packets.sortedWith { packet1, packet2 ->
            when (runComparison(Comparison(packet1, packet2))) {
                ComparisonResult.CorrectOrder,
                ComparisonResult.NoAction -> -1
                ComparisonResult.WrongOrder -> 1
            }
        }
        val packet1 = sortedPackets.indexOf(PacketList(listOf(PacketList(listOf(PacketInt(2))))))
        val packet2 = sortedPackets.indexOf(PacketList(listOf(PacketList(listOf(PacketInt(6))))))

        return (packet1 + 1) * (packet2 + 1)
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day13_test")
//    println(part2(testInput))

    val input = readInput("Day13")
//    println(part1(input)) // 5503
    println(part2(input))
}

sealed interface ComparisonResult {
    object NoAction : ComparisonResult
    object CorrectOrder : ComparisonResult
    object WrongOrder : ComparisonResult

}
inline fun ComparisonResult.onTakeAction(body: (action: ComparisonResult) -> Unit) {
    if (this !is ComparisonResult.NoAction) {
        body(this)
    }
}

