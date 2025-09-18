import kotlin.collections.emptyList

fun main(args: Array<String>) {
    val words = if (args.isEmpty()) {
        readlnOrNull()?.split(" ")?.filter { it.isNotEmpty() } ?: emptyList()
    } else {
        args.toList()
    }

    words.groupingBy { it }.eachCount().entries.toList().sortedWith (
        compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key }
    ).forEach { (word, wordCount) ->
        println("$word - $wordCount") }
}