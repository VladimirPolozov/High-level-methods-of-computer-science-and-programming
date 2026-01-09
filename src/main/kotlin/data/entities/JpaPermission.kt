package data.entities

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class PermissionId(
    @Column(name = "user_login", nullable = false)
    val userLogin: String,

    @Column(name = "resource_path", nullable = false)
    val resourcePath: String,

    @Column(nullable = false)
    val action: String
) : Serializable

@Entity
@Table(name = "permissions")
data class JpaPermission(
    @EmbeddedId
    val id: PermissionId,

    @ManyToOne
    @MapsId("userLogin")
    @JoinColumn(name = "user_login", referencedColumnName = "login")
    val user: JpaUser
)