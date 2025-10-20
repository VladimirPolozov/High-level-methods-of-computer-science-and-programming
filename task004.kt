fun main(args: Array<String>) {
    args.groupingBy { it }.eachCount().forEach { (word, wordCount) ->
        println("$word - $wordCount") }
}