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

        val login by parser.option(fullName = "login", shortName="l",
            type = ArgType.String, description = "User login"
        ).required()

        val password by parser.option( fullName = "password", shortName = "p",
            type = ArgType.String, description = "User password"
        ).required()

        val action by parser.option(fullName = "action", shortName = "a",
            type = ArgType.String, description = "Action: read/write/execute"
        ).required()

        val resource by parser.option(fullName = "resource", shortName = "r",
            type = ArgType.String, description = "Resource path"
        ).required()

        val volume by parser.option(fullName = "volume", shortName = "v",
            type = ArgType.Int, description = "Requested volume"
        ).required()

        return try {
            parser.parse(args)

            val act = Action.fromString(action) ?: return null
            AccessRequest(login, password, resource, act, volume)

        } catch (e: Exception) {
            null
        }
    }
}