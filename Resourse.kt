data class Resource(
    val name: String,
    val maxVolume: Int,
    val content: String? = null,
    val children: MutableList<Resource> = mutableListOf()
)



