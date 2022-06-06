package aj.dev.event.view.list.vm

import aj.dev.event.data.model.Temperature

interface EventsListMethods {
    suspend fun fetchEvents(): List<Temperature>
}