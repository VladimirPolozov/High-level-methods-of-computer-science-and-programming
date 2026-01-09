package application.services

import domain.entities.User
import domain.exceptions.InvalidLoginException
import domain.exceptions.InvalidPasswordException
import domain.repository.UserRepository
import infrastructure.adapters.security.HashService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class AuthServiceImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var hashService: HashService
    private lateinit var authService: AuthServiceImpl

    private val TEST_LOGIN = "test_user"
    private val TEST_PASSWORD = "test_password"
    private val TEST_SALT = "some_salt".toByteArray()
    private val TEST_HASH = "correct_hash".toByteArray()

    private val existingUser = User(
        login = TEST_LOGIN,
        passwordHash = TEST_HASH,
        salt = TEST_SALT,
        permissions = emptyMap()
    )

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        hashService = mock(HashService::class.java)
        authService = AuthServiceImpl(userRepository, hashService)
    }

    @Test
    fun `should authenticate successfully with correct credentials`() {
        `when`(userRepository.findByLogin(TEST_LOGIN)).thenReturn(existingUser)
        `when`(hashService.hash(TEST_PASSWORD, TEST_SALT)).thenReturn(TEST_HASH)

        val result = authService.authenticate(TEST_LOGIN, TEST_PASSWORD)

        assertNotNull(result)
        assertEquals(TEST_LOGIN, result.login)
    }

    @Test
    fun `should throw InvalidLoginException when user not found`() {
        `when`(userRepository.findByLogin("unknown")).thenReturn(null)

        assertThrows(InvalidLoginException::class.java) {
            authService.authenticate("unknown", TEST_PASSWORD)
        }
    }

    @Test
    fun `should throw InvalidPasswordException with incorrect password`() {
        val wrongPassword = "wrong_password"
        val wrongHash = "wrong_hash".toByteArray()

        `when`(userRepository.findByLogin(TEST_LOGIN)).thenReturn(existingUser)
        `when`(hashService.hash(wrongPassword, TEST_SALT)).thenReturn(wrongHash)

        assertThrows(InvalidPasswordException::class.java) {
            authService.authenticate(TEST_LOGIN, wrongPassword)
        }
    }
}