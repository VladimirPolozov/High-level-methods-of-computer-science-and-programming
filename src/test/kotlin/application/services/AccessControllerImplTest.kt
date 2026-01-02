package application.services

import main.kotlin.application.services.AccessControllerImpl
import main.kotlin.domain.entities.User
import main.kotlin.domain.enums.Action
import main.kotlin.domain.enums.ExitCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AccessControllerTest {

    private val accessController = AccessControllerImpl()

    private val testUser = User(
        login = "admin",
        passwordHash = byteArrayOf(),
        salt = byteArrayOf(),
        permissions = mapOf(
            "A" to setOf(Action.READ),
            "A.B" to setOf(Action.EXECUTE)
        )
    )

    @Test
    fun `should allow access when parent has permission`() {
        val result = accessController.checkPermission(
            testUser,
            "A.B.C",
            Action.EXECUTE
        )
        assertEquals(ExitCode.SUCCESS, result)
    }

    @Test
    fun `should allow access when root has permission`() {
        val result = accessController.checkPermission(
            testUser,
            "A.B.C.D",
            Action.READ
        )
        assertEquals(ExitCode.SUCCESS, result)
    }

    @Test
    fun `should deny access when no permission in hierarchy`() {
        val result = accessController.checkPermission(
            testUser,
            "A.B.C",
            Action.WRITE
        )
        assertEquals(ExitCode.FORBIDDEN, result)
    }

    @Test
    fun `should deny access for unrelated path`() {
        val result = accessController.checkPermission(
            testUser,
            "X.Y.Z",
            Action.READ
        )
        assertEquals(ExitCode.FORBIDDEN, result)
    }
}