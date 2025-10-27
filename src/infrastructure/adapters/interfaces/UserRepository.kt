package infrastructure.adapters.interfaces

import domain.entities.User


// Интерфейс репозитория ресурсов: возвращает ресурс по строковому пути (например, "src.domain.User") или null, если не найден
interface UserRepository {
    fun findByLogin(login: String): User?
}