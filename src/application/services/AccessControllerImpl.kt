package application.services

import domain.enums.ExitCode
import interfaces.AccessController
import domain.entities.User
import infrastructure.adapters.repositories.InMemoryResourceRepository

// Implements AccessController: навигация по parent, сбор inherited permissions

class AccessControllerImpl(resourceRepo: InMemoryResourceRepository) : AccessController {
    override fun checkPermission(
        user: User,
        resourcePath: String,
        action: String
    ): ExitCode {

    }
}