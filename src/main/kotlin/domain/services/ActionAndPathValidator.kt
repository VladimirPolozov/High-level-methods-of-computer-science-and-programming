package main.kotlin.domain.services

import main.kotlin.domain.dto.AccessRequest
import main.kotlin.domain.enums.Action

interface ActionAndPathValidator {

    fun actionValidate(request: AccessRequest): Action?

    fun pathValidate(request: AccessRequest): String?
}