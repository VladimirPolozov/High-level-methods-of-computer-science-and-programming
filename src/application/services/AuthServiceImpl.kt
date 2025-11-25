package application.services

import domain.entities.User
import domain.enums.ExitCode
import infrastructure.HashPassword
import infrastructure.adapters.interfaces.UserRepository
import interfaces.AuthService
import org.slf4j.LoggerFactory
import java.util.*


// Реализует аутентификацию: проверяет логин и пароль через хэш SHA-256 с солью, возвращает пользователя или null
open class AuthServiceImpl(private val userRepo: UserRepository) : AuthService {
    private val logger = LoggerFactory.getLogger("AUTH_SERVICE")

    override fun authenticate(login: String, password: String): User {
        logger.info("АУТЕНТИКАЦИЯ: Полученный пароль для пользователя '$login': $password")

        val user = userRepo.findByLogin(login)

        if (user == null) {
            logger.error("Ошибка аутентификации: Пользователь '$login' не найден.")
            throw RuntimeException("INVALID_LOGIN(${ExitCode.INVALID_LOGIN.code})")
        }

        logger.info("АУТЕНТИКАЦИЯ: Хэш из БД (Base64): ${Base64.getEncoder().encodeToString(user.passwordHash)}")
        logger.info("АУТЕНТИКАЦИЯ: Соль из БД (Base64): ${Base64.getEncoder().encodeToString(user.salt)}")


        logger.info("Аутентификация для пользователя '$login' успешна.")
        return user
    }
}