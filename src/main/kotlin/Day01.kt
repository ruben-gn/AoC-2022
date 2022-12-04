typealias Calories = Int

fun main() {
    Day01().run()
}

class Day01 : Day(1) {

    override fun part1(input: String): Calories =
        totalCaloriesPerElf(input)
            .max()

    override fun part2(input: String): Calories =
        totalCaloriesPerElf(input)
            .sorted()
            .takeLast(3)
            .sum()

    private fun totalCaloriesPerElf(food: String) =
        food.split("\n\n")
            .map(::splitCaloriesByElf)
            .map(::sumCaloriesPerElf)

    private fun sumCaloriesPerElf(calories: List<Int>) = calories.sum()

    private fun splitCaloriesByElf(calorieGroup: String) =
        calorieGroup.split("\n")
            .map(String::toInt)

}