package resources

data class Resource(
    val name: String,
    val maxVolume: Int,
    val parent: Recource?,
)