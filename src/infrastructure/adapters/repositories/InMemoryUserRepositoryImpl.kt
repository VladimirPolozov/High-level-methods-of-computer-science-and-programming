package infrastructure.adapters.repositories

import domain.entities.Action
import infrastructure.adapters.interfaces.UserRepository
import domain.entities.User
import infrastructure.HashPassword


// In-memory репозиторий пользователей: хранит захардкоженных пользователей с хэшами паролей и правами доступа
object InMemoryUserRepositoryImpl : UserRepository {

    private val users: Map<String, User> = buildUsers()

    override fun findByLogin(login: String): User? = users[login]

    private fun buildUsers(): Map<String, User> {
        val aliceSalt = HashPassword.generateSalt()
        val aliceHash = HashPassword.hash("alicepass", aliceSalt)

        val bobSalt = HashPassword.generateSalt()
        val bobHash = HashPassword.hash("bobpass", bobSalt)

        return mapOf(
            "alice" to User(
                login = "alice",
                passwordHash = aliceHash,
                salt = aliceSalt,
                permissions = mapOf(
                    "docs" to setOf(Action.READ),
                    "docs.internal" to setOf(Action.READ, Action.WRITE)
                )
            ),
            "bob" to User(
                login = "bob",
                passwordHash = bobHash,
                salt = bobSalt,
                permissions = mapOf(
                    "A" to setOf(Action.READ, Action.EXECUTE),
                    "A.A8B" to setOf(Action.WRITE)
                )
            )
        )
    }
}