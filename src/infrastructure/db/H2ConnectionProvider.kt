package infrastructure.db

import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import domain.exceptions.DbConnectionException

object H2ConnectionProvider {

    private val logger = LoggerFactory.getLogger("DB_CONN")

    private const val DB_URL = "jdbc:h2:file:./src/data/app-db"

    /**
     * Открывает новое соединение с БД.
     * @throws DbConnectionException при ошибке подключения.
     */
    fun getConnection(): Connection {
        try {
            Class.forName("org.h2.Driver")
            logger.debug("Attempting to connect to $DB_URL")

            val connection = DriverManager.getConnection(DB_URL)
            connection.autoCommit = false
            logger.debug("Successfully connected to the database.")
            return connection
        } catch (e: Exception) {
            logger.error("Database connection failed: ${e.message}")
            throw DbConnectionException()
        }
    }

    /**
     * Закрывает соединение, Statement и ResultSet.
     * Необходимо вызывать в блоке 'finally'.
     */
    fun close(connection: Connection?, statement: java.sql.Statement?, resultSet: ResultSet?) {
        resultSet?.close()
        statement?.close()
        try {
            connection?.close()
            logger.trace("DB connection closed.")
        } catch (e: Exception) {
            logger.error("Error closing connection: ${e.message}")
        }
    }
}