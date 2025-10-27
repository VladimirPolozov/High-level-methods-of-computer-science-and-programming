package infrastructure.adapters.interfaces

import domain.entities.Resource


// Интерфейс: findByPath(path): Resource? (поиск по иерархии)
interface ResourceRepository {
    fun findByPath(path: String): Resource?
}