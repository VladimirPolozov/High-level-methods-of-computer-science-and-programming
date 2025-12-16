package application.services

import domain.dto.AccessRequest
import domain.entities.User
import domain.enums.Action
import domain.enums.ExitCode
import domain.exceptions.InvalidLoginException
import domain.exceptions.InvalidPasswordException
import domain.repository.ResourceRepository

import org.slf4j.LoggerFactory
import domain.services.AccessController
import domain.services.ActionAndPathValidator
import domain.services.AuthService
import domain.services.VolumeValidator

/**
 * Оркестратор: обрабатывает запрос, последовательно выполняя аутентификацию,
 * контроль доступа и валидацию объёма.
 * * Класс зависит только от интерфейсов (DIP).
 */
class RequestProcessor(
    private val authService: AuthService,
    private val accessController: AccessController,
    private val volumeValidator: VolumeValidator,
    private val resourceRepository: ResourceRepository,
    private val actionAndPathValidator: ActionAndPathValidator
) {
    private val logger = LoggerFactory.getLogger("REQUEST_PROCESSOR")

    fun process(request: AccessRequest): ExitCode {

        // Действие
        val action: Action? = actionAndPathValidator.actionValidate(request)
        if (action == null) {
            logger.warn("Request denied: Invalid format for action or resource path.")
            return ExitCode.UNKNOWN_ACTION
        }

        // Проверка пути ресурса
        if (actionAndPathValidator.pathValidate(request) == null) {
            logger.warn("Request denied: Invalid format for resource path (${request.path}).")
            return ExitCode.INVALID_FORMAT
        }

        // Проверка логина и пароля пользователя
        val user: User
        try {
            user = authService.authenticate(request.login, request.password)
        } catch (e: InvalidLoginException) {
            logger.warn("Request denied: Invalid login attempt for user '${request.login}'")
            return ExitCode.INVALID_LOGIN
        } catch (e: InvalidPasswordException) {
            logger.warn("Request denied: Invalid password for user '${request.login}'")
            return ExitCode.INVALID_PASSWORD
        }
        logger.info("Authentication successful for user '${user.login}'. Processing access request...")

        // Ресурс
        val resource = resourceRepository.findByPath(request.path)
        if (resource == null) {
            logger.warn("Request denied: Resource path '${request.path}' not found.")
            return ExitCode.NOT_FOUND
        }

        // Доступ
        val accessCode = accessController.checkPermission(user, request.path, action)
        if (accessCode != ExitCode.SUCCESS) {
            logger.warn("Access denied for user '${user.login}' to '${request.path}' (${request.action}). Code: ${accessCode.code}")
            return accessCode
        }

        // Volume ресурса
        val volumeCode = volumeValidator.validate(request.volume, resource)
        if (volumeCode != ExitCode.SUCCESS) {
            logger.warn("Volume exceeded for '${request.path}'. Requested: ${request.volume}, Max: ${resource.maxVolume}. Code: ${volumeCode.code}")
            return volumeCode
        }

        logger.info("Access granted for user '${user.login}' to '${request.path}' (${request.action}) with volume ${request.volume}.")
        return ExitCode.SUCCESS
    }
}