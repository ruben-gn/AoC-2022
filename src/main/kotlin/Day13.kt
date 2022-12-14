val example2 = """
    [[4,[4],[[10,7,2],[1,6,5,7,4],[7,3,3,1,5],[]],1],[[],[[7,6,3]],5,5]]
    [[[10]]]
""".trimIndent()

//fun main() = Day13().run(example2)
//fun main() = Day13().run(example)
fun main() = Day13().run()

class Day13 : Day(13) {
    override fun part1(input: String): Any = input
        .split("\n\n")
        .map(::line)
        .map { it.parse() }
        .mapIndexed { index, (left, right) -> if (left < right) index + 1 else 0 }
        .sum()

    override fun part2(input: String): Any {
        val (d1, d2) = Node.LisNode(listOf(Node.LisNode(listOf(Node.NumNode(2))))) to Node.LisNode(listOf(Node.LisNode(listOf(Node.NumNode(6)))))

        val packets = (input.replace("\n\n", "\n")
            .split("\n").map { it.removeSurrounding("[", "]") }
            .map { it.parse() }
            .map { Node.LisNode(it) } + d1 + d2)
            .sorted()
        return (packets.indexOf(d1) + 1) * (packets.indexOf(d2) + 1)
    }

    private fun line(block: String) =
        block.split("\n")
            .map { it.removeSurrounding("[", "]") }


    private fun List<String>.parse(): Pair<Node.LisNode, Node.LisNode> {
        return Node.LisNode(this[0].parse()) to Node.LisNode(this[1].parse())
    }

    private sealed interface Node : Comparable<Node> {
        data class NumNode(val value: Int) : Node {
            override fun toString() = "N${value}"
        }

        private fun NumNode.toList() = LisNode(listOf(this))

        data class LisNode(val value: List<Node>) : Node {
            override fun toString() = "L${value}"
        }

        override operator fun compareTo(other: Node): Int =
            when {
                this is NumNode && other is NumNode -> this.value.compareTo(other.value)
                this is LisNode && other is LisNode -> {
                    repeat(minOf(this.value.size, other.value.size)) { index ->
                        val result = this.value[index] compareTo other.value[index]
                        if (result != 0)
                            return result
                    }
                    this.value.size.compareTo(other.value.size)
                }

                else -> when {
                    this is NumNode -> this.toList().compareTo(other)
                    other is NumNode -> this.compareTo(other.toList())
                    else -> error("Should not happen")
                }
            }
    }

    private fun String.parse(): List<Node> {
        val running = mutableListOf<Node>()
        var index = 0
        while (index < this.length) {
            val value = this[index]
            if (value.isDigit()) {
                val start = index
                if (this.length > index + 1 && this[index + 1].isDigit()) {
                    index += 1
                }
                running.add(Node.NumNode(this.substring(start, index + 1).toInt()))
            }
            if (value == '[') {
                val start = index
                var counter = 1
                while (counter > 0) {
                    index++
                    if (this[index] == '[') counter++
                    if (this[index] == ']') counter--
                }
                running.add(Node.LisNode(this.substring(start + 1, index).parse()))
            }
            index++
        }
        return running
    }
}

private val example = """
    [1,1,3,1,1]
    [1,1,5,1,1]

    [[1],[2,3,4]]
    [[1],4]

    [9]
    [[8,7,6]]

    [[4,4],4,4]
    [[4,4],4,4,4]

    [7,7,7,7]
    [7,7,7]

    []
    [3]

    [[[]]]
    [[]]

    [1,[2,[3,[4,[5,6,7]]]],8,9]
    [1,[2,[3,[4,[5,6,0]]]],8,9]
""".trimIndent()