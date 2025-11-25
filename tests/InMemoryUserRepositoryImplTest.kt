package tests

import infrastructure.adapters.repositories.InMemoryUserRepositoryImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertEquals

class InMemoryUserRepositoryImplTest {

    private val repository = InMemoryUserRepositoryImpl

    @Test
    fun shouldFindExistingUser() {
        val login = "alice"
        val user = repository.findByLogin(login)
        assertNotNull(user, "Пользователь 'alice' должен быть найден")
        assertEquals(login, user!!.login, "Найденный логин должен соответствовать запрошенному")
    }

    @Test
    fun shouldReturnNullForNonExistingUser() {
        val login = "non_existent_user"
        val user = repository.findByLogin(login)
        assertNull(user, "Для несуществующего логина должен возвращаться null")
    }
}