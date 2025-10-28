package infrastructure

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64


// Синглтон для безопасного хэширования паролей: генерирует соль, хэширует пароль (SHA-256 + соль), поддерживает Base64-конвертацию
object HashPassword {
    private const val HASH_ALGORITHM = "SHA-256"

    fun hash(password: String, salt: ByteArray): ByteArray {
        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        val passwordBytes = password.toByteArray(Charsets.UTF_8)
        val combined = ByteArray(passwordBytes.size + salt.size)
        System.arraycopy(passwordBytes, 0, combined, 0, passwordBytes.size)
        System.arraycopy(salt, 0, combined, passwordBytes.size, salt.size)

        return digest.digest(combined)
    }

    fun generateSalt(length: Int = 16): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(length)
        random.nextBytes(salt)
        return salt
    }

    fun toBase64(bytes: ByteArray): String {
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun fromBase64(base64String: String): ByteArray {
        return Base64.getDecoder().decode(base64String)
    }
}