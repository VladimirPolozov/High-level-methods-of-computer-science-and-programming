package infrastructure.adapters.db

import data.repositories.JpaPermissionRepository
import domain.entities.User
import domain.enums.Action
import domain.repository.UserRepository
import infrastructure.adapters.db.entities.JpaUser
import infrastructure.adapters.db.repositories.jpa.JpaUserRepository
import org.springframework.stereotype.Repository

@Repository
class JpaUserRepositoryAdapter(
    private val jpaUserRepo: JpaUserRepository,
    private val jpaPermissionRepo: JpaPermissionRepository
) : UserRepository {

    override fun findByLogin(login: String): User? {
        val jpaUser = jpaUserRepo.findByLogin(login) ?: return null
        val permissions = loadPermissions(login)
        return toDomainUser(jpaUser, permissions)
    }

    override fun updatePassword(login: String, passwordHash: ByteArray, salt: ByteArray) {
        val user = jpaUserRepo.findByLogin(login) ?: return
        val updatedUser = JpaUser(
            login = login,
            passwordHash = passwordHash,
            salt = salt
        )
        jpaUserRepo.save(updatedUser)
    }

    private fun loadPermissions(login: String): Map<String, Set<Action>> {
        val permissions = jpaPermissionRepo.findByUserLogin(login)
        return permissions.groupBy(
            keySelector = { it.id.resourcePath },
            valueTransform = {
                Action.Companion.fromString(it.id.action.uppercase())
                    ?: throw IllegalArgumentException("Unknown action: ${it.id.action}")
            }
        ).mapValues { it.value.toSet() }
    }

    private fun toDomainUser(jpaUser: JpaUser, permissions: Map<String, Set<Action>>): User {
        return User(
            login = jpaUser.login,
            passwordHash = jpaUser.passwordHash,
            salt = jpaUser.salt,
            permissions = permissions
        )
    }
}