package domain.services

import domain.enums.Action
import domain.entities.User
import main.kotlin.domain.enums.ExitCode

interface AccessController {
    fun checkPermission(
        user: User,
        resourcePath: String,
        action: Action
    ): ExitCode
}