import resources.Resource

// Интерфейс: findByPath(path): Resource? (поиск по иерархии)

interface ResourceRepository {
    fun findByPath(path: String): Resource?
}