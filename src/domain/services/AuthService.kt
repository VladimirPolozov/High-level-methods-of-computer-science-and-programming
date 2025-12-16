package domain.services

import domain.entities.User

interface AuthService {
    fun authenticate(login: String, password: String): User
}