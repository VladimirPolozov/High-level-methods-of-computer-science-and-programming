package infrastructure.adapters.mappers

import domain.entities.User
import java.sql.ResultSet

class UserMapper {

    /**
     * Преобразует ResultSet, полученный из таблицы USERS, в доменную сущность User.
     * Права доступа (permissions) не маппируются здесь, они должны быть добавлены позже
     * при сборке финального объекта User.
     */
    fun toDomain(resultSet: ResultSet): User {
        val login = resultSet.getString("login")
        val hash = resultSet.getBytes("password_hash")
        val salt = resultSet.getBytes("salt")

        return User(
            login = login,
            passwordHash = hash,
            salt = salt,
            permissions = emptyMap()
        )
    }
}