// Интерфейс: checkPermission(user, resource, action): ExitCode (права + наследование)

package interfaces

interface AccessController {
    fun checkPermission(
        user: User,
        resourcePath: String,
        action: String
    ): ExitCode
}