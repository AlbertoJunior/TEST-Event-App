package aj.dev.event.data.repository

import aj.dev.event.data.model.Temperature
import aj.dev.event.network.EventsAPI
import aj.dev.event.vm.EventsMethods
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventsAPI: EventsAPI) : EventsMethods {

    override suspend fun fetchEvents(): List<Temperature> {
        return try {
            eventsAPI.fetchEvents().toList()
        } catch (e: Exception) {
            listOf()
        }
    }
}