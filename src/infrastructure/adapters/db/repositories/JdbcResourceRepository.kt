package infrastructure.adapters.db.repositories

import domain.entities.Resource
import domain.exceptions.DbConnectionException
import domain.repository.ResourceRepository
import infrastructure.adapters.mappers.ResourceMapper
import infrastructure.db.H2ConnectionProvider
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.ResultSet

class JdbcResourceRepository(
    private val connectionProvider: H2ConnectionProvider,
    private val resourceMapper: ResourceMapper
) : ResourceRepository {

    private val logger = LoggerFactory.getLogger("RESOURCE_REPO")

    override fun findByPath(path: String): Resource? {
        var connection: Connection? = null
        var statement: java.sql.PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            connection = connectionProvider.getConnection()

            val sql = "SELECT path, max_volume FROM resources WHERE path = ?"
            statement = connection.prepareStatement(sql)
            statement.setString(1, path)
            resultSet = statement.executeQuery()

            if (!resultSet.next()) {
                logger.debug("Resource '$path' not found in database.")
                return null
            }

            return resourceMapper.toDomain(resultSet)

        } catch (e: DbConnectionException) {
            throw e
        } catch (e: Exception) {
            logger.error("Error finding resource '$path': ${e.message}", e)
            throw DbConnectionException()
        } finally {
            connectionProvider.close(connection, statement, resultSet)
        }
    }
}
