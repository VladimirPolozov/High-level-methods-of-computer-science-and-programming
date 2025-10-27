// Интерфейс: findByLogin(login): User? (абстракция хранения пользователей)

package infrastructure.adapters.interfaces

import domain.entities.User

interface UserRepository {
    fun findByLogin(login: String): User?
}