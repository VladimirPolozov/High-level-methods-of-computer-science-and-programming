// Интерфейс: findByPath(path): Resource? (поиск по иерархии)

package infrastructure.adapters.interfaces

import domain.entities.Resource

interface ResourceRepository {
    fun findByPath(path: String): Resource?
}