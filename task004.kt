fun main(args: Array<String>) {
    args.groupingBy { it }.eachCount().forEach { println("$it ") }
}