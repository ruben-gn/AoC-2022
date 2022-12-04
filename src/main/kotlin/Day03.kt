private val example = """
    vJrwpWtwJgWrhcsFMMfFFhFp
    jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
    PmmdzqPrVvPwwTWBwg
    wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
    ttgJtRGJQctTZtZT
    CrZsJsPPZsGzwwsLwLmpwMDw
    """.trimIndent()


fun main() {
    Day03().run()
//    Day03().run(example)
}


class Day03 : Day(3) {
    override fun part1(input: String): Any = input.split("\n")
        .map { it.chunked(it.length / 2) }
        .map(this::toDouble)
        .sumOf { it.priority }

    override fun part2(input: String): Any = input.split("\n")
        .chunked(3)
        .map(this::toDouble)
        .sumOf { it.priority }

    private fun toDouble(compartments: List<String>): Char =
        compartments.map(String::toSet).reduce { acc, set -> acc.intersect(set) }.first()

    private val Char.priority: Int
        get() = letters.indexOf(this) + 1

    private val letters = ('a'..'z').toList() + ('A'..'Z').toList()
}
