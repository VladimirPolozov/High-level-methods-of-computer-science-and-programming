package application.services

import domain.entities.Action
import domain.enums.ExitCode
import interfaces.AccessController
import domain.entities.User
import infrastructure.adapters.interfaces.ResourceRepository


// Реализация контроля доступа: проверяет права пользователя на ресурс с учётом наследования от родительских путей
class AccessControllerImpl(private val resourceRepository: ResourceRepository) : AccessController {
    override fun checkPermission(
        user: User,
        resourcePath: String,
        action: Action
    ): ExitCode {
        if (resourceRepository.findByPath(resourcePath) == null) {
            return ExitCode.FORBIDDEN
        }

        val parts = resourcePath.split('.')
        for (i in parts.size downTo 1) {
            val currentPath = parts.subList(0, i).joinToString(".")

            val allowedActions = user.permissions[currentPath]
            if (allowedActions != null && action in allowedActions) {
                return ExitCode.SUCCESS
            }
        }

        return ExitCode.FORBIDDEN
    }
}