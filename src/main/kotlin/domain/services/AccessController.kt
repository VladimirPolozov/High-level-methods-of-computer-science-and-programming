package main.kotlin.domain.services

import main.kotlin.domain.enums.Action
import main.kotlin.domain.entities.User
import main.kotlin.domain.enums.ExitCode

interface AccessController {
    fun checkPermission(
        user: User,
        resourcePath: String,
        action: Action
    ): ExitCode
}