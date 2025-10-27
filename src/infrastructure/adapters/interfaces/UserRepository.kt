import user.User

// Интерфейс: findByLogin(login): User? (абстракция хранения пользователей)

interface UserRepository {
    fun findByLogin(login: String): User?
}