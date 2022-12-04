private val example = """
    A Y
    B X
    C Z
""".trimIndent()

fun main() {
//    Day02().run(example)
    Day02().run()
}

class Day02 : Day(2) {
    override fun part1(input: String): Any =
        input.tuples()
            .map { (enemy, hero) -> Move.from(enemy) to Move.from(hero) }
            .sumOf(::calculatePoints)


    override fun part2(input: String): Any =
        input.tuples()
            .map { (enemy, result) -> Move.from(enemy) to Result.from(result) }
            .map { (enemy, result) -> enemy to enemy.resultsIn(result) }
            .sumOf(::calculatePoints)


    private fun calculatePoints(moves: Pair<Move, Move>): Int =
        with(moves) {
            second.points + when {
                first beats second -> 0
                second beats first -> 6
                else -> 3
            }
        }

    enum class Result(val symbol: String) {
        WIN("Z"), LOSS("X"), DRAW("Y");

        companion object {
            fun from(value: String) = Result.values().first { it.symbol == value }
        }
    }

    enum class Move(val points: Int) {
        ROCK(1) {
            override val beats
                get() = SCISSORS
        },
        PAPER(2) {
            override val beats
                get() = ROCK
        },
        SCISSORS(3) {
            override val beats
                get() = PAPER
        };

        infix fun beats(other: Move) = this.beats == other

        abstract val beats: Move

        fun resultsIn(result: Result): Move =
            when (result) {
                Result.LOSS -> this.beats
                Result.DRAW -> this
                Result.WIN -> this.beats.beats
            }

        companion object {
            fun from(value: String) = when (value) {
                "A", "X" -> ROCK
                "B", "Y" -> PAPER
                "C", "Z" -> SCISSORS
                else -> throw IllegalArgumentException("Invalid move: $value")
            }
        }
    }
}

private fun String.tuples(): List<List<String>> = this.split("\n").map { it.split(" ") }


