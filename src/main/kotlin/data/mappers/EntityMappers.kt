package data.mappers

import data.entities.JpaPermission
import data.entities.JpaResource
import data.entities.JpaUser
import data.entities.PermissionId
import domain.entities.Resource
import domain.entities.User
import domain.enums.Action

// JPA -> Domain
fun JpaUser.toDomain(permissions: List<JpaPermission>): User {
    // Группируем права по ресурсам
    val permissionsMap = permissions.groupBy(
        keySelector = { it.id.resourcePath },
        valueTransform = {
            Action.fromString(it.id.action)
                ?: throw IllegalArgumentException("Unknown action: ${it.id.action}")
        }
    ).mapValues { it.value.toSet() }

    return User(
        login = this.login,
        passwordHash = this.passwordHash,
        salt = this.salt,
        permissions = permissionsMap
    )
}

fun JpaResource.toDomain(): Resource {
    return Resource(
        path = this.path,
        maxVolume = this.maxVolume
    )
}

// Domain -> JPA
fun User.toJpa(): JpaUser {
    return JpaUser(
        login = this.login,
        passwordHash = this.passwordHash ?: byteArrayOf(),
        salt = this.salt ?: byteArrayOf()
    )
}

fun Resource.toJpa(): JpaResource {
    return JpaResource(
        path = this.path,
        maxVolume = this.maxVolume
    )
}

fun Pair<String, Action>.toJpaPermission(userLogin: String): JpaPermission {
    return JpaPermission(
        id = PermissionId(
            userLogin = userLogin,
            resourcePath = this.first,
            action = this.second.name
        ),
        user = JpaUser( // stub, будет заменён при сохранении
            login = userLogin,
            passwordHash = byteArrayOf(),
            salt = byteArrayOf()
        )
    )
}