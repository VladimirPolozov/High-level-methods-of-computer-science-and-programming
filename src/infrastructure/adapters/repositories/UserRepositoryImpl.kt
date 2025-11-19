package infrastructure.adapters.repositories

import java.sql.Connection
import infrastructure.adapters.interfaces.UserRepository
import domain.entities.User
import domain.entities.Action

class UserRepositoryImpl(
    private val connection: Connection
) : UserRepository {

    override fun findByLogin(login: String): User? {
        val sql = "SELECT login, password_hash, salt FROM users WHERE login = ?"
        val stmt = connection.prepareStatement(sql)
        stmt.setString(1, login)
        val rs = stmt.executeQuery()
        if (rs.next()) {
            val userLogin = rs.getString("login")
            val passwordHash = rs.getString("password_hash")
            val salt = rs.getString("salt")
            val permissions = loadPermissionsForUser(userLogin)
            return User(userLogin, passwordHash, salt, permissions)
        }
        return null
    }

    private fun loadPermissionsForUser(login: String): Map<String, Set<Action>> {
        val sql = "SELECT resource_path, action FROM permissions WHERE user_login = ?"
        val stmt = connection.prepareStatement(sql)
        stmt.setString(1, login)
        val rs = stmt.executeQuery()
        val result = mutableMapOf<String, MutableSet<Action>>()
        while (rs.next()) {
            val path = rs.getString("resource_path")
            val action = Action.valueOf(rs.getString("action"))
            result.computeIfAbsent(path) { mutableSetOf() }.add(action)
        }
        return result.mapValues { it.value.toSet() }
    }
}