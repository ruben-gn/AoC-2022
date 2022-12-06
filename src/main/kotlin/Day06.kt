private val example = """
    mjqjpqmgbljsphdztnvjfqwrcgsmlb
""".trimIndent()

//fun main() = Day06().run(example)
fun main() = Day06().run()

class Day06 : Day(6) {
    override fun part1(input: String): Any =
        findFirstDifferent(input, 4)

    override fun part2(input: String): Any =
        findFirstDifferent(input, 14)

    private fun findFirstDifferent(input: String, num: Int) = input.windowed(num, 1)
        .map { it.toSet() }
        .indexOfFirst { it.size == num }
        .let { it + num }
}