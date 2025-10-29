import app.ExitCodeProcessor
import app.components.AppComponents
import domain.enums.ExitCode
import infrastructure.adapters.AppArgsParser


// Точка входа: парсит аргументы, выводит справку при --help или ошибке, иначе обрабатывает запрос и завершает программу с нужным кодом.
fun main(args: Array<String>) {
    val request = AppArgsParser.parse(args)
    if (args.contains("-h") || args.contains("--help") || request == null) {
        println("./run.sh --login <user> --password <pass> --action <read/write/execute> --resource <path> --volume <num>")
        ExitCodeProcessor.finish(ExitCode.HELP)
    }

    val processor = AppComponents.createDefault()
    val code = processor.process(request!!)
    ExitCodeProcessor.finish(code)
}