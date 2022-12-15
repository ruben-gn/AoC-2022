import java.lang.Long.min
import kotlin.math.abs
import kotlin.math.max

fun main() = Day15().run()
//fun main() = Day15().run(example)

//private val max = 20L // example
val max = 4000000L // real

class Day15 : Day(15) {
    override fun part1(input: String): Any {
        val sensors = input.split("\n").map { it.toSensor() }

        val (minX, maxX) = sensors.minMax()
        val knownPositions = sensors.map { it.beacon } + sensors.map { it.position }

        return (minX..maxX).count { x ->
            with(Point(x, max / 2L)) {
                sensors.any { it.withinBeaconRange(this) && this !in knownPositions }
            }
        }
    }

    override fun part2(input: String): Any {
        val sensors = input.split("\n").map { it.toSensor() }

        return (0..max).mapIndexed { index, y ->
            val range = sensors.mapNotNull { sensor -> sensor.range(y) }.sortedBy { it.first }.merge()
            index to range
        }.first { it.second.size > 1 }.let {
            it.first + (it.second[0].second + 1) * 4000000
        }
    }

    data class Sensor(val position: Point, val beacon: Point) {
        private fun distanceToPoint(point: Point): Long = abs(point.x - position.x) + abs(point.y - position.y)

        fun range(y: Long): Pair<Long, Long>? = with(abs(position.y - y)) {
            if (this in 0..distanceToBeacon)
                max(position.x - (distanceToBeacon - this), 0) to
                        min(position.x + (distanceToBeacon - this), max)
            else null
        }

        fun withinBeaconRange(point: Point): Boolean = this.distanceToPoint(point) <= this.distanceToBeacon
        val distanceToBeacon = distanceToPoint(beacon)
    }

    data class Point(val x: Long, val y: Long)

    private fun String.toSensor() = with(this.split(":")) {
        Sensor(position = this[0].toPoint(), beacon = this[1].toPoint())
    }

    private fun String.toPoint() = with(this.split(" ").takeLast(2)) {
        val coordinates = this.map { it.split("=") }
        Point(coordinates[0][1].split(",")[0].toLong(), coordinates[1][1].toLong())
    }

    private fun List<Sensor>.minMax(): Pair<Long, Long> =
        this.minOf { it.position.x - it.distanceToBeacon } to this.maxOf { it.position.x + it.distanceToBeacon }

    private fun List<Pair<Long, Long>>.merge(): List<Pair<Long, Long>> = this.fold(emptyList()) { acc, pair ->
        when (acc.size) {
            0 -> listOf(pair)
            2 -> return acc
            else -> {
                if (pair.first <= acc.first().second + 1)
                    listOf(acc.first().merge(pair))
                else return acc + pair
            }
        }
    }

    private fun Pair<Long, Long>.merge(pair: Pair<Long, Long>) =
        this.first to max(this.second, pair.second)
}

private val example = """
Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3
""".trimIndent()