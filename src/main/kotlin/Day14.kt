import kotlin.math.sign

private val example = """
    498,4 -> 498,6 -> 496,6
    503,4 -> 502,4 -> 502,9 -> 494,9
""".trimIndent()

fun main() = Day14().run()
//fun main() = Day14().run(example)

class Day14 : Day(14) {
    private var minX = Int.MAX_VALUE
    private var maxX = Int.MIN_VALUE
    private var minY = Int.MAX_VALUE
    private var maxY = Int.MIN_VALUE

    private val increaseX = 300
    private val half = increaseX / 2

    override fun part1(input: String): Any {

        val coordinates = getCoordinates(input)
        val grid = initialGrid(coordinates)

        return run(grid)
    }

    override fun part2(input: String): Any {
        val coordinates = getCoordinates(input)
        val grid = initialGrid(coordinates)

        grid[maxY + 2].fill("▓")

        return run(grid)
    }

    private fun run(grid: Array<Array<String>>): Int {
        var sand = 500 to 0
        var counter = 0
        while (true) {

            if (grid[sand.second + 1][sand.first - minX + 1 + half] == "·") {
                sand = sand.first to sand.second + 1
            } else if (grid[sand.second + 1][sand.first - minX + half] == "·") {
                sand = sand.first - 1 to sand.second + 1
            } else if (grid[sand.second + 1][sand.first - minX + 2 + half] == "·") {
                sand = sand.first + 1 to sand.second + 1
            } else {
                grid[sand.second][sand.first - minX + 1 + half] = "o"

                counter++
                if (sand.first == 500 && sand.second == 0) {
                    break
                }

                sand = 500 to 0
            }
            if (sand.second > maxY + 1) break

        }
        return counter
    }

    private fun getCoordinates(input: String) = input.split("\n")
        .map { line -> line.split(" -> ") }
        .map { line ->
            line.map { pair ->
                with(pair.split(",")) {
                    if (this[0].toInt() < minX) minX = this[0].toInt()
                    if (this[0].toInt() > maxX) maxX = this[0].toInt()
                    if (this[1].toInt() < minY) minY = this[1].toInt()
                    if (this[1].toInt() > maxY) maxY = this[1].toInt()

                    this[0].toInt() to this[1].toInt()
                }
            }
        }

    private fun fillGrid(it: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>, grid: Array<Array<String>>) {
        it.forEach { drawLine(grid, it.first, it.second) }
        grid[0][500 - minX + 1 + half] = "+"
    }


    private fun drawLine(grid: Array<Array<String>>, first: Pair<Int, Int>, second: Pair<Int, Int>) {
        val sign = sign((first.first - second.first).toDouble()) to sign((first.second - second.second).toDouble())

        var x = first
        while (x != second) {
            grid[x.second][x.first - minX + 1 + half] = "▓"
            x = (x.first - sign.first).toInt() to (x.second - sign.second).toInt()
        }
        grid[second.second][second.first - minX + 1 + half] = "▓"
    }

    private fun initialGrid(c: List<List<Pair<Int, Int>>>): Array<Array<String>> {
        val grid = Array(maxY + 3) {
            Array(maxX - minX + increaseX) { "·" }
        }

        c.map { it.zipWithNext() }
            .forEach { fillGrid(it, grid) }

        return grid
    }
}