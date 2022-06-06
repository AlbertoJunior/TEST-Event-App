package aj.dev.event.view.detail.vm

import aj.dev.event.data.model.Temperature

interface EventsDetailMethods {
    suspend fun fetchEventDetail(id: Long): Temperature
}