package aj.dev.event.view.checkin.vm

import aj.dev.event.MainDispatcherRule
import aj.dev.event.R
import aj.dev.event.listener.ProviderHandler
import aj.dev.event.view.list.vm.EventListPresenter
import aj.dev.event.view.list.vm.EventListViewModel
import aj.dev.event.view.list.vm.EventsListMethods
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class EventListViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var resource: ProviderHandler

    @MockK
    private lateinit var api: EventsListMethods

    private lateinit var viewModel: EventListViewModel

    companion object {
        private const val ERROR_FETCH_EVENTS = "Não foi possível buscar os eventos."
        private const val ERROR_DETAIL_EVENT = "Detalhes do evento indisponível."

        private const val ITEM_ID_VALID = "1"
        private const val ITEM_ID_INVALID = ""
        private val ITEM_ID_INVALID_NULL: String? = null

        private val RESULT_VALID_3: List<EventListPresenter> = listOf(
            EventListPresenter("1", "Title 1", 1.0, "01-01-2022"),
            EventListPresenter("2", "Title 2", 2.0, "02-02-2022"),
            EventListPresenter("3", "Title 3", 30.0, "03-03-2022")
        )
        private val RESULT_VALID: List<EventListPresenter> = emptyList()
        private val RESULT_INVALID: Exception = Exception()
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = EventListViewModel(api, resource)

        coEvery { resource.getString(R.string.error_fetch_events) } returns ERROR_FETCH_EVENTS
        coEvery { resource.getString(R.string.error_detail_event) } returns ERROR_DETAIL_EVENT
    }

    @Test
    fun `fetch events success`() {
        // given
        val loadingObserver: Observer<Boolean?> = mockk(relaxed = true)
        val eventsObserver: Observer<List<EventListPresenter>?> = mockk(relaxed = true)
        val errorObserver: Observer<String?> = mockk(relaxed = true)

        // when
        addObservers(loadingObserver, eventsObserver, errorObserver)
        coEvery { api.fetchEvents() } returns RESULT_VALID_3

        // then
        viewModel.fetchEvents()

        verify { loadingObserver.onChanged(true) }
        verify(exactly = 1) { eventsObserver.onChanged(RESULT_VALID_3) }
        verify(exactly = 0) { errorObserver.onChanged(any()) }
        verify { loadingObserver.onChanged(false) }

        // finally
        removeObservers(loadingObserver, eventsObserver, errorObserver)
    }

    @Test
    fun `fetch events success empty`() {
        // given
        val loadingObserver: Observer<Boolean?> = mockk(relaxed = true)
        val eventsObserver: Observer<List<EventListPresenter>?> = mockk(relaxed = true)
        val errorObserver: Observer<String?> = mockk(relaxed = true)

        // when
        addObservers(loadingObserver, eventsObserver, errorObserver)
        coEvery { api.fetchEvents() } returns RESULT_VALID

        // then
        viewModel.fetchEvents()

        verify { loadingObserver.onChanged(true) }
        verify(exactly = 1) { eventsObserver.onChanged(RESULT_VALID) }
        verify(exactly = 0) { errorObserver.onChanged(any()) }
        verify { loadingObserver.onChanged(false) }

        // finally
        removeObservers(loadingObserver, eventsObserver, errorObserver)
    }

    @Test
    fun `fetch events error`() {
        // given
        val loadingObserver: Observer<Boolean?> = mockk(relaxed = true)
        val eventsObserver: Observer<List<EventListPresenter>?> = mockk(relaxed = true)
        val errorObserver: Observer<String?> = mockk(relaxed = true)

        // when
        addObservers(loadingObserver, eventsObserver, errorObserver)
        coEvery { api.fetchEvents() } throws RESULT_INVALID

        // then
        viewModel.fetchEvents()

        verify { loadingObserver.onChanged(true) }
        verify(exactly = 1) { errorObserver.onChanged(ERROR_FETCH_EVENTS) }
        verify(exactly = 1) { eventsObserver.onChanged(null) }
        verify { loadingObserver.onChanged(false) }

        // finally
        removeObservers(loadingObserver, eventsObserver, errorObserver)
    }

    @Test
    fun `fetch events onItemClicked success`() {
        // given
        val loadingObserver: Observer<Boolean?> = mockk(relaxed = true)
        val navigateObserver: Observer<Long?> = mockk(relaxed = true)
        val errorObserver: Observer<String?> = mockk(relaxed = true)

        // when
        addObservers(
            loadingObserver,
            errorObserver = errorObserver,
            navigateObserver = navigateObserver
        )

        // then
        viewModel.onItemClicked(ITEM_ID_VALID)

        verify(exactly = 1) { navigateObserver.onChanged(1L) }
        verify(exactly = 0) { errorObserver.onChanged(any()) }

        // finally
        removeObservers(
            loadingObserver,
            errorObserver = errorObserver,
            navigateObserver = navigateObserver
        )
    }

    @Test
    fun `fetch events onItemClicked error id empty`() {
        // given
        val loadingObserver: Observer<Boolean?> = mockk(relaxed = true)
        val navigateObserver: Observer<Long?> = mockk(relaxed = true)
        val errorObserver: Observer<String?> = mockk(relaxed = true)

        // when
        addObservers(
            loadingObserver,
            errorObserver = errorObserver,
            navigateObserver = navigateObserver
        )

        // then
        viewModel.onItemClicked(ITEM_ID_INVALID)

        verify(exactly = 0) { navigateObserver.onChanged(any()) }
        verify(exactly = 1) { errorObserver.onChanged(ERROR_DETAIL_EVENT) }

        // finally
        removeObservers(
            loadingObserver,
            errorObserver = errorObserver,
            navigateObserver = navigateObserver
        )
    }

    @Test
    fun `fetch events onItemClicked error id null`() {
        // given
        val loadingObserver: Observer<Boolean?> = mockk(relaxed = true)
        val navigateObserver: Observer<Long?> = mockk(relaxed = true)
        val errorObserver: Observer<String?> = mockk(relaxed = true)

        // when
        addObservers(
            loadingObserver,
            errorObserver = errorObserver,
            navigateObserver = navigateObserver
        )

        // then
        viewModel.onItemClicked(ITEM_ID_INVALID_NULL)

        verify(exactly = 0) { navigateObserver.onChanged(any()) }
        verify(exactly = 1) { errorObserver.onChanged(ERROR_DETAIL_EVENT) }

        // finally
        removeObservers(
            loadingObserver,
            errorObserver = errorObserver,
            navigateObserver = navigateObserver
        )
    }

    private fun addObservers(
        loadingObserver: Observer<Boolean?>? = null,
        eventsObserver: Observer<List<EventListPresenter>?>? = null,
        errorObserver: Observer<String?>? = null,
        navigateObserver: Observer<Long?>? = null
    ) {
        loadingObserver?.let { viewModel.loading.observeForever(it) }
        eventsObserver?.let { viewModel.events.observeForever(it) }
        errorObserver?.let { viewModel.error.observeForever(it) }
        navigateObserver?.let { viewModel.navigate.observeForever(it) }
    }

    private fun removeObservers(
        loadingObserver: Observer<Boolean?>? = null,
        eventsObserver: Observer<List<EventListPresenter>?>? = null,
        errorObserver: Observer<String?>? = null,
        navigateObserver: Observer<Long?>? = null
    ) {
        loadingObserver?.let { viewModel.loading.removeObserver(it) }
        eventsObserver?.let { viewModel.events.removeObserver(it) }
        errorObserver?.let { viewModel.error.removeObserver(it) }
        navigateObserver?.let { viewModel.navigate.removeObserver(it) }
    }

}