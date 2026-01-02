package application.services

import main.kotlin.application.services.RequestProcessor
import main.kotlin.domain.entities.Resource
import main.kotlin.domain.entities.User
import main.kotlin.domain.enums.Action
import main.kotlin.domain.enums.ExitCode
import main.kotlin.domain.dto.AccessRequest
import main.kotlin.domain.exceptions.InvalidLoginException
import main.kotlin.domain.exceptions.InvalidPasswordException
import main.kotlin.domain.repository.ResourceRepository
import main.kotlin.domain.services.AccessController
import main.kotlin.domain.services.AuthService
import main.kotlin.domain.services.VolumeValidator
import main.kotlin.domain.services.ActionAndPathValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class RequestProcessorTest {

    private lateinit var authService: AuthService
    private lateinit var accessController: AccessController
    private lateinit var volumeValidator: VolumeValidator
    private lateinit var resourceRepository: ResourceRepository
    private lateinit var actionAndPathValidator: ActionAndPathValidator
    private lateinit var requestProcessor: RequestProcessor

    private val VALID_USER = User("tests", byteArrayOf(), byteArrayOf(), emptyMap())
    private val VALID_RESOURCE = Resource("test.resource", 100)
    private val VALID_REQUEST = AccessRequest("tests", "pass", "A.B.C", "READ", 50)

    @BeforeEach
    fun setUp() {
        authService = mock(AuthService::class.java)
        accessController = mock(AccessController::class.java)
        volumeValidator = mock(VolumeValidator::class.java)
        resourceRepository = mock(ResourceRepository::class.java)
        actionAndPathValidator = mock(ActionAndPathValidator::class.java)

        requestProcessor = RequestProcessor(
            authService,
            accessController,
            volumeValidator,
            resourceRepository,
            actionAndPathValidator
        )
    }

    @Test
    fun `should return success for a fully valid request`() {
        whenever(actionAndPathValidator.actionValidate(any())).thenReturn(Action.READ)
        whenever(actionAndPathValidator.pathValidate(any())).thenReturn("A.B.C")
        whenever(authService.authenticate(any(), any())).thenReturn(VALID_USER)
        whenever(resourceRepository.findByPath(any())).thenReturn(VALID_RESOURCE)
        whenever(accessController.checkPermission(any(), any(), any())).thenReturn(ExitCode.SUCCESS)
        whenever(volumeValidator.validate(any(), any())).thenReturn(ExitCode.SUCCESS)

        val result = requestProcessor.process(VALID_REQUEST)
        assertEquals(ExitCode.SUCCESS, result)
    }

    @Test
    fun `should return invalid login when auth service throws invalid login exception`() {
        whenever(actionAndPathValidator.actionValidate(any())).thenReturn(Action.READ)
        whenever(actionAndPathValidator.pathValidate(any())).thenReturn("A.B.C")

        doThrow(InvalidLoginException()).whenever(authService).authenticate(any(), any())

        val result = requestProcessor.process(VALID_REQUEST)
        assertEquals(ExitCode.INVALID_LOGIN, result)
    }

    @Test
    fun `should return invalid password when auth service throws invalid password exception`() {
        whenever(actionAndPathValidator.actionValidate(any())).thenReturn(Action.READ)
        whenever(actionAndPathValidator.pathValidate(any())).thenReturn("A.B.C")

        doThrow(InvalidPasswordException()).whenever(authService).authenticate(any(), any())

        val result = requestProcessor.process(VALID_REQUEST)
        assertEquals(ExitCode.INVALID_PASSWORD, result)
    }

    @Test
    fun `should return not found when resource does not exist in repository`() {
        whenever(actionAndPathValidator.actionValidate(any())).thenReturn(Action.READ)
        whenever(actionAndPathValidator.pathValidate(any())).thenReturn("A.B.C")
        whenever(authService.authenticate(any(), any())).thenReturn(VALID_USER)
        whenever(resourceRepository.findByPath(any())).thenReturn(null)

        val result = requestProcessor.process(VALID_REQUEST)
        assertEquals(ExitCode.NOT_FOUND, result)
    }

    @Test
    fun `should return forbidden when access controller denies access`() {
        whenever(actionAndPathValidator.actionValidate(any())).thenReturn(Action.READ)
        whenever(actionAndPathValidator.pathValidate(any())).thenReturn("A.B.C")
        whenever(authService.authenticate(any(), any())).thenReturn(VALID_USER)
        whenever(resourceRepository.findByPath(any())).thenReturn(VALID_RESOURCE)
        whenever(accessController.checkPermission(any(), any(), any())).thenReturn(ExitCode.FORBIDDEN)

        val result = requestProcessor.process(VALID_REQUEST)
        assertEquals(ExitCode.FORBIDDEN, result)
    }
}