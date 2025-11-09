import application.services.AccessControllerImpl
import domain.entities.Action
import domain.entities.User
import domain.enums.ExitCode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class AccessControllerImplTest {

    private val TEST_RESOURCE_PATH = "A.B.C"
    private val PARENT_RESOURCE_PATH = "A.B"
    private val ROOT_RESOURCE_PATH = "A"

    private val TEST_USER = User(
        login = "test",
        passwordHash = ByteArray(0),
        salt = ByteArray(0),
        permissions = mapOf(
            ROOT_RESOURCE_PATH to setOf(Action.READ, Action.WRITE),
            PARENT_RESOURCE_PATH to setOf(Action.EXECUTE)
        )
    )

    @Test
    fun shouldReturnSuccessWhenPermissionIsFoundOnExactPath() {
        val mockRepo = MockResourceRepository(setOf(TEST_RESOURCE_PATH))
        val controller = AccessControllerImpl(mockRepo)

        val result = controller.checkPermission(TEST_USER, TEST_RESOURCE_PATH, Action.EXECUTE)

        assertEquals(ExitCode.SUCCESS, result, "Разрешение найдено по родительскому пути")
    }

    @Test
    fun shouldReturnSuccessWhenPermissionIsFoundOnRootPath() {
        val mockRepo = MockResourceRepository(setOf(TEST_RESOURCE_PATH))
        val controller = AccessControllerImpl(mockRepo)

        val result = controller.checkPermission(TEST_USER, TEST_RESOURCE_PATH, Action.READ)

        assertEquals(ExitCode.SUCCESS, result, "Разрешение найдено по корневому пути")
    }

    @Test
    fun shouldReturnForbiddenWhenPermissionIsMissing() {
        val mockRepo = MockResourceRepository(setOf(TEST_RESOURCE_PATH))
        val controller = AccessControllerImpl(mockRepo)

        val result = controller.checkPermission(TEST_USER, TEST_RESOURCE_PATH, Action.WRITE)

        assertEquals(ExitCode.FORBIDDEN, result, "Доступ должен быть запрещен, если разрешение не найдено")
    }

    @Test
    fun shouldReturnForbiddenWhenResourceDoesNotExist() {
        val NON_EXISTENT_RESOURCE = "X.Y.Z"
        val mockRepo = MockResourceRepository(emptySet())
        val controller = AccessControllerImpl(mockRepo)

        val result = controller.checkPermission(TEST_USER, NON_EXISTENT_RESOURCE, Action.READ)

        assertEquals(ExitCode.FORBIDDEN, result, "Доступ должен быть запрещен, если ресурс не найден")
    }
}