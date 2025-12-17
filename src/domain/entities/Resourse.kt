package domain.entities


// Data class ресурса: содержит имя, максимальный объём
data class Resource(
    val path: String,
    val maxVolume: Int
)