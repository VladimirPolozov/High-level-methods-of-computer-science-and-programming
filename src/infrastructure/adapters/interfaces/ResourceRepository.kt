package infrastructure.adapters.interfaces

import domain.entities.Resource


// Интерфейс репозитория ресурсов: находит ресурс по полному пути (например, "A.B.C") или возвращает null
interface ResourceRepository {
    fun findByPath(path: String): Resource?
}