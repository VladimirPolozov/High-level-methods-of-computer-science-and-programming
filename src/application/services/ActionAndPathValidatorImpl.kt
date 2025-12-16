package application.services

import domain.dto.AccessRequest
import domain.enums.Action
import domain.services.ActionAndPathValidator

class ActionAndPathValidatorImpl() : ActionAndPathValidator {

    private val RESOURCE_PATH_REGEX = "^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*\$".toRegex()

    override fun actionValidate(request: AccessRequest): Action? {
        val actionString = request.action

        val validatedAction: Action = Action.fromString(actionString.uppercase())
            ?: return null

        return validatedAction
    }

    override fun pathValidate(request: AccessRequest): String? {
        val resourcePath = request.path

        if (resourcePath.isEmpty() || !resourcePath.matches(RESOURCE_PATH_REGEX)) {
            return null
        }

        return resourcePath
    }
}