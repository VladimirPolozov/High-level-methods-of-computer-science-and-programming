package infrastructure.adapters.db.entities

@Entity
@Table(name = "users")
data class JpaUser(
    @Id
    @Column(name = "login", nullable = false, unique = true)
    val login: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: ByteArray,

    @Column(nullable = false)
    val salt: ByteArray
)