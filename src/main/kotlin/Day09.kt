import kotlin.math.abs
import kotlin.math.sign

private val example = """
    R 4
    U 4
    L 3
    D 1
    R 4
    D 1
    L 5
    R 2
""".trimIndent()

//fun main() = Day09().run(example)
fun main() = Day09().run()

class Day09 : Day(9) {
    override fun part1(input: String) =
        solveForKnots(input, 2)

    override fun part2(input: String): Any {
        return solveForKnots(input, 10)
    }

    private fun solveForKnots(input: String, knots: Int): Int {
        val commands = input.split("\n").map(Command::parse)
        var segments = Array(knots) { Point(0, 0) }

        val visited = mutableSetOf(segments.last())

        for (command in commands) {
            val result = command.moveFrom(segments[0].x, segments[0].y)

            for (point in result) {

                segments.forEachIndexed { index, segment ->
                    if (index == 0) segments[index] = point else moveSegment(
                        index,
                        segments,
                        segment
                    )
                }

                visited.add(segments.last())

//                printMap(segments)
            }
        }

//        printComplete(visited)
        return visited.size
    }

    private fun printComplete(visited: MutableSet<Point>) {
        val map = Array(20) { Array(20) { '.' } }
        visited.forEach { point -> map[point.y + 10][point.x + 10] = '#' }
        println(map.joinToString("\n") { it.joinToString("") })
    }

    private fun moveTail(tail: Point, point: Point): Point =
        if (abs(point.x - tail.x) > 1 && abs(point.y - tail.y) > 1) {
            val x = tail.x + (point.x - tail.x).sign
            val y = tail.y + (point.y - tail.y).sign
            Point(x, y)
        } else if (abs(point.x - tail.x) > 1) {
            Point(tail.x + (point.x - tail.x).sign, point.y)
        } else if (abs(point.y - tail.y) > 1) {
            Point(point.x, tail.y + (point.y - tail.y).sign)
        } else {
            Point(tail.x, tail.y)
        }

    private fun printMap(segments: Array<Point>) {
        val size = 20
        val map = Array(size) { Array(size) { "." } }
        segments.forEachIndexed { index, segment ->
            map[segment.y + size / 2][segment.x + size / 2] =
                if (map[segment.y + size / 2][segment.x + size / 2] == ".") (when (index) {
                    0 -> "H"
                    segments.size - 1 -> "T"
                    else -> index
                }.toString()) else map[segment.y + size / 2][segment.x + size / 2]
        }
        println(map.joinToString("\n") { it.joinToString("") })
        println()
    }

    private fun moveSegment(index: Int, segments: Array<Point>, segment: Point) {
        segments[index] = moveTail(segment, segments[index - 1])
    }
}

private data class Point(val x: Int, val y: Int)
private data class Command(val direction: Direction, val distance: Int) {
    enum class Direction {
        R, U, L, D
    }

    companion object {
        fun parse(input: String): Command = with(input.split(" ")) {
            Command(Direction.valueOf(first()), last().toInt())
        }
    }

    fun moveFrom(x: Int, y: Int) =
        when (direction) {
            Direction.R -> (x + 1..x + distance).map { it to y }.map { Point(it.first, it.second) }
            Direction.D -> (y + 1..y + distance).map { x to it }.map { Point(it.first, it.second) }
            Direction.L -> (x - 1 downTo x - distance).map { it to y }.map { Point(it.first, it.second) }
            Direction.U -> (y - 1 downTo y - distance).map { x to it }.map { Point(it.first, it.second) }
        }
}
