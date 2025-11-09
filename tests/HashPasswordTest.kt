import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertContentEquals
import org.junit.jupiter.api.assertEquals
import org.junit.jupiter.api.assertNotEquals
import java.nio.charset.StandardCharsets
import infrastructure.HashPassword

class HashPasswordTest {

    @Test
    fun shouldGenerateSaltOfCorrectLength() {
        val expectedLength = 16
        val salt = HashPassword.generateSalt(expectedLength)
        assertEquals(expectedLength, salt.size)
    }

    @Test
    fun shouldGenerateDifferentSaltsOnMultipleCalls() {
        val salt1 = HashPassword.generateSalt()
        val salt2 = HashPassword.generateSalt()
        assertNotEquals(salt1, salt2, "Сгенерированные соли должны быть разными")
    }

    @Test
    fun shouldReturnSameHashForSamePasswordAndSalt() {
        val password = "test_password"
        val salt = HashPassword.generateSalt(10)
        val hash1 = HashPassword.hash(password, salt)
        val hash2 = HashPassword.hash(password, salt)

        assertContentEquals(hash1, hash2, "Хэш должен быть детерминированным")
    }

    @Test
    fun shouldReturnDifferentHashForDifferentSalts() {
        val password = "test_password"
        val salt1 = HashPassword.generateSalt(10)
        val salt2 = HashPassword.generateSalt(10)
        val hash1 = HashPassword.hash(password, salt1)
        val hash2 = HashPassword.hash(password, salt2)

        assertNotEquals(hash1, hash2, "Разные соли должны давать разные хэши")
    }

}