import kotlin.math.abs

fun main() = Day10().run()
//fun main() = Day10().run(example)

class Day10 : Day(10) {

    override fun part1(input: String): Any =
        input.toRegisterValues()
            .drop(19)
            .chunked(40)
            .mapIndexed { index, nums -> nums.first() * (index * 40 + 20) }
            .sum()

    override fun part2(input: String): Any =
        input.toRegisterValues()
            .chunked(40)
            .map { it.mapIndexed {index, num  -> toPixel(num, index) }.joinToString("") }
            .forEach(::println)

    private fun toPixel(num: Int, index: Int) = if (abs(num - index) <= 1) "██" else "░░"

    private fun String.toRegisterValues() =
        this.split("\n").map(::command)
            .runningFold(listOf(1)) { acc, command -> command.apply(acc.last()) }
            .flatten()

    private fun Pair<String, Int>.apply(num: Int) =
        when (this.first) {
            "addx" -> listOf(num, num + this.second)
            else -> listOf(num)
        }

    private fun command(command: String): Pair<String, Int> {
        val split = command.split(" ")
        return split[0] to if (split.size == 2) split[1].toInt() else 0
    }
}

private val example1 = """ 
    noop
    addx 3
    addx -5
""".trimIndent()
private val example = """
    addx 15
    addx -11
    addx 6
    addx -3
    addx 5
    addx -1
    addx -8
    addx 13
    addx 4
    noop
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx -35
    addx 1
    addx 24
    addx -19
    addx 1
    addx 16
    addx -11
    noop
    noop
    addx 21
    addx -15
    noop
    noop
    addx -3
    addx 9
    addx 1
    addx -3
    addx 8
    addx 1
    addx 5
    noop
    noop
    noop
    noop
    noop
    addx -36
    noop
    addx 1
    addx 7
    noop
    noop
    noop
    addx 2
    addx 6
    noop
    noop
    noop
    noop
    noop
    addx 1
    noop
    noop
    addx 7
    addx 1
    noop
    addx -13
    addx 13
    addx 7
    noop
    addx 1
    addx -33
    noop
    noop
    noop
    addx 2
    noop
    noop
    noop
    addx 8
    noop
    addx -1
    addx 2
    addx 1
    noop
    addx 17
    addx -9
    addx 1
    addx 1
    addx -3
    addx 11
    noop
    noop
    addx 1
    noop
    addx 1
    noop
    noop
    addx -13
    addx -19
    addx 1
    addx 3
    addx 26
    addx -30
    addx 12
    addx -1
    addx 3
    addx 1
    noop
    noop
    noop
    addx -9
    addx 18
    addx 1
    addx 2
    noop
    noop
    addx 9
    noop
    noop
    noop
    addx -1
    addx 2
    addx -37
    addx 1
    addx 3
    noop
    addx 15
    addx -21
    addx 22
    addx -6
    addx 1
    noop
    addx 2
    addx 1
    noop
    addx -10
    noop
    noop
    addx 20
    addx 1
    addx 2
    addx 2
    addx -6
    addx -11
    noop
    noop
    noop
""".trimIndent()

