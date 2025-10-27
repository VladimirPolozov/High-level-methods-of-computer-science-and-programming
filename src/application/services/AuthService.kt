import user.User

// Implements AuthService: хэширование SHA-256 + соль, возврат User или null

import user.InMemoryUserRepository

class AuthServiceImpl(private val userRepository: InMemoryUserRepository) : AuthService {

    override fun authenticate(login: String, password: String): User? {
        val user = userRepository.findByLogin(login) ?: return null
        val hash = HashPassword.hash(password, user.salt)
        return if (hash.contentEquals(user.passwordHash)) user else null
    }
}