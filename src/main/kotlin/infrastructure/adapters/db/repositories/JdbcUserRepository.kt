package main.kotlin.infrastructure.adapters.db.repositories

import main.kotlin.domain.enums.Action
import main.kotlin.domain.entities.User
import main.kotlin.domain.exceptions.DbConnectionException
import main.kotlin.domain.repository.UserRepository
import main.kotlin.infrastructure.adapters.mappers.UserMapper
import main.kotlin.infrastructure.db.H2ConnectionProvider
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.ResultSet

/**
 * Реализация UserRepository с использованием JDBC для H2/SQLite.
 * Ответственна за загрузку User, его хеша, соли и прав доступа.
 */
@Repository
class JdbcUserRepository(
    private val connectionProvider: H2ConnectionProvider,
    private val userMapper: UserMapper
) : UserRepository {

    private val logger = LoggerFactory.getLogger("main")

    override fun findByLogin(login: String): User? {
        var connection: Connection? = null
        var statement: java.sql.PreparedStatement? = null
        var userResultSet: ResultSet? = null

        try {
            connection = connectionProvider.getConnection()

            val userSql = "SELECT login, password_hash, salt FROM users WHERE login = ?"
            statement = connection.prepareStatement(userSql)
            statement.setString(1, login)

            userResultSet = statement.executeQuery()

            if (!userResultSet.next()) {
                logger.debug("User '$login' not found in database.")
                return null
            }

            val baseUser = userMapper.toDomain(userResultSet)

            val permissions = loadPermissions(connection, baseUser.login)

            logger.debug("Creating copy of baseUser with new permissions: {}", permissions)
            val updatedUser = baseUser.copy(permissions = permissions)
            logger.debug("Updated user: {}", updatedUser)

            return baseUser.copy(permissions = permissions)


        } catch (e: DbConnectionException) {
            throw e
        } catch (e: Exception) {
            logger.error("Error finding user '$login': ${e.message}", e)
            throw DbConnectionException()
        } finally {
            connectionProvider.close(connection, statement, userResultSet)
        }
    }

    override fun updatePassword(login: String, passwordHash: ByteArray, salt: ByteArray) {
        val sql = "UPDATE USERS SET password_hash = ?, salt = ? WHERE login = ?"
        connectionProvider.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setBytes(1, passwordHash)
                stmt.setBytes(2, salt)
                stmt.setString(3, login)
                stmt.executeUpdate()
            }
            conn.commit()
        }
    }

    /**
     * Загружает все права доступа для данного пользователя, собирая их в Map<ResourcePath, Set<Action>>.
     * АДАПТИРОВАНО под таблицу permissions (user_login, resource_path, action).
     */
    private fun loadPermissions(connection: Connection, login: String): Map<String, Set<Action>> {
        val permissionsMap = mutableMapOf<String, MutableSet<Action>>()
        var statement: java.sql.PreparedStatement? = null
        var rightsResultSet: ResultSet? = null

        val rightsSql = """
            SELECT resource_path, action 
            FROM permissions 
            WHERE user_login = ?
        """.trimIndent()

        try {
            statement = connection.prepareStatement(rightsSql)
            statement.setString(1, login)
            rightsResultSet = statement.executeQuery()

            while (rightsResultSet.next()) {
                val path = rightsResultSet.getString("resource_path")
                val actionString = rightsResultSet.getString("action")

                val action = try {
                    Action.valueOf(actionString.uppercase())
                } catch (e: IllegalArgumentException) {
                    logger.warn("Unknown action '$actionString' found in DB for path '$path'. Skipping.")
                    continue
                }

                permissionsMap
                    .getOrPut(path) { mutableSetOf() }
                    .add(action)
            }

        } catch (e: Exception) {
            logger.error("Error loading permissions for user '$login': ${e.message}", e)
            throw DbConnectionException()
        } finally {
            rightsResultSet?.close()
            statement?.close()
        }

        return permissionsMap.mapValues { it.value.toSet() }
    }
}