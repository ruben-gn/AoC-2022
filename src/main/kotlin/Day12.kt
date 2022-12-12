private val example = """
    Sabqponm
    abcryxxl
    accszExk
    acctuvwj
    abdefghi
""".trimIndent()

//fun main() = Day12().run(example)
fun main() = Day12().run()

class Day12 : Day(12) {
    override fun part1(input: String): Any = input.toField()
        .let { (end, start, grid) ->
            findShortestPathToPredicate(
                start,
                grid
            ) { cell -> cell.coordinates.x == end.x && cell.coordinates.y == end.y }
        }

    override fun part2(input: String): Any = input.toField()
        .let { (_, end, grid) -> findShortestPathToPredicate(end, grid) { cell -> cell.height == 'a' } }

    private fun findShortestPathToPredicate(start: Point, grid: List<List<Cell>>, endCheck: (Cell) -> Boolean): Int {
        val visited = mutableListOf<Point>()

        val startCell = grid[start.y][start.x]
        startCell.cost = 1
        var queue = mutableListOf(startCell)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            visited.add(current.coordinates)

            val neighbors = grid.neighbors(current)
            for (neighbor in neighbors) {
                if (endCheck(neighbor)) {
                    return current.cost
                }

                val cost = current.cost + 1
                if (neighbor.cost > cost) {
                    grid[neighbor.coordinates.y][neighbor.coordinates.x].cost = cost
                }

                if (visited.none { (x, y) -> x == neighbor.coordinates.x && y == neighbor.coordinates.y }
                    && queue.none { c -> c.coordinates.x == neighbor.coordinates.x && c.coordinates.y == neighbor.coordinates.y }) {
                    queue.add(grid[neighbor.coordinates.y][neighbor.coordinates.x])
                }
            }

            queue = queue.sortedBy { it.cost }.toMutableList()
        }

        return Int.MAX_VALUE
    }

    private fun String.toField(): Triple<Point, Point, List<List<Cell>>> {
        var start = Point(0, 0)
        var end = Point(0, 0)

        val grid = this.lines().mapIndexed { y, line ->
            line.mapIndexed { x, cell ->
                Cell(
                    Point(x, y), when (cell) {
                        'S' -> {
                            start = Point(x, y)
                            'a'
                        }

                        'E' -> {
                            end = Point(x, y)
                            'z'
                        }

                        else -> cell
                    }
                )
            }
        }

        return Triple(start, end, grid)
    }

    private fun List<List<Cell>>.neighbors(current: Cell): List<Cell> {
        val neighbors = mutableListOf<Cell>()
        val c = current.coordinates

        if (c.x > 0 && current.height - this[c.y][c.x - 1].height <= 1)
            neighbors.add(this[c.y][c.x - 1])

        if (c.x < this.first().size - 1 && current.height - this[c.y][c.x + 1].height <= 1)
            neighbors.add(this[c.y][c.x + 1])

        if (c.y > 0 && current.height - this[c.y - 1][c.x].height <= 1)
            neighbors.add(this[c.y - 1][c.x])

        if (c.y < this.size - 1 && current.height - this[c.y + 1][c.x].height <= 1)
            neighbors.add(this[c.y + 1][c.x])

        return neighbors
    }

    private data class Cell(
        val coordinates: Point,
        val height: Char,
        var cost: Int = Int.MAX_VALUE,
    )

    private data class Point(val x: Int, val y: Int)
}
