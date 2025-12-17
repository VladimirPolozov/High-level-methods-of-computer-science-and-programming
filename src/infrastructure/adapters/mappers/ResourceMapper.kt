package infrastructure.adapters.mappers

import domain.entities.Resource
import java.sql.ResultSet

class ResourceMapper {
    /**
     * Преобразует ResultSet, полученный из таблицы RESOURCES, в доменную сущность Resource.
     */
    fun toDomain(resultSet: ResultSet): Resource {

        val path = resultSet.getString("path")
        val maxVolume = resultSet.getInt("max_volume")

        return Resource(
            path = path,
            maxVolume = maxVolume,
        )
    }
}