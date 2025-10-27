// Implements IUserRepository: listOf<User> (из CreateUsers.kt, с хэшами)

package user

import UserRepository


class InMemoryUserRepository : UserRepository {
    private val users = CreateUsers.all()
    override fun findByLogin(login: String): User? = users.find { it.login == login }
}