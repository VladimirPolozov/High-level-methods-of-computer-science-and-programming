package user

import java.security.MessageDigest
import java.security.SecureRandom

// добавить права доступа к ресурсам
class User(val login: String, val password: String) {
    val passwordHash: ByteArray
    val salt: ByteArray = ByteArray(16).also { SecureRandom().nextBytes(it) }

    private val permissions = mutableListOf<Pair<String, MutableSet<String>>>()

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

    fun setPermission(path: String, action: String) {
        val action = action.lowercase()
        val entry = permissions.find { it.first == path }
        if (entry != null) {
            entry.second.add(action)
        } else {
            permissions.add(path to mutableSetOf(action))
        }
    }

    fun checkPermission(path: String, action: String): Boolean {
        val action = action.lowercase()
        val parts = path.split('.')
        val sb = StringBuilder()
        for (i in parts.indices) {
            if (i > 0) sb.append('.')
            sb.append(parts[i])
            val prefix = sb.toString()
            val entry = permissions.find { it.first == prefix }
            if (entry != null && action in entry.second) {
                return true
            }
        }
        return false
    }
}