package user

import java.security.MessageDigest
import java.security.SecureRandom

//data class Permissions(
//    val path: String,
//    val actions: Set<String>
//)

//class User(
//    val login: String,
//    val password: String,
//    val permissions: List<Permissions> = emptyList()) {
//
//    val passwordHash: ByteArray
//    val salt: ByteArray = ByteArray(16).also { SecureRandom().nextBytes(it) }
//
//    init {
//        passwordHash = hashPasswordWithSalt(password, salt)
//        val login = login
//    }
//
//    private fun hashPasswordWithSalt(password: String, salt: ByteArray): ByteArray {
//        val md = MessageDigest.getInstance("SHA-256")
//        val passwordBytes = password.toByteArray(Charsets.UTF_8)
//        md.update(salt) // солим пароль
//        md.update(passwordBytes)
//        return md.digest()
//    }
//
//    fun checkPassword(attempt: String): Boolean {
//        val attemptHash = hashPasswordWithSalt(attempt, salt)
//        return MessageDigest.isEqual(passwordHash, attemptHash)
//    }
//
//    fun checkPermission(path: String, action: String): Boolean {
//        val normAction = action.lowercase()
//        val parts = path.split('.')
//        val sb = StringBuilder()
//        for (i in parts.indices) {
//            if (i > 0) sb.append('.')
//            sb.append(parts[i])
//            val prefix = sb.toString()
//
//            if (permissions.any {
//                    it.path == prefix && normAction in it.actions.map { a -> a.lowercase() }
//                }) {
//                return true
//            }
//        }
//        return false
//    }
//}