package tests

import application.services.AuthServiceImpl
import domain.entities.User
import infrastructure.HashPassword
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull

class AuthServiceImplTest {

    private val TEST_LOGIN = "test_user"
    private val TEST_PASSWORD = "test_password"
    private val TEST_SALT = HashPassword.generateSalt()
    private val TEST_HASH = HashPassword.hash(TEST_PASSWORD, TEST_SALT)

    private val existingUser = User(
        login = TEST_LOGIN,
        passwordHash = TEST_HASH,
        salt = TEST_SALT,
        permissions = emptyMap()
    )

    @Test
    fun shouldAuthenticateSuccessfullyWithCorrectCredentials() {
        val mockRepo = MockUserRepository(TEST_LOGIN, existingUser)
        val authService = AuthServiceImpl(mockRepo)

        val user = authService.authenticate(TEST_LOGIN, TEST_PASSWORD)
        assertNotNull(user, "Аутентификация должна быть успешной")

        val password = "test_password"
        val salt = HashPassword.generateSalt(16)
        val hash1 = HashPassword.hash(password, salt)
        val hash2 = HashPassword.hash(password, salt)

        assertEquals(hash1, hash2, "Хэш должен быть детерминированным")
    }

    @Test
    fun shouldReturnNullWhenUserNotFound() {
        val mockRepo = MockUserRepository("another_user", existingUser)
        val authService = AuthServiceImpl(mockRepo)

        val user = authService.authenticate(TEST_LOGIN, TEST_PASSWORD)

        assertNull(user, "Должен быть возвращен null, если пользователь не найден")
    }

    @Test
    fun shouldReturnNullWithIncorrectPassword() {
        val mockRepo = MockUserRepository(TEST_LOGIN, existingUser)
        val authService = AuthServiceImpl(mockRepo)
        val incorrectPassword = "wrong_password"

        val user = authService.authenticate(TEST_LOGIN, incorrectPassword)

        assertNull(user, "Должен быть возвращен null при неверном пароле")
    }
}