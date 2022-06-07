package aj.dev.event.data.repository

import aj.dev.event.R
import aj.dev.event.listener.ProviderHandler
import aj.dev.event.network.CheckInRequest
import aj.dev.event.network.EventsAPI
import aj.dev.event.view.checkin.vm.EventsCheckinMethods
import aj.dev.event.view.detail.vm.EventsDetailMethods
import aj.dev.event.view.detail.vm.TemperatureDetailPresenter
import aj.dev.event.view.list.vm.EventsListMethods
import aj.dev.event.view.list.vm.TemperatureListPresenter
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventsAPI: EventsAPI,
    private val resource: ProviderHandler
) : EventsListMethods, EventsDetailMethods, EventsCheckinMethods {

    override suspend fun fetchEvents(): List<TemperatureListPresenter> {
        return try {
            eventsAPI.fetchEvents()
                .map { EventConverterUtils.temperatureToTemperatureListPresenter(it) }
                .toList()
        } catch (e: Exception) {
            throw RuntimeException(resource.getString(R.string.error_items_not_found))
        }
    }

    override suspend fun fetchEventDetail(id: Long): TemperatureDetailPresenter {
        return try {
            val temperature = eventsAPI.fetchEventsDetail(id)
            EventConverterUtils.temperatureToTemperatureDetailPresenter(temperature)
        } catch (e: Exception) {
            throw RuntimeException(resource.getString(R.string.error_item_not_found))
        }
    }

    override suspend fun checkIn(id: Long, name: String, email: String) {
        try {
            eventsAPI.checkIn(CheckInRequest(id, name, email))
        } catch (e: Exception) {
            throw RuntimeException(resource.getString(R.string.check_in_error_service))
        }
    }

}