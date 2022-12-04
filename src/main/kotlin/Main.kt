import java.io.File

fun main() {
    Runner().run()
}

/**
 * Magic happens here
 */
class Runner {
    val classLoader: ClassLoader = this.javaClass.classLoader

    fun run() = runDays(1..readLastDayFromFileList())

    private fun runDays(days: IntRange) = days.forEach(::runDay)

    private fun runDay(dayNumber: Int) = loadDayClass(dayNumber).run()

    private fun loadDayClass(number: Int) =
        classLoader.loadClass("Day${number.toString().padStart(2, '0')}")
            .getDeclaredConstructor()
            .newInstance() as Day

    private fun readLastDayFromFileList() =
        File("src/main/kotlin").listFiles()
            .map { it.nameWithoutExtension }
            .sortedDescending()
            .first { it.startsWith("Day") }
            .takeLast(2)
            .toInt()
}