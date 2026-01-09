package infrastructure.adapters.security

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class HashServiceTest {

    private val hashService = HashService() // Создаем экземпляр вручную для Unit-теста

    @Test
    fun `should generate salt of correct length`() {
        val expectedLength = 16
        val salt = hashService.generateSalt(expectedLength)
        assertEquals(expectedLength, salt.size)
    }

    @Test
    fun `should generate different salts on multiple calls`() {
        val salt1 = hashService.generateSalt()
        val salt2 = hashService.generateSalt()
        assertNotEquals(salt1.toList(), salt2.toList(), "Сгенерированные соли должны быть разными")
    }

    @Test
    fun `should return same hash for same password and salt`() {
        val password = "test_password"
        val salt = hashService.generateSalt(10)

        val hash1 = hashService.hash(password, salt)
        val hash2 = hashService.hash(password, salt)

        assertArrayEquals(hash1, hash2, "Хэш должен быть детерминированным")
    }

    @Test
    fun `should return different hash for different salts`() {
        val password = "test_password"
        val salt1 = hashService.generateSalt(10)
        val salt2 = hashService.generateSalt(10)

        val hash1 = hashService.hash(password, salt1)
        val hash2 = hashService.hash(password, salt2)

        assertNotEquals(hash1.toList(), hash2.toList(), "Разные соли должны давать разные хэши")
    }
}