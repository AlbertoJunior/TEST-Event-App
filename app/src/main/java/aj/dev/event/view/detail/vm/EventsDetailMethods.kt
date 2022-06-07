package aj.dev.event.view.detail.vm

interface EventsDetailMethods {
    suspend fun fetchEventDetail(id: Long): EventDetailPresenter
}