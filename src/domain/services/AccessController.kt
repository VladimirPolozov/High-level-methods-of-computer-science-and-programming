package domain.services

import domain.enums.Action
import domain.entities.User
import domain.enums.ExitCode

interface AccessController {
    fun checkPermission(
        user: User,
        resourcePath: String,
        action: Action
    ): ExitCode
}