package app
import kotlinx.cli.*
import user.*
import resources.*
import kotlin.system.exitProcess

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required

fun main(args: Array<String>) {
    val parser = ArgParser("app")

    if (args.contains("-h") || args.contains("--help")) {
        println(
            """
            Supported arguments:
            -l, --login      User login
            -p, --password   User password
            -r, --resource   Resource path
            -a, --action     Action: read, write, execute
            -v, --volume     Resource volume
            -h, --help       Show help
            """.trimIndent()
        )
        exitProcess(1)
    }

    val login by parser.option(
        ArgType.String,
        shortName = "l",
        fullName = "login",
        description = "User login"
    ).required()

    val password by parser.option(
        ArgType.String,
        shortName = "p",
        fullName = "password",
        description = "User password"
    ).required()

    val resourcePath by parser.option(
        ArgType.String,
        shortName = "r",
        fullName = "resource",
        description = "Resource path"
    ).required()

    val action by parser.option(
        ArgType.String,
        shortName = "a",
        fullName = "action",
        description = "Action: read, write, execute"
    ).required()

    val volume by parser.option(
        ArgType.Int,
        shortName = "v",
        fullName = "volume",
        description = "Requested volume"
    ).required()

    try {
        parser.parse(args)
    } catch (e: Exception) {
        println("Неверный формат запуска. Используйте -h для справки.")
        exitProcess(7) // неверный формат
fun main(args: Array<String>) {
    val parser = ArgParser("app")

    val login by parser.option(ArgType.String, description = "User login")
    val password by parser.option(ArgType.String, description = "User password")
    val action by parser.option(ArgType.String, description = "Action: read, write, execute")
    val resourcePath by parser.option(ArgType.String, description = "Resource path (A.B.C)")
    val volume by parser.option(ArgType.Int, description = "Requested volume")
    val help by parser.option(ArgType.Boolean, shortName = "h", description = "Show help")

    try {
        parser.parse(args)
    } catch (e: HelpException) {
        exitProcess(1)
    } catch (e: ParseException) {
        exitProcess(7)
    } catch (e: Exception) {
        exitProcess(7)
    }

    if (help == true) {
        println("Справка по программе:")
        println("--login <user>       Логин пользователя")
        println("--password <pass>    Пароль пользователя")
        println("--action <read/write/execute>   Действие")
        println("--resource <path>    Путь до ресурса")
        println("--volume <num>       Объём запрашиваемого ресурса")
        exitProcess(1)
    }

    val user = users.find { it.login == login } ?: exitProcess(3)

    if (!user.checkPassword(password ?: "")) {
        exitProcess(2)
    }

    val normAction = action?.lowercase() ?: exitProcess(4)
    if (normAction !in listOf("read","write","execute")) exitProcess(4)

    val resource = root.findResource(resourcePath ?: "")
        ?: exitProcess(6)

    if (!user.checkPermission(resourcePath ?: "", normAction)) {
        exitProcess(5)
    }

    val reqVolume = volume ?: exitProcess(7)
    if (reqVolume <= 0) exitProcess(7)
    if (reqVolume > resource.maxVolume) exitProcess(8)

    exitProcess(0)
}