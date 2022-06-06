package aj.dev.event.view.detail.vm

interface EventsCheckinMethods {
    suspend fun checkIn(id: Long, name: String, email: String): String
}