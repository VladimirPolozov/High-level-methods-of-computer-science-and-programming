package main.kotlin.domain.repository

import main.kotlin.domain.entities.User

interface UserRepository {
    fun findByLogin(login: String): User?

    fun updatePassword(login: String, passwordHash: ByteArray, salt: ByteArray)
}