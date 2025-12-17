package tests

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull

class InMemoryResourceRepositoryImplTest {

    private val repository = InMemoryResourceRepositoryImpl

    @Test
    fun shouldFindExistingResourceByFullPath() {
        val path = "A.A8B.C"
        val resource = repository.findByPath(path)
        assertNotNull(resource, "Ресурс по полному пути A.A8B.C должен быть найден")
    }

    @Test
    fun shouldFindExistingResourceByRootPath() {
        val path = "A"
        val resource = repository.findByPath(path)
        assertNotNull(resource, "Ресурс по корневому пути A должен быть найден")
    }

    @Test
    fun shouldReturnNullForNonExistingResource() {
        val path = "Non.Existent.Path"
        val resource = repository.findByPath(path)
        assertNull(resource, "Для несуществующего пути должен возвращаться null")
    }
}