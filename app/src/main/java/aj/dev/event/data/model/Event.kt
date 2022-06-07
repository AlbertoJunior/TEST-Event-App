package aj.dev.event.data.model

import java.io.Serializable

data class Event(
    val people: List<People>? = emptyList(),
    val date: Long,
    val description: String,
    val image: String,
    val longitude: Double,
    val latitude: Double,
    val price: Double,
    val title: String,
    val id: String
) : Serializable