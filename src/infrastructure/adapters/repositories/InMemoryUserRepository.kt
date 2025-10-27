package infrastructure.adapters.repositories

import infrastructure.adapters.interfaces.UserRepository
import user.CreateUsers
import domain.entities.User

// Implements IUserRepository: listOf<User> (из CreateUsers.kt, с хэшами)

class InMemoryUserRepository : UserRepository {
    private val users = CreateUsers.all()
    override fun findByLogin(login: String): User? = users.find { it.login == login }
}