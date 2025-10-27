package app
import app.components.AppComponents
import domain.enums.ExitCode
import infrastructure.adapters.AppArgsParser


fun main(args: Array<String>) {
    val request = AppArgsParser.parse(args)
    if (args.contains("-h") || args.contains("--help") || request == null) {
        println("Usage: java -jar app.jar --login <user> --password <pass> --action <read|write|execute> --resource <path> --volume <int>")
        ExitCodeProcessor.finish(ExitCode.HELP)
    }

    val processor = AppComponents.createDefault()
    val code = processor.process(request!!)
    ExitCodeProcessor.finish(code)
}