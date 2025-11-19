package application.services

import domain.entities.User
import infrastructure.HashPassword
import infrastructure.adapters.interfaces.UserRepository
import interfaces.AuthService


// Реализует аутентификацию: проверяет логин и пароль через хэш SHA-256 с солью, возвращает пользователя или null
open class AuthServiceImpl(private val userRepository: UserRepository) : AuthService {

    override fun authenticate(login: String, password: String): User? {
        val user = userRepository.findByLogin(login) ?: return null
        val hash = HashPassword.hash(password, user.salt)
        return if (hash.contentEquals(user.passwordHash)) user else null
    }
}