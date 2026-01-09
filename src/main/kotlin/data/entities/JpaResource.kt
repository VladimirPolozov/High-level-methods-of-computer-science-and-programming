package data.entities

import javax.persistence.*

@Entity
@Table(name = "resources")
data class JpaResource(
    @Id
    @Column(name = "path", nullable = false, unique = true)
    val path: String,

    @Column(name = "max_volume", nullable = false)
    val maxVolume: Int
)