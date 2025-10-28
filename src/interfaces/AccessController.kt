package interfaces

import domain.entities.Action
import domain.enums.ExitCode
import domain.entities.User


// Интерфейс контроля доступа: проверяет, может ли пользователь выполнить действие над ресурсом с учётом наследования прав
interface AccessController {
    fun checkPermission(
        user: User,
        resourcePath: String,
        action: Action
    ): ExitCode
}