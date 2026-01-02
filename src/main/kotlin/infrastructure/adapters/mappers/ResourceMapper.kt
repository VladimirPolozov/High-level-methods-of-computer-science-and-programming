package main.kotlin.infrastructure.adapters.mappers

import main.kotlin.domain.entities.Resource
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
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