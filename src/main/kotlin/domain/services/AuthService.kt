package main.kotlin.domain.services

import main.kotlin.domain.entities.User

interface AuthService {
    fun authenticate(login: String, password: String): User
}