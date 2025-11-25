package application.services

import domain.dto.AccessRequest
import domain.enums.ExitCode
import infrastructure.adapters.interfaces.ResourceRepository
import interfaces.VolumeValidator
import org.slf4j.LoggerFactory


// Оркестратор: обрабатывает запрос, последовательно выполняя аутентификацию, проверку существования ресурса, контроль доступа и валидацию объёма
class RequestProcessor(
    private val authService: AuthServiceImpl,
    private val accessController: AccessControllerImpl,
    private val volumeValidator: VolumeValidator,
    private val resourceRepository: ResourceRepository
) {
    fun process(request: AccessRequest): ExitCode {
        val logger = LoggerFactory.getLogger("DB")
        val user = authService.authenticate(request.login, request.password)
            ?: return ExitCode.INVALID_PASSWORD

        val login = request.login
        val pass = request.password
        logger.info("$login")
        logger.info("$pass")


        val resource = resourceRepository.findByPath(request.path)
            ?: return ExitCode.NOT_FOUND

        val accessCode = accessController.checkPermission(user, request.path, request.action)
        if (accessCode != ExitCode.SUCCESS) return accessCode

        return volumeValidator.validate(request.volume, resource)
    }
}