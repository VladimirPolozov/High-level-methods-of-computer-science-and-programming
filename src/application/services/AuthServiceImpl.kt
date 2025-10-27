package application.services

import domain.entities.User
import infrastructure.HashPassword
import infrastructure.adapters.interfaces.UserRepository
import interfaces.AuthService


// Implements AuthService: хэширование SHA-256 + соль, возврат User или null
class AuthServiceImpl(private val userRepository: UserRepository) : AuthService {

    override fun authenticate(login: String, password: String): User? {
        val user = userRepository.findByLogin(login) ?: return null
        val hash = HashPassword.hash(password, user.salt)
        return if (hash.contentEquals(user.passwordHash)) user else null
    }
}