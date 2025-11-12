package tests

import application.services.AuthServiceImpl
import application.services.AccessControllerImpl
import application.services.RequestProcessor
import domain.dto.AccessRequest
import domain.entities.Action
import domain.entities.User
import domain.enums.ExitCode
import domain.entities.Resource
import infrastructure.adapters.interfaces.ResourceRepository
import interfaces.VolumeValidator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class MockAuthService(
    private val userToReturn: User?
) : AuthServiceImpl(MockUserRepository("any", null)) {
    override fun authenticate(login: String, password: String): User? = userToReturn
}


class MockAccessController(
    private val resultCode: ExitCode
) : AccessControllerImpl(MockResourceRepository(emptySet())) {
    override fun checkPermission(user: User, resourcePath: String, action: Action): ExitCode = resultCode
}

class MockVolumeValidator(
    private val resultCode: ExitCode
) : VolumeValidator {
    override fun validate(volume: Int, resource: Resource): ExitCode = resultCode
}


class MockResourceRepositoryForProcessor(
    private val resourceToReturn: Resource?
) : ResourceRepository {
    override fun findByPath(path: String): Resource? = resourceToReturn
}

class RequestProcessorTest {

    private val VALID_USER = User("tests", ByteArray(0), ByteArray(0), emptyMap())
    private val VALID_RESOURCE = Resource("test.resource", 100)
    private val VALID_REQUEST = AccessRequest("tests", "pass", "test.resource", Action.READ, 50)

    @Test
    fun shouldReturnSuccessCodeOnFullValidRequest() {
        val authMock = MockAuthService(VALID_USER)
        val resourceRepoMock = MockResourceRepositoryForProcessor(VALID_RESOURCE)
        val accessMock = MockAccessController(ExitCode.SUCCESS)
        val volumeMock = MockVolumeValidator(ExitCode.SUCCESS)

        val processor = RequestProcessor(authMock, accessMock, volumeMock, resourceRepoMock)

        val result = processor.process(VALID_REQUEST)

        assertEquals(ExitCode.SUCCESS, result, "Полный успешный сценарий должен возвращать SUCCESS")
    }

    @Test
    fun shouldStopAndReturnInvalidPasswordOnAuthFailure() {

        val authMock = MockAuthService(null)

        val resourceRepoMock = MockResourceRepositoryForProcessor(VALID_RESOURCE)
        val accessMock = MockAccessController(ExitCode.SUCCESS)
        val volumeMock = MockVolumeValidator(ExitCode.SUCCESS)

        val processor = RequestProcessor(authMock, accessMock, volumeMock, resourceRepoMock)

        val result = processor.process(VALID_REQUEST)

        assertEquals(ExitCode.INVALID_PASSWORD, result, "Должен вернуться INVALID_PASSWORD при ошибке аутентификации")
    }

    @Test
    fun shouldStopAndReturnNotFoundWhenResourceIsMissing() {

        val authMock = MockAuthService(VALID_USER)
        val resourceRepoMock = MockResourceRepositoryForProcessor(null) // <-- Ключевой мок: Ресурс не найден

        val accessMock = MockAccessController(ExitCode.SUCCESS)
        val volumeMock = MockVolumeValidator(ExitCode.SUCCESS)

        val processor = RequestProcessor(authMock, accessMock, volumeMock, resourceRepoMock)

        val result = processor.process(VALID_REQUEST)

        assertEquals(ExitCode.NOT_FOUND, result, "Должен вернуться NOT_FOUND, если ресурс не существует")
    }

    @Test
    fun shouldStopAndReturnForbiddenOnAccessDenied() {

        val authMock = MockAuthService(VALID_USER)
        val resourceRepoMock = MockResourceRepositoryForProcessor(VALID_RESOURCE)
        val accessMock = MockAccessController(ExitCode.FORBIDDEN)

        val volumeMock = MockVolumeValidator(ExitCode.SUCCESS)

        val processor = RequestProcessor(authMock, accessMock, volumeMock, resourceRepoMock)

        val result = processor.process(VALID_REQUEST)

        assertEquals(ExitCode.FORBIDDEN, result, "Должен вернуться FORBIDDEN при отказе в доступе")
    }

    @Test
    fun shouldReturnVolumeErrorCodeOnValidationFailure() {
        val authMock = MockAuthService(VALID_USER)
        val resourceRepoMock = MockResourceRepositoryForProcessor(VALID_RESOURCE)
        val accessMock = MockAccessController(ExitCode.SUCCESS)
        val volumeMock = MockVolumeValidator(ExitCode.EXCEED_MAX_VOLUME)

        val processor = RequestProcessor(authMock, accessMock, volumeMock, resourceRepoMock)

        val result = processor.process(VALID_REQUEST)

        assertEquals(ExitCode.EXCEED_MAX_VOLUME, result, "Должна вернуться ошибка валидации объема")
    }
}