fun main(args: Array<String>) {
    args.groupingBy { it }.eachCount().entries.toList().sortedWith (
        compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key }
    ).forEach { (word, wordCount) ->
        println("$word - $wordCount") }
}