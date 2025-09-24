// ввод и проверка консольного ввода
// запуск функций из др дерикторий


fun findResource(path: String, root: Resource): Resource? {
    var current: Resource? = root
    for (name in path.split(".")) {
        current = current?.children?.find { it.name == name } ?: return null
    }
    return current
}

fun hasAccess(user: User, path: String, action: Action): Boolean {
    val parts = path.split(".")
    for (i in parts.size downTo 1) {
        val subPath = parts.take(i).joinToString(".")
        val perm = user.permissions.find { it.resourcePath == subPath }
        if (perm != null && action in perm.actions) return true
    }
    return false
}

// -----------------------------
// Главная функция
// -----------------------------
fun main(args: Array<String>) {
    val parser = ArgParser("app")

    val login by parser.option(ArgType.String, description = "User login")
    val password by parser.option(ArgType.String, description = "User password")
    val actionStr by parser.option(ArgType.String, description = "Action: read, write, execute")
    val resourcePath by parser.option(ArgType.String, description = "Resource path (A.B.C)")
    val volume by parser.option(ArgType.Int, description = "Requested volume")
    val help by parser.option(ArgType.Boolean, shortName = "h", description = "Show help")

    try {
        parser.parse(args)
    } catch (e: Exception) {
        parser.printHelp()
        exitProcess(7) // Неверный формат запуска
    }

    if (help == true) {
        parser.printHelp()
        exitProcess(1) // Запрошена справка
    }

    // Проверка логина
    val user = users.find { it.login == login }
        ?: exitProcess(3)

    // Проверка пароля
    if (!user.checkPassword(password ?: "")) {
        exitProcess(2)
    }

    // Проверка действия
    val action = when (actionStr?.lowercase()) {
        "read" -> Action.READ
        "write" -> Action.WRITE
        "execute" -> Action.EXECUTE
        else -> exitProcess(4)
    }

    // Проверка ресурса
    val resource = findResource(resourcePath ?: "", root) ?: exitProcess(6)

    // Проверка прав доступа
    if (!hasAccess(user, resourcePath ?: "", action)) {
        exitProcess(5)
    }

    // Проверка объёма
    val reqVolume = volume ?: exitProcess(7)
    if (reqVolume <= 0 || reqVolume > resource.maxVolume) {
        if (reqVolume > resource.maxVolume) exitProcess(8)
        else exitProcess(7)
    }

    // Успех
    exitProcess(0)
}