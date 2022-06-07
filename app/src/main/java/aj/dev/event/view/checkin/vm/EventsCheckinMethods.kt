package aj.dev.event.view.checkin.vm

interface EventsCheckinMethods {
    suspend fun checkIn(id: Long, name: String, email: String)
}