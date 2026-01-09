package data.entities

import javax.persistence.*

@Entity
@Table(name = "users")
data class JpaUser(
    @Id
    @Column(name = "login", nullable = false)
    val login: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: ByteArray,

    @Column(nullable = false)
    val salt: ByteArray
)