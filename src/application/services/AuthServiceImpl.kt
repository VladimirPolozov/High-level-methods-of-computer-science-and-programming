package application.services

import domain.entities.User
import domain.exceptions.InvalidLoginException
import domain.exceptions.InvalidPasswordException
import domain.repository.UserRepository
import domain.services.AuthService
import infrastructure.adapters.security.HashService
import org.slf4j.LoggerFactory

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