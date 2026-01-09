package infrastructure.adapters.db

import domain.entities.User
import domain.repository.UserRepository

class MockUserRepository(
    private val expectedLogin: String,
    private val expectedUser: User?
) : UserRepository {
    var findByLoginCallCount = 0

    override fun findByLogin(login: String): User? {
        ++findByLoginCallCount
        return if (login == expectedLogin) expectedUser else null
    }

    override fun updatePassword(login: String, passwordHash: ByteArray, salt: ByteArray) {
    }
}