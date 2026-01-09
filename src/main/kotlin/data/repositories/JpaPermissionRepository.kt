package data.repositories

import data.entities.JpaPermission
import data.entities.PermissionId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaPermissionRepository : JpaRepository<JpaPermission, PermissionId> {
    fun findByIdUserLogin(login: String): List<JpaPermission>
    fun findByUserLogin(login: String)
}