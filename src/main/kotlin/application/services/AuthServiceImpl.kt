package main.kotlin.application.services

import main.kotlin.domain.entities.User
import main.kotlin.domain.exceptions.InvalidLoginException
import main.kotlin.domain.exceptions.InvalidPasswordException
import main.kotlin.domain.repository.UserRepository
import main.kotlin.domain.services.AuthService
import main.kotlin.infrastructure.adapters.security.HashService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
open class AuthServiceImpl(
    private val userRepo: UserRepository,
    private val hashService: HashService
) : AuthService {

    private val logger = LoggerFactory.getLogger("AuthService")

    override fun authenticate(login: String, password: String): User {

        val user = userRepo.findByLogin(login)
        if (user == null) {
            logger.error("Authentication error: User '$login' was not found.")
            throw InvalidLoginException()
        }

        val salt = user.salt
        val passwordHash = user.passwordHash

        if (salt == null || passwordHash == null) {
            logger.error("Authentication error: Password/salt not initialized for user '$login'.")
            throw InvalidPasswordException()
        }

        val hashedPassword = hashService.hash(password, salt)
        if (!user.passwordHash.contentEquals(hashedPassword)) {
            logger.error("Authentication error: Invalid password for user '$login'.")
            throw InvalidPasswordException()
        }

        logger.info("Authentication for the user '$login' is successful.")
        return user
    }
}