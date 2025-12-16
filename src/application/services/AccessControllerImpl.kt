package application.services

import domain.enums.Action
import domain.enums.ExitCode
import domain.entities.User
import domain.repository.ResourceRepository
import domain.services.AccessController
import org.slf4j.LoggerFactory

// Реализация контроля доступа: проверяет права пользователя на ресурс с учётом наследования от родительских путей

open class AccessControllerImpl() : AccessController {

    private val logger = LoggerFactory.getLogger("ACCESS_CTRL")

    override fun checkPermission(
        user: User,
        resourcePath: String,
        action: Action
    ): ExitCode {

        val pathSegments = resourcePath.split('.')

        for (i in pathSegments.size downTo 1) {

            val currentCheckingPath = pathSegments.subList(0, i).joinToString(".")
            logger.debug("Verifying permissions for a hierarchical path: $currentCheckingPath")

            val allowedActions = user.permissions[currentCheckingPath]

            if (allowedActions != null) {
                logger.debug("The right was found for '$currentCheckingPath': $allowedActions")

                if (action in allowedActions) {
                    logger.info("Access is allowed for $currentCheckingPath: $action (The right is found in the hierarchy).")
                    return ExitCode.SUCCESS
                }
            }
        }

        logger.warn("Access is denied to $resourcePath: $action (Rights not found in the hierarchy).")
        return ExitCode.FORBIDDEN
    }
}