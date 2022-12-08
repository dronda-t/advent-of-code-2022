import java.util.Scanner


const val TOTAL_FILESYSTEM_SIZE = 70000000
const val FILE_SIZE_TO_INSERT = 30000000
interface FileSystemItem

data class File(
    val name: String,
    val size: Int,
    val parent: Directory? = null,
) : FileSystemItem
class Directory(
    val name: String,
    val items: MutableSet<FileSystemItem>,
    val parent: Directory? = null
) : FileSystemItem {

    fun addDirectory(name: String) {
        items.add(
            Directory(
                name = name,
                items = mutableSetOf(),
                parent = this
            )
        )
    }
    fun addFile(name: String, size: Int) {
        items.add(File(
            name = name,
            size = size,
            parent = this
        ))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Directory

        if (name != other.name) return false
        if (parent != other.parent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Directory(name='$name')"
    }
}
fun main() {
    fun determineSizes(directory: Directory): HashMap<Directory, Int> {
        val stack = ArrayDeque<Directory>()
        val directoryToSize = hashMapOf<Directory, Int>()
        stack.add(directory)
        while (stack.isNotEmpty()) {
            val directory = stack.removeLast()
            var size = 0
            for (item in directory.items) {
                when (item) {
                    is File -> size += item.size
                    is Directory -> when(val itemSize = directoryToSize[item]) {
                        null -> {
                            stack.addLast(directory) // add back to the stack
                            stack.addLast(item)
                            continue
                        }
                        else -> size += itemSize
                    }
                }
            }
            directoryToSize[directory] = size
        }
        return directoryToSize
    }

    val topLevelDir = Directory(
        name = "/",
        items = mutableSetOf(),
        parent = null
    )
    var currentDirectory = topLevelDir
    fun runChangeDirectory(directory: String) {
        when (directory) {
            ".." -> currentDirectory.parent?.let { currentDirectory = it }
            "/" -> currentDirectory = topLevelDir
            else -> {
                currentDirectory.items.asSequence()
                    .filterIsInstance<Directory>()
                    .find { (it as? Directory)?.name == directory }
                    ?.let { directoryToChangeTo ->
                        currentDirectory = directoryToChangeTo
                    }
            }
        }
    }
    fun runCaptureLs(first: String, second: String) {
        when (first) {
            "dir" -> currentDirectory.addDirectory(second)
            else -> currentDirectory.addFile(second, first.toInt())
        }

    }
    fun runCommand(command: String, scanner: Scanner) {
        when (command) {
            "cd" -> {
                val directory = scanner.next()
                runChangeDirectory(directory)
            }
            "ls" -> {
                while (scanner.hasNext()) {
                    when (val next = scanner.next()) {
                        "$" -> runCommand(scanner.next(), scanner)
                        else -> runCaptureLs(next, scanner.next())
                    }
                }
            }
            else -> error("Unrecognized")
        }
    }
    fun part1(input: String): Int {
        val scanner = Scanner(input)
        while (scanner.hasNext()) {
            when (scanner.next()) {
                "$" -> runCommand(scanner.next(), scanner)
            }
        }

        return determineSizes(topLevelDir).entries.filter { it.value <= 100000 }.sumOf { it.value }
    }

    fun part2(input: String): Int {
        val scanner = Scanner(input)
        while (scanner.hasNext()) {
            when (scanner.next()) {
                "$" -> runCommand(scanner.next(), scanner)
            }
        }

        val sizes = determineSizes(topLevelDir)
        val topLevelSize = sizes[topLevelDir]!!
        val freeSpace = (TOTAL_FILESYSTEM_SIZE - topLevelSize).coerceAtLeast(0)
        val spaceNeeded = (FILE_SIZE_TO_INSERT - freeSpace).coerceAtLeast(0)

        return sizes.entries.filter { it.value > spaceNeeded }.minOf { it.value }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInputRaw("Day07_test")
//    println(part2(testInput))

    val input = readInputRaw("Day07")
//    println(part1(input))
    println(part2(input))
}