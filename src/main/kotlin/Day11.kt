//fun main() = Day11().run(example)
fun main() = Day11().run()

class Day11 : Day(11) {
    override fun part1(input: String): Any {
        val monkeys = input.split("\n\n").map(::parseMonkey)
        return run(20, monkeys) { monkey, item ->
            val op = monkey.operation(item)
            op / 3
        }
    }

    override fun part2(input: String): Any {
        val monkeys = input.split("\n\n").map(::parseMonkey)
        val supermod = monkeys.map { it.divisibleBy }.reduce { acc, i -> acc * i }

        return run(10000, monkeys) { monkey, item ->
            val op = monkey.operation(item)
            op % supermod
        }
    }

    private fun parseMonkey(monkeyString: String): Monkey = with(monkeyString.split("\n")) {
        val items = ArrayDeque(getItemsFrom(this))
        val operation = this[2].split("=").last().split(" ").takeLast(2).toOperation()
        val test = this[3].takeLastInt()
        val yesMonkey = this[4].takeLastInt()
        val noMonkey = this[5].takeLastInt()

        return Monkey(
            items,
            operation,
            test,
            yesMonkey,
            noMonkey
        )
    }

    private fun String.takeLastInt(split: String = " ") = this.split(split).last().toInt()

    private fun getItemsFrom(itemList: List<String>) =
        itemList[1].split(":").last().split(",").map { it.trim() }.map { it.toLong() }

    private fun run(rounds: Int, monkeys: List<Monkey>, worry: (Monkey, Long) -> Long): Long {
        repeat(rounds) {
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    monkey.ops++

                    val newWorry = worry(monkey, item)

                    if (newWorry % monkey.divisibleBy == 0L) {
                        monkeys[monkey.yesMonkey].items.add(newWorry)
                    } else {
                        monkeys[monkey.noMonkey].items.add(newWorry)
                    }
                }

                monkey.items.clear()
            }
        }
        return monkeys.map { it.ops }.sortedDescending().take(2).reduce { acc, i -> acc * i }
    }
}

private fun List<String>.toOperation(): (Long) -> Long {
    val y = { x: Long -> if (this[1] == "old") x else this[1].toLong() }
    return when (this[0]) {
        "*" -> { x -> x * y(x) }
        "+" -> { x -> x + y(x) }
        else -> { x -> x }
    }
}

data class Monkey(
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val divisibleBy: Int,
    val yesMonkey: Int,
    val noMonkey: Int
) {
    var ops = 0L
}

private val example = """
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
""".trimIndent()