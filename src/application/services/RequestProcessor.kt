package application.services

import domain.dto.AccessRequest
import domain.enums.ExitCode
import infrastructure.adapters.interfaces.ResourceRepository
import interfaces.VolumeValidator

// Композит: process(request: AccessRequest): ExitCode (оркестрация: auth → find → access → volume)

class RequestProcessor(
    private val authService: AuthServiceImpl,
    private val accessController: AccessControllerImpl,
    private val volumeValidator: VolumeValidator,
    private val resourceRepository: ResourceRepository
) {
    fun process(request: AccessRequest): ExitCode {
        val user = authService.authenticate(request.login, request.password)
            ?: return ExitCode.INVALID_PASSWORD

        val resource = resourceRepository.findByPath(request.path)
            ?: return ExitCode.NOT_FOUND

        val accessCode = accessController.checkPermission(user, request.path, request.action.name)
        if (accessCode != ExitCode.SUCCESS) return accessCode

        return volumeValidator.validate(request.volume, resource)
    }
}