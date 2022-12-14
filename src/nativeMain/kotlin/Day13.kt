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
    fun String.parsePacketList(): PacketList {
        val lists = ArrayDeque<MutableList<PacketType>>().apply { add(mutableListOf()) }
        val reader = removeSurrounding("[", "]").reader()
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

    fun comparator(left: PacketType, right: PacketType): Int {
        if (left is PacketInt && right is PacketInt) return left.value.compareTo(right.value)
        if (left is PacketList && right is PacketList) {
            var pos = 0
            while (pos < left.list.size && pos < right.list.size) {
                val comparison = comparator(left.list[pos], right.list[pos])
                if (comparison != 0) return comparison
                pos += 1
            }
            return left.list.size.compareTo(right.list.size)
        }
        if (left is PacketList && right is PacketInt) return comparator(left, PacketList(listOf(right)))
        if (left is PacketInt && right is PacketList) return comparator(PacketList(listOf(left)), right)
        error("Invalid state")
    }

    fun part1(input: List<String>): Int {
        val iterator = input.listIterator()
        val comparisons = buildList<PacketType> {
            while (iterator.hasNext()) {
                add(iterator.next().parsePacketList(),)
                add(iterator.next().parsePacketList(),)
                if (iterator.hasNext()) {
                    iterator.next()
                }
            }
        }.windowed(2, 2).map { it[0] to it[1] }

        val rightOrder = mutableListOf<Int>()
        for (index in comparisons.indices) {
            if (comparator(comparisons[index].first, comparisons[index].second) == -1) {
                rightOrder.add(index + 1)
            }
        }

        return rightOrder.sum()
    }

    fun part2(input: List<String>): Int {
        val dividerPacket1 = "[[2]]".parsePacketList()
        val dividerPacket2 = "[[6]]".parsePacketList()
        val packets = input.filterNot { it.isEmpty() }
            .map { it.parsePacketList() }
            .plus(listOf(dividerPacket1, dividerPacket2))
            .sortedWith { packet1, packet2 ->
                comparator(packet1, packet2)
            }

        val packet1 = packets.indexOf(dividerPacket1)
        val packet2 = packets.indexOf(dividerPacket2)

        return (packet1 + 1) * (packet2 + 1)
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day13_test")
//    println(part1(testInput))

    val input = readInput("Day13")
//    println(part1(input)) // 5503
    println(part2(input)) // 20952
}

