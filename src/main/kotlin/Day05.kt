private val example = """
    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
""".trimIndent()

fun main() = Day05().run()

class Day05 : Day(5) {
    override fun part1(input: String): Any {
        val start = parse(input.split("\n\n"))
        start.second.forEach {
            (0 until it.amount).map { _ -> start.first[it.from].removeLast() }
                .forEach(start.first[it.to]::add)
        }
        return start.first.drop(1).joinToString("") { it.last() }
    }

    override fun part2(input: String): Any {
        val start = parse(input.split("\n\n"))
        start.second.forEach {
            (0 until it.amount).map { _ -> start.first[it.from].removeLast() }
                .reversed()
                .forEach(start.first[it.to]::add)
        }
        return start.first.drop(1).joinToString("") { it.last() }
    }

    private fun parse(input: List<String>): Pair<Array<ArrayDeque<String>>, List<Move>> {
        val situation = parseSituation(input[0])
        val moves = input[1].split("\n").map { parseMove(it) }
        return situation to moves
    }

    private fun parseSituation(situation: String): Array<ArrayDeque<String>> {
        val elements = situation.split("\n").map { line -> line.chunked(4).map { it.drop(1).dropLast(it.length - 2) } }
        val stacks = Array(elements.last().maxOf { it.toInt()} + 1) { ArrayDeque<String>() }
        elements.reversed().drop(1)
            .forEach { list -> list.forEachIndexed { index, s -> if (s.isNotBlank()) stacks[index + 1 ].addLast(s) } }
        return stacks
    }

    private fun parseMove(move: String): Move {
        val digits = move.split(" ").filter(String::isNumeric).map(String::toInt)

        return Move(digits[0], digits[1], digits[2])
    }
}

private fun String.isNumeric(): Boolean = this.all { it.isDigit() }
private data class Move(val amount: Int, val from: Int, val to: Int)