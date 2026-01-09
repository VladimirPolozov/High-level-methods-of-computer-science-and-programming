package infrastructure.adapters.db

import domain.entities.Resource
import domain.repository.ResourceRepository

class MockResourceRepository(
    private val existingPaths: Set<String>
) : ResourceRepository {
    override fun findByPath(path: String): Resource? {
        return if (path in existingPaths) {
            Resource(path = path, maxVolume = 100)
        } else {
            null
        }
    }
}