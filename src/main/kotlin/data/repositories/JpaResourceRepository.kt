package data.repositories

import data.entities.JpaResource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaResourceRepository : JpaRepository<JpaResource, String> { // PK = String (path)
    fun findByPath(path: String): JpaResource?
}