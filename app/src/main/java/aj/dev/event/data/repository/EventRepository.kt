package aj.dev.event.data.repository

import aj.dev.event.data.model.Temperature
import aj.dev.event.network.CheckInRequest
import aj.dev.event.network.EventsAPI
import aj.dev.event.view.detail.vm.EventsCheckinMethods
import aj.dev.event.view.detail.vm.EventsDetailMethods
import aj.dev.event.view.list.vm.EventsListMethods
import android.util.Log
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventsAPI: EventsAPI) : EventsListMethods,
    EventsDetailMethods, EventsCheckinMethods {

    override suspend fun fetchEvents(): List<Temperature> {
        return try {
            eventsAPI.fetchEvents().toList()
        } catch (e: Exception) {
            listOf()
        }
    }

    override suspend fun fetchEventDetail(id: Long): Temperature {
        return try {
            eventsAPI.fetchEventsDetail(id)
        } catch (e: Exception) {
            print(e.message)
            throw RuntimeException("Item não encontrado")
        }
    }

    override suspend fun checkIn(id: Long, name: String, email: String): String {
        return try {
            eventsAPI.checkIn(CheckInRequest(id, name, email))
            "Check-in Realizado com Sucesso!"
        } catch (e: Exception) {
            Log.e("EventRepository", e.message ?: "checkIn")
            "No momento não possível realizar o Check-In"
        }
    }

}