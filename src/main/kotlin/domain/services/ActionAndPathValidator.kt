package domain.services

import domain.dto.AccessRequest
import domain.enums.Action

interface ActionAndPathValidator {

    fun actionValidate(request: AccessRequest): Action?

    fun pathValidate(request: AccessRequest): String?
}