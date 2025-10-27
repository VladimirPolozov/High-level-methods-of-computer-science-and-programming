package resources

import Action
import java.util.Collections.emptyMap

data class Resource(
    val name: String,
    val maxVolume: Int,
    val parent: Resource? = null,
    val permissions: Map<String, Set<Action>> = emptyMap()
) {
    fun fullPath(): String = parent?.let { it.fullPath() + "." + name } ?: name
}