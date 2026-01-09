package infrastructure.adapters.db.repositories.jpa

import data.mappers.toDomain
import data.repositories.JpaResourceRepository
import domain.entities.Resource
import domain.repository.ResourceRepository
import org.springframework.stereotype.Repository

@Repository
class JpaResourceRepositoryAdapter(
    private val jpaResourceRepo: JpaResourceRepository
) : ResourceRepository {

    override fun findByPath(path: String): Resource? {
        return jpaResourceRepo.findByPath(path)?.toDomain()
    }
}