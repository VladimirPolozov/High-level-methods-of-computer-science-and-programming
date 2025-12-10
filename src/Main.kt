import app.ExitCodeProcessor
import app.components.AppComponents
import application.services.RequestProcessor
import domain.enums.ExitCode
import infrastructure.adapters.AppArgsParser
import org.slf4j.LoggerFactory
import java.sql.SQLException

// Точка входа: парсит аргументы, выводит справку при --help или ошибке, иначе обрабатывает запрос и завершает программу с нужным кодом.

val logger = LoggerFactory.getLogger("main")

fun finishWithCode(exitCode: ExitCode, message: String? = null) {
    if (message != null) {
        logger.info(message)
    }
    logger.info("Program finished with code ${exitCode.name}")
    ExitCodeProcessor.finish(exitCode)
}

fun safeFinish(exitCode: ExitCode, errorMessage: String? = null) {
    if (errorMessage != null) {
        System.err.println(errorMessage)
    }
    finishWithCode(exitCode, errorMessage)
}

fun main(args: Array<String>) {
    try {
        runApplication(args)
    } catch (e: Exception) {
        System.err.println("Unexpected error: ${e.message}")
        e.printStackTrace()
        finishWithCode(ExitCode.SQL_ERROR, "Unexpected error: ${e.message}")
    }
}

fun runApplication(args: Array<String>) {
    if (args.contains("-h") || args.contains("--help")) {
        println("./run.sh --login <user> --password <pass> --action <read/write/execute> --resource <path> --volume <num>")
        finishWithCode(ExitCode.HELP, "Help requested")
        return
    }

    val request = AppArgsParser.parse(args)
    if (request == null) {
        safeFinish(
            ExitCode.INVALID_FORMAT,
            "Error: Invalid arguments provided. Use -h or --help for usage information."
        )
        return
    }

    val processor: RequestProcessor
    try {
        processor = AppComponents.createDefault()
    } catch (e: Exception) {
        safeFinish(
            ExitCode.DB_CONNECTION_ERROR,
            "Failed to initialize application: ${e.message}"
        )
        return
    }

    val code = try {
        processor.process(request)
    } catch (e: SQLException) {
        safeFinish(
            ExitCode.SQL_ERROR,
            "SQL error occurred: ${e.message}"
        )
        return
    }

    finishWithCode(code, "Request processed successfully")
}