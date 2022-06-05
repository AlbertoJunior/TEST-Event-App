package aj.dev.event.vm

import aj.dev.event.data.model.Temperature

interface EventsMethods {
    suspend fun fetchEvents(): List<Temperature>
}