// Интерфейс: checkPermission(user, resource, action): ExitCode (права + наследование)
interface AccessController {
    fun checkPermission(
        user: User,
        resourcePath: String,
        action: String
    ): ExitCode
}