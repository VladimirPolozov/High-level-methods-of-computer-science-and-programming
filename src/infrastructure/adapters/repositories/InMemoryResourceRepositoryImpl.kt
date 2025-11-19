package infrastructure.adapters.repositories

import domain.entities.Resource
import infrastructure.adapters.interfaces.ResourceRepository


// In-memory репозиторий ресурсов: хранит все ресурсы в мапе по полному пути (без дерева и parent-ссылок)
object InMemoryResourceRepositoryImpl : ResourceRepository {

    private val resources: Map<String, Resource> = buildResourceMap()

    override fun findByPath(path: String): Resource? {
        return resources[path]
    }

    private fun buildResourceMap(): Map<String, Resource> {
        return mapOf(
            "A" to Resource(name = "A", maxVolume = 100),
            "A.A8B" to Resource(name = "A8B", maxVolume = 50),
            "A.A8B.C" to Resource(name = "C", maxVolume = 30),
            "A.A8B.C.f_d" to Resource(name = "f_d", maxVolume = 10),
        )
    }
}