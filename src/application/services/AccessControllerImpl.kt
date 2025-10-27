// Implements AccessController: навигация по parent, сбор inherited permissions

package application.services

import ExitCode
import interfaces.AccessController
import user.User

class AccessControllerImpl : AccessController {
    override fun checkPermission(
        user: User,
        resourcePath: String,
        action: String
    ): ExitCode {

    }
}