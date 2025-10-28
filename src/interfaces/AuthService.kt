package interfaces

import domain.entities.User


// Интерфейс аутентификации: проверяет логин и пароль, возвращает пользователя или null
interface AuthService {
    fun authenticate(login: String, password: String): User?
}