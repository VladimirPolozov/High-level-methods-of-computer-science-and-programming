package app.components

import application.services.*
import domain.services.ActionAndPathValidator
import domain.services.AccessController
import domain.services.AuthService
import domain.services.VolumeValidator
import domain.repository.UserRepository
import domain.repository.ResourceRepository
import infrastructure.adapters.mappers.UserMapper
import infrastructure.adapters.mappers.ResourceMapper
import infrastructure.adapters.security.HashService
import infrastructure.adapters.db.repositories.JdbcUserRepository
import infrastructure.adapters.db.repositories.JdbcResourceRepository
import infrastructure.db.H2ConnectionProvider
import org.slf4j.LoggerFactory

/**
 * AppContainer: Фабрика, отвечающая за создание и внедрение всех зависимостей
 */
object AppContainer {

    private val logger = LoggerFactory.getLogger("APP_CONTAINER")

    private val connectionProvider = H2ConnectionProvider

    private val userMapper = UserMapper()
    private val resourceMapper = ResourceMapper()

    private val hashService = HashService

    private val userRepo: UserRepository = JdbcUserRepository(connectionProvider, userMapper)
    private val resourceRepo: ResourceRepository = JdbcResourceRepository(connectionProvider, resourceMapper)

    private val authService: AuthService = AuthServiceImpl(userRepo, hashService)

    private val accessController: AccessController = AccessControllerImpl()
    private val volumeValidator: VolumeValidator = VolumeValidatorImpl()
    private val actionAndPathValidator: ActionAndPathValidator = ActionAndPathValidatorImpl()

    init {
        val initialPasswords = mapOf(
            "alice" to "alice123",
            "bob" to "bob456",
            "admin" to "adminpass"
        )

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

    fun createDefault(): RequestProcessor {
        logger.info("Initializing application components...")

        return RequestProcessor(
            authService = authService,
            accessController = accessController,
            volumeValidator = volumeValidator,
            resourceRepository = resourceRepo,
            actionAndPathValidator = actionAndPathValidator,
        )
    }
}