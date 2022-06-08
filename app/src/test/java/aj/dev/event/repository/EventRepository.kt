package aj.dev.event.repository

import aj.dev.event.MainDispatcherRule
import aj.dev.event.R
import aj.dev.event.data.model.Event
import aj.dev.event.data.repository.EventRepository
import aj.dev.event.listener.ProviderHandler
import aj.dev.event.network.CheckInRequest
import aj.dev.event.network.EventsAPI
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import java.util.*

@ExperimentalCoroutinesApi
internal class EventRepository {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var resource: ProviderHandler

    @MockK
    private lateinit var api: EventsAPI

    private lateinit var repository: EventRepository

    companion object {
        private const val VALID_ID = 1L
        private const val INVALID_ID = -1L
        private const val VALID_NAME = "mock"
        private const val INVALID_NAME = ""
        private const val VALID_EMAIL = "mock@mock.com"
        private const val INVALID_EMAIL = "mock@"

        private val VALID_CHECK_IN_REQUEST = CheckInRequest(VALID_ID, VALID_NAME, VALID_EMAIL)
        private val INVALID_CHECK_IN_REQUEST = CheckInRequest(INVALID_ID, INVALID_NAME, INVALID_EMAIL)

        private const val ERROR_FETCH_EVENTS = "Nenhum item foi encontrado"
        private const val ERROR_EVENT_NOT_FOUND = "Item não encontrado"
        private const val ERROR_CHECK_IN = "Não foi possível fazer Check-In nesse evento."

        private val RESULT_FETCH_EVENTS_SUCCESS = arrayOf(
            factory("1"), factory("2"), factory("3"), factory("4")
        )
        private val RESULT_FETCH_EVENTS_SUCCESS_EMPTY = emptyArray<Event>()

        private val RESULT_VALID_EVENT = factory("$VALID_ID")

        private fun factory(number: String) = Event(
            emptyList(),
            Date().time,
            "Description $number",
            "image$number",
            1.0 + number.toDouble(),
            1.0 + number.toDouble(),
            10.0 * number.toDouble(),
            "Title $number",
            number
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = EventRepository(api, resource)

        coEvery { resource.getString(R.string.error_items_not_found) } returns ERROR_FETCH_EVENTS
        coEvery { resource.getString(R.string.error_item_not_found) } returns ERROR_EVENT_NOT_FOUND
        coEvery { resource.getString(R.string.check_in_error_service) } returns ERROR_CHECK_IN
    }

    @Test
    fun `fetch events success`() = runTest {
        coEvery { api.fetchEvents() } returns RESULT_FETCH_EVENTS_SUCCESS
        val fetchEvents = repository.fetchEvents()
        assert(fetchEvents.isNotEmpty())
        assert(fetchEvents[3].id == "4")
        assert(fetchEvents[3].title == "Title 4")
    }

    @Test
    fun `fetch events success empty`() = runTest {
        coEvery { api.fetchEvents() } returns RESULT_FETCH_EVENTS_SUCCESS_EMPTY
        val fetchEvents = repository.fetchEvents()
        assert(fetchEvents.isEmpty())
    }

    @Test(expected = RuntimeException::class)
    fun `fetch events error`() = runTest {
        coEvery { api.fetchEvents() } throws RuntimeException(ERROR_FETCH_EVENTS)
        repository.fetchEvents()
    }

    @Test(expected = RuntimeException::class)
    fun `fetch events error response null`() = runTest {
        coEvery { api.fetchEvents() } returns mockk()
        repository.fetchEvents()
    }

    @Test
    fun `fetch events detail success`() = runTest {
        coEvery { api.fetchEventsDetail(VALID_ID) } returns RESULT_VALID_EVENT
        val fetchEventDetail = repository.fetchEventDetail(VALID_ID)
        assert(fetchEventDetail.id == "$VALID_ID")
        assert(fetchEventDetail.id != "$INVALID_ID")
        assert(fetchEventDetail.title == "Title $VALID_ID")
        assert(fetchEventDetail.image.isNotBlank())
    }

    @Test(expected = RuntimeException::class)
    fun `fetch events detail error`() = runTest {
        coEvery { api.fetchEventsDetail(INVALID_ID) } throws RuntimeException(ERROR_EVENT_NOT_FOUND)
        repository.fetchEventDetail(INVALID_ID)
    }

    @Test(expected = RuntimeException::class)
    fun `fetch events detail error response null`() = runTest {
        coEvery { api.fetchEventsDetail(INVALID_ID) } returns mockk()
        repository.fetchEventDetail(INVALID_ID)
    }

    @Test
    fun `fetch events check in success`() = runTest {
        coEvery { api.checkIn(VALID_CHECK_IN_REQUEST) } returns true
        repository.checkIn(VALID_ID, VALID_NAME, VALID_EMAIL)
    }

    @Test(expected = RuntimeException::class)
    fun `fetch events check in error`() = runTest {
        coEvery { api.checkIn(INVALID_CHECK_IN_REQUEST) } returns false
        repository.checkIn(any(), any(), any())
    }

}