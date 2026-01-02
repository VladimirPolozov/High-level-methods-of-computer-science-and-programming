package infrastructure.adapters.db

import main.kotlin.domain.entities.Resource
import main.kotlin.domain.repository.ResourceRepository

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