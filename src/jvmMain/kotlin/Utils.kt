
/**
 * Reads lines from the given input txt file.
 */
actual fun readInput(name: String) = java.io.File("inputs/$name.txt")
    .reader()
    .readLines()

actual fun readInputRaw(name: String) = java.io.File("inputs/$name.txt")
    .reader()
    .readText()
