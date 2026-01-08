package main.kotlin.infrastructure.db

import jakarta.annotation.PostConstruct
import main.kotlin.domain.repository.UserRepository
import main.kotlin.infrastructure.adapters.security.HashService
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory

@Component
class DataInitializer(
    private val userRepo: UserRepository,
    private val hashService: HashService
) {
    private val logger = LoggerFactory.getLogger(DataInitializer::class.java)

    @PostConstruct
    fun initPasswords() {
        val initialPasswords = mapOf(
            "alice" to "alice123",
            "bob" to "bob456",
            "admin" to "adminpass"
        )

        logger.info("Checking and initializing user passwords...")

        for ((login, password) in initialPasswords) {
            val user = userRepo.findByLogin(login)

            if (user != null && user.passwordHash == null) {
                val salt = hashService.generateSalt()
                val hashedPassword = hashService.hash(password, salt)

                userRepo.updatePassword(login, hashedPassword, salt)
                logger.info("Password initialized for user '$login'.")
            }
        }
    }
}