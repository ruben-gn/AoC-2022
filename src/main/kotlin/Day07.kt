private val example = """
    ${'$'} cd /
    ${'$'} ls
    dir a
    14848514 b.txt
    8504156 c.dat
    dir d
    ${'$'} cd a
    ${'$'} ls
    dir e
    29116 f
    2557 g
    62596 h.lst
    ${'$'} cd e
    ${'$'} ls
    584 i
    ${'$'} cd ..
    ${'$'} cd ..
    ${'$'} cd d
    ${'$'} ls
    4060174 j
    8033020 d.log
    5626152 d.ext
    7214296 k
""".trimIndent()

//fun main() = Day07().run(example)
fun main() = Day07().run()

class Day07 : Day(7) {
    override fun part1(input: String): Any = createDirectoryTree(input.split("\n").toMutableList())
        .flatten()
        .map { dir -> dir.size }
        .filter { it <= 100000 }
        .sum()


    override fun part2(input: String): Any {
        val directories = createDirectoryTree(input.split("\n").toMutableList())
            .flatten()

        val toFree = directories.maxOf { it.size } - 40000000

        return directories.sortedBy { it.size }
            .first { it.size >= toFree }
            .size
    }


    private fun createDirectoryTree(commands: MutableList<String>): Directory {
        val directories = ArrayDeque<Directory>()
        directories.addLast(Directory("/", mutableListOf()))
        commands.removeFirst()
        while (commands.isNotEmpty()) {
            val line = commands.removeFirst()
            when {
                line == "$ cd .." -> {
                    val lastDir = directories.removeLast()
                    directories.last().children.add(lastDir)
                }

                line.startsWith("$ cd ") -> {
                    val dirName = line.substringAfter("$ cd ")
                    directories.addLast(Directory(dirName, mutableListOf()))
                }

                line.startsWith("dir") || line.startsWith("$ ls") -> Unit
                else -> {
                    val file = line.split(" ")
                    directories.last().children.add(File(file[1], file[0].toInt()))
                }
            }
        }

        while (directories.size > 1) {
            val child = directories.removeLast()
            directories.last().children.add(child)
        }
        return directories.removeLast()
    }
}

private sealed interface Node {
    val size: Int
}

private data class Directory(val name: String, val children: MutableList<Node>) : Node {
    override val size: Int
        get() = children.sumOf { it.size }

    fun flatten(): List<Directory> = children.filterIsInstance<Directory>().flatMap { it.flatten() } + this
}

private data class File(val name: String, override val size: Int) : Node