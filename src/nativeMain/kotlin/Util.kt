import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

actual fun readInput(name: String): List<String> {
    return buildList {
        FileSystem.SYSTEM.source("inputs/$name.txt".toPath()).use { fileSource ->
            fileSource.buffer().use { bufferedSource ->
                while (true) {
                    val line = bufferedSource.readUtf8Line() ?: break
                    add(line)
                }
            }
        }
    }
}

actual fun readInputRaw(name: String): String {
    return buildString {
        FileSystem.SYSTEM.source("inputs/$name.txt".toPath()).use { fileSource ->
            fileSource.buffer().use { bufferedSource ->
                while (true) {
                    val line = bufferedSource.readUtf8Line() ?: break
                    append(line)
                }
            }
        }
    }
}
