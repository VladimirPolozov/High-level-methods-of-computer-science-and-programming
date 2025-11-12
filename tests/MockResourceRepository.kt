package tests

import domain.entities.Resource
import infrastructure.adapters.interfaces.ResourceRepository

class MockResourceRepository(
    private val existingPaths: Set<String>
) : ResourceRepository {
    override fun findByPath(path: String): Resource? {
        return if (path in existingPaths) {
            Resource(name = path.substringAfterLast('.'), maxVolume = 100)
        } else {
            null
        }
    }
}