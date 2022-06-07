package aj.dev.event.network

import aj.dev.event.data.model.Event
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventsAPI {
    companion object {
        const val BASE_URL = "http://5f5a8f24d44d640016169133.mockapi.io/api/"
    }

    @GET("events")
    suspend fun fetchEvents(): Array<Event>

    @GET("events/{id}")
    suspend fun fetchEventsDetail(@Path("id") id: Long): Event

    @POST("checkin")
    suspend fun checkIn(@Body request: CheckInRequest): Any?
}