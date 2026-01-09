package data.repositories

import data.entities.JpaUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaUserRepository : JpaRepository<JpaUser, String> { // PK = String (login)
    fun findByLogin(login: String): JpaUser?
}