// Implements AccessController: навигация по parent, сбор inherited permissions

package application.services

import interfaces.AccessController

class AccessController : AccessController {
    override fun checkPermission(
        user: User,
        resourcePath: String,
        action: String
    ): ExitCode
}