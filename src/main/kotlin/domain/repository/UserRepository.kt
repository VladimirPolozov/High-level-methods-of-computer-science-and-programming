package domain.repository

import domain.entities.User

interface UserRepository {
    fun findByLogin(login: String): User?

    fun updatePassword(login: String, passwordHash: ByteArray, salt: ByteArray)
}