package infrastructure.adapters.db.repositories.jpa

import infrastructure.adapters.db.entities.JpaResource
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaResourceRepository : JpaRepository<JpaResource, String> {
    fun findByPath(path: String): JpaResource?
}

annotation class JpaUserRepository
