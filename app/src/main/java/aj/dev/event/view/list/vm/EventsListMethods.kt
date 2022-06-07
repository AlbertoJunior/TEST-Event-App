package aj.dev.event.view.list.vm

interface EventsListMethods {
    suspend fun fetchEvents(): List<EventListPresenter>
}