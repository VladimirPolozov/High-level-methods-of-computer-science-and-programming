package user

import java.security.MessageDigest
import java.security.SecureRandom

// Действия с документом
enum class Action { READ, WRITE, EXECUTE }
// Права доступа к документу
data class Permission(
    val resourcePath: String,
    val actions: Set<Action>
)
// добавить права доступа к ресурсам
class User(
    val login: String,
    val password: String,
    val permissions: List<Permission>
) {
    private val passwordHash: ByteArray
    val salt: ByteArray = ByteArray(16).also { SecureRandom().nextBytes(it) }

    init {
        passwordHash = hashPasswordWithSalt(password, salt)
        val login = login
    }

    private fun hashPasswordWithSalt(password: String, salt: ByteArray): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")
        val passwordBytes = password.toByteArray(Charsets.UTF_8)
        md.update(salt) // солим пароль
        md.update(passwordBytes)
        return md.digest()
    }

    fun checkPassword(attempt: String): Boolean {
        val attemptHash = hashPasswordWithSalt(attempt, salt)
        return MessageDigest.isEqual(passwordHash, attemptHash)
    }
}