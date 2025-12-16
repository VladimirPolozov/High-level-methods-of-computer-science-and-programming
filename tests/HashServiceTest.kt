package tests

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import infrastructure.adapters.security.HashService
import org.junit.jupiter.api.Assertions.assertNotEquals

class HashServiceTest {

    @Test
    fun shouldGenerateSaltOfCorrectLength() {
        val expectedLength = 16
        val salt = HashService.generateSalt(expectedLength)
        assertEquals(expectedLength, salt.size)
    }

    @Test
    fun shouldGenerateDifferentSaltsOnMultipleCalls() {
        val salt1 = HashService.generateSalt()
        val salt2 = HashService.generateSalt()
        assertNotEquals(salt1, salt2, "Сгенерированные соли должны быть разными")
    }

    @Test
    fun shouldReturnSameHashForSamePasswordAndSalt() {
        val password = "test_password"
        val salt = HashService.generateSalt(10)
        val hash1 = HashService.hash(password, salt)
        val hash2 = HashService.hash(password, salt)

        assertEquals(hash1, hash2, "Хэш должен быть детерминированным")
    }

    @Test
    fun shouldReturnDifferentHashForDifferentSalts() {
        val password = "test_password"
        val salt1 = HashService.generateSalt(10)
        val salt2 = HashService.generateSalt(10)
        val hash1 = HashService.hash(password, salt1)
        val hash2 = HashService.hash(password, salt2)

        assertNotEquals(hash1, hash2, "Разные соли должны давать разные хэши")
    }

}