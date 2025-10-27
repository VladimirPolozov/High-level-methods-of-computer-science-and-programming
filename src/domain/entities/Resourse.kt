package domain.entities

import java.util.Collections.emptyMap


data class Resource(
    val name: String,
    val maxVolume: Int,
    val permissions: Map<String, Set<Action>> = emptyMap()
)