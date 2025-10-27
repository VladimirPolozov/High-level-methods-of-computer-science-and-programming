// Интерфейс: checkPermission(user, resource, action): ExitCode (права + наследование)

package interfaces

import ExitCode
import user.User

interface AccessController {
    fun checkPermission(
        user: User,
        resourcePath: String,
        action: String
    ): ExitCode
}