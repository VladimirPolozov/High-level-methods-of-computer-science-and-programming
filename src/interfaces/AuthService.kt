// Интерфейс: authenticate(login, password): User? (аутентификация)
interface AuthService {
    fun authenticate(login: String, password: String): User?
}