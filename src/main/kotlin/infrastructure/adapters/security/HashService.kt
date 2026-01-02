package main.kotlin.infrastructure.adapters.security

import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64


// Синглтон для безопасного хэширования паролей: генерирует соль, хэширует пароль (SHA-256 + соль), поддерживает Base64-конвертацию
@Component
class HashService {
    private val HASH_ALGORITHM = "SHA-256"

    fun hash(password: String, salt: ByteArray): ByteArray {
        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        val passwordBytes = password.toByteArray(Charsets.UTF_8)

        val combined = passwordBytes + salt

        return digest.digest(combined)
    }

    fun generateSalt(length: Int = 16): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(length)
        random.nextBytes(salt)
        return salt
    }
}