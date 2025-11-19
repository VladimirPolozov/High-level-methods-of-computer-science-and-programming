package infrastructure.adapters.repositories

import domain.entities.Resource
import infrastructure.adapters.interfaces.ResourceRepository
import java.sql.Connection

class ResourceRepositoryImpl(private val connection: Connection) : ResourceRepository {

    override fun findByPath(path: String): Resource? {
        val sql = "SELECT name, max_volume FROM resources WHERE path = ?"
        val stmt = connection.prepareStatement(sql)
        stmt.setString(1, path)
        val rs = stmt.executeQuery()
        if (rs.next()) {
            val name = rs.getString("name")
            val maxVolume = rs.getInt("max_volume")
            return Resource(name, maxVolume)
        }
        return null
    }
}