package infrastructure.adapters

import domain.dto.AccessRequest
import domain.entities.Action
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required


// Парсит аргументы командной строки через kotlinx-cli, возвращает AccessRequest или null (включая обработку --help и невалидных данных)
object AppArgsParser {
    fun parse(args: Array<String>): AccessRequest? {
        val parser = ArgParser("resource-access")

        val login by parser.option(ArgType.String, description = "User login").required()
        val password by parser.option(ArgType.String, description = "User password").required()
        val action by parser.option(ArgType.String, description = "Action: read/write/execute").required()
        val resource by parser.option(ArgType.String, description = "Resource path").required()
        val volume by parser.option(ArgType.Int, description = "Requested volume").required()
        val help by parser.option(ArgType.Boolean, shortName = "h", fullName = "help", description = "Show help")

        return try {
            parser.parse(args)
            if (help == true) null
            else {
                val act = Action.fromString(action) ?: return null
                AccessRequest(login, password, resource, act, volume)
            }
        } catch (e: Exception) {
            null
        }
    }
}