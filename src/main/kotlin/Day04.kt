private val example = """
    2-4,6-8
    2-3,4-5
    5-7,7-9
    2-8,3-7
    6-6,4-6
    2-6,4-8
""".trimIndent()

fun main() = Day04().run()
//fun main() = Day04().run(example)

class Day04 : Day(4) {
    override fun part1(input: String): Any =
        input.split("\n")
            .map(::toRanges)
            .count { (one, other) -> one.contains(other) or other.contains(one) }

    override fun part2(input: String): Any =
        input.split("\n")
            .map(::toRanges)
            .count { (one, other) -> one.overlaps(other) }

    private fun IntRange.overlaps(other: IntRange): Boolean =
        other.any { this.contains(it) }

    private fun IntRange.contains(other: IntRange): Boolean =
        other.all { this.contains(it) }

    private fun toRanges(pairOfRanges: String): List<IntRange> =
        pairOfRanges.split(",")
            .map(::toRange)
            .sortedBy { it.first }

    private fun toRange(range: String): IntRange =
        range.split("-")[0].toInt()..range.split("-")[1].toInt()

}