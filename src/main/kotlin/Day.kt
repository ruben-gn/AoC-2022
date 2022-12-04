import java.io.File
import kotlin.system.measureTimeMillis

abstract class Day(private val value: Int) {
    fun run(input: String? = null) {
        println("--- Day $value ---")
        val totalTime= measureTimeMillis {
            runDay(input)
        }
        println("Total time: $totalTime ms\n")
    }

    private fun runDay(input: String?) {
        if (input != null) {
            runParts(input)
        } else {
            val fileName = "src/main/resources/day${value.toString().padStart(2, '0')}.txt"
            File(fileName).readText().let(::runParts)
        }
    }

    private fun runParts(input: String) {
        runPart(1) {part1(input)}
        runPart(2) {part2(input)}
    }

    private fun runPart(partNumber: Int, part: () -> Any) {
        val result: Any
        val timed = measureTimeMillis {
            result = part()
        }
        println("Part $partNumber: $result ($timed ms)")
    }

    abstract fun part1(input: String): Any
    abstract fun part2(input: String): Any
}
