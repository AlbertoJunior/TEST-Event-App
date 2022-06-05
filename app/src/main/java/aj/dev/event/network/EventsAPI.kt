package aj.dev.event.network

import aj.dev.event.data.model.Temperature
import retrofit2.http.GET
import retrofit2.http.Query

interface EventsAPI {
    companion object {
        const val BASE_URL = "http://5f5a8f24d44d640016169133.mockapi.io/api/"
    }

    @GET("events")
    suspend fun fetchEvents(): Array<Temperature>

    @GET("events/{id}")
    suspend fun fetchEventsDetail(@Query("id") id: Long): Temperature
}