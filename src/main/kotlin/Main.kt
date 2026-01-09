import application.services.RequestProcessor
import main.kotlin.domain.enums.ExitCode
import infrastructure.adapters.cli.AppArgsParser
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import kotlin.system.exitProcess

@SpringBootApplication
@EnableJpaRepositories("main.kotlin.infrastructure.adapters.db.repositories.jpa")
class SecurityApplication(
    private val parser: AppArgsParser,
    private val processor: RequestProcessor
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(SecurityApplication::class.java)

    override fun run(vararg args: String) {
        val exitCode = try {
            execute(args)
        } catch (e: Exception) {
            logger.error("CRITICAL UNEXPECTED ERROR: ${e.message}", e)
            ExitCode.OTHER_ERROR
        }

        logger.info("Program finished with code ${exitCode.name} (${exitCode.code})")
        exitProcess(exitCode.code)
    }

    private fun execute(args: Array<out String>): ExitCode {
        if (args.contains("-h") || args.contains("--help")) {
            println("./run.sh --login <user> --password <pass> --action <read/write/execute> --resource <path> --volume <num>")
            return ExitCode.HELP
        }

        val request = parser.parse(args) ?: run {
            logger.warn("Invalid arguments provided")
            return ExitCode.INVALID_FORMAT
        }

        return try {
            processor.process(request)
        } catch (e: Exception) {
            logger.error("Error during request processing: ${e.message}")
            ExitCode.DB_CONNECTION_ERROR
        }
    }
}

fun main(args: Array<String>) {
    runApplication<SecurityApplication>(*args) {
        setWebApplicationType(WebApplicationType.NONE)
    }
}