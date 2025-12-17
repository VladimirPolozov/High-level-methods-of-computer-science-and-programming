import kotlin.system.exitProcess
import app.components.AppContainer
import application.services.RequestProcessor
import domain.enums.ExitCode
import domain.exceptions.DbConnectionException
import infrastructure.adapters.cli.AppArgsParser
import org.slf4j.LoggerFactory

// Точка входа: парсит аргументы, выводит справку при --help или ошибке, иначе обрабатывает запрос и завершает программу с нужным кодом.

val logger = LoggerFactory.getLogger("main")

fun finishWithCode(exitCode: ExitCode, message: String? = null) {
    if (message != null) {
        logger.info(message)
    }
    logger.info("Program finished with code ${exitCode.name} (${exitCode.code})")
    exitProcess(exitCode.code)
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
        System.err.println("CRITICAL UNEXPECTED ERROR: ${e.message}")
        e.printStackTrace()
        safeFinish(
            ExitCode.OTHER_ERROR,
            "CRITICAL UNEXPECTED ERROR: Failed to run application. Details: ${e.message}"
        )
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
        processor = AppContainer.createDefault()
    } catch (e: DbConnectionException) {
        safeFinish(
            ExitCode.DB_CONNECTION_ERROR,
            "Failed to initialize application (DB connection/config error): ${e.message}"
        )
        return
    } catch (e: Exception) {
        safeFinish(
            ExitCode.OTHER_ERROR,
            "Failed to initialize application: ${e.message}"
        )
        return
    }

    val code = try {
        processor.process(request)
    } catch (e: DbConnectionException) {
        safeFinish(
            ExitCode.DB_CONNECTION_ERROR,
            "Database error during request processing: ${e.message}"
        )
        return
    }

    finishWithCode(code, "Request processed successfully")
}