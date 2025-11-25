package application.services

import domain.entities.Action
import domain.enums.ExitCode
import interfaces.AccessController
import domain.entities.User
import infrastructure.adapters.interfaces.ResourceRepository
import org.slf4j.LoggerFactory


// Реализация контроля доступа: проверяет права пользователя на ресурс с учётом наследования от родительских путей

open class AccessControllerImpl(private val resourceRepository: ResourceRepository) : AccessController {

    private val logger = LoggerFactory.getLogger("ACCESS_CTRL")

    override fun checkPermission(
        user: User,
        resourcePath: String,
        action: Action
    ): ExitCode {
        if (resourceRepository.findByPath(resourcePath) == null) {
            logger.warn("Ресурс '$resourcePath' не существует.")
            return ExitCode.FORBIDDEN
        }

        val pathSegments = resourcePath.split('.')

        for (i in pathSegments.size downTo 1) {

            val currentCheckingPath = pathSegments.subList(0, i).joinToString(".")
            logger.debug("Проверка прав для иерархического пути: $currentCheckingPath")

            val allowedActions = user.permissions[currentCheckingPath]

            if (allowedActions != null) {
                logger.debug("Найдено право для '$currentCheckingPath': $allowedActions")

                if (action in allowedActions) {
                    logger.info("Доступ разрешен для $currentCheckingPath: $action (Право найдено в иерархии).")
                    return ExitCode.SUCCESS
                }
            }
        }

        logger.warn("Доступ запрещен для $resourcePath: $action (Права не найдены в иерархии).")
        return ExitCode.FORBIDDEN
    }
}