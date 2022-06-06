package aj.dev.event.network

import java.io.Serializable

data class CheckInRequest(val eventId: Long, val name: String, val email: String) : Serializable