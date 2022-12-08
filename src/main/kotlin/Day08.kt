private val example = """
    30373
    25512
    65332
    33549
    35390
    """.trimIndent()

//fun main() = Day08().run(example)
fun main() = Day08().run()

class Day08 : Day(8) {
    override fun part1(input: String): Any {
        val trees = parseGrid(input).flatten()
        return trees.count { tree -> isVisible(trees, tree) }
    }

    override fun part2(input: String): Any {
        val trees = parseGrid(input).flatten()
        return trees.maxOf { tree -> scenicScore(tree, trees) }
    }

    private fun scenicScore(tree: Tree, trees: List<Tree>): Int {
        fun getScore(line: List<Tree>): Int {
            var count = 0
            for (anotherTree in line) {
                count++
                if (anotherTree.value >= tree.value)
                    break
            }
            return count
        }

        val left = getScore(trees.filter { it.directlyLeftOf(tree) }.reversed())
        val right = getScore(trees.filter { it.directlyRightOf(tree) })
        val up = getScore(trees.filter { it.directlyAbove(tree) }.reversed())
        val down= getScore(trees.filter { it.directlyBelow(tree) })

        return left * right * up * down
    }

    private fun isVisible(trees: List<Tree>, tree: Tree): Boolean {
        val left = highest(trees, tree, Tree::directlyLeftOf)
        val right = highest(trees, tree, Tree::directlyRightOf)
        val up = highest(trees, tree, Tree::directlyAbove)
        val down = highest(trees, tree, Tree::directlyBelow)

        return left || right || up || down
    }

    private fun highest(trees: List<Tree>, tree: Tree, filter: Tree.(Tree) -> Boolean) =
        relevantTrees(trees, filter, tree).all { tree.higherThan(it) }

    private fun relevantTrees(
        trees: List<Tree>,
        filter: Tree.(Tree) -> Boolean,
        tree: Tree
    ) = trees.filter { it.filter(tree) }

    private fun parseGrid(grid: String) = grid.split("\n")
        .mapIndexed { index, line -> parseLine(index, line) }

    private fun parseLine(y: Int, line: String) = line.split("")
        .filter { it.isNotBlank() }
        .mapIndexed { x, value -> Tree(x, y, value.toInt()) }
}

private data class Tree(val x: Int, val y: Int, val value: Int) {
    fun sameRow(other: Tree) = y == other.y
    fun sameColumn(other: Tree) = x == other.x
    fun leftOf(other: Tree) = x < other.x
    fun rightOf(other: Tree) = x > other.x
    fun above(other: Tree) = y < other.y
    fun below(other: Tree) = y > other.y

    fun directlyLeftOf(tree: Tree) = sameRow(tree) && leftOf(tree)
    fun directlyRightOf(tree: Tree) = sameRow(tree) && rightOf(tree)
    fun directlyAbove(tree: Tree) = sameColumn(tree) && above(tree)
    fun directlyBelow(tree: Tree) = sameColumn(tree) && below(tree)

    fun higherThan(other: Tree) = value > other.value
}



