package aj.dev.event.view.checkin.vm

import aj.dev.event.MainDispatcherRule
import aj.dev.event.R
import aj.dev.event.listener.ProviderHandler
import aj.dev.event.view.detail.vm.EventDetailPresenter
import aj.dev.event.view.detail.vm.EventDetailViewModel
import aj.dev.event.view.detail.vm.EventsDetailMethods
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
internal class EventDetailViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var resource: ProviderHandler

    @MockK
    private lateinit var api: EventsDetailMethods

    private lateinit var viewModel: EventDetailViewModel

    companion object {
        private const val VALID_ID = 1L
        private const val INVALID_ID = -1L

        private const val VALID_ID_STRING = "1"
        private val INVALID_ID_STRING: String? = null

        private const val ERROR_FETCH_EVENT = "Não foi possível buscar os detalhes do evento."
        private const val ERROR_NAVIGATE = "Não foi possível solicitar o Check-In pare este evento."

        private val RESULT_VALID = mockk<EventDetailPresenter>()
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = EventDetailViewModel(api, resource)

        coEvery { resource.getString(R.string.error_fetch_detail_event) } returns ERROR_FETCH_EVENT
        coEvery { resource.getString(R.string.error_check_in_event) } returns ERROR_NAVIGATE

        coEvery { api.fetchEventDetail(VALID_ID) } returns RESULT_VALID
        coEvery { api.fetchEventDetail(INVALID_ID) } throws Exception()
    }

    @Test
    fun `fetch event success`() {
        // given
        val loadingObserver: Observer<Boolean?> = mockk(relaxed = true)
        val resultObserver: Observer<EventDetailPresenter?> = mockk(relaxed = true)
        val errorObserver: Observer<String?> = mockk(relaxed = true)

        // when
        addObservers(loadingObserver, resultObserver, errorObserver)

        // then
        viewModel.fetchEvent(VALID_ID)

        verify { loadingObserver.onChanged(true) }
        verify(exactly = 1) { resultObserver.onChanged(RESULT_VALID) }
        verify(exactly = 0) { errorObserver.onChanged(any()) }
        verify { loadingObserver.onChanged(false) }

        // finally
        removeObservers(loadingObserver, resultObserver, errorObserver)
    }

    @Test
    fun `fetch event error invalid id`() {
        // given
        val loadingObserver: Observer<Boolean?> = mockk(relaxed = true)
        val resultObserver: Observer<EventDetailPresenter?> = mockk(relaxed = true)
        val errorObserver: Observer<String?> = mockk(relaxed = true)

        // when
        addObservers(loadingObserver, resultObserver, errorObserver)

        // then
        viewModel.fetchEvent(INVALID_ID)

        verify { loadingObserver.onChanged(true) }
        verify(exactly = 1) { errorObserver.onChanged(ERROR_FETCH_EVENT) }
        verify(exactly = 1) { resultObserver.onChanged(null) }
        verify { loadingObserver.onChanged(false) }

        // finally
        removeObservers(loadingObserver, resultObserver, errorObserver)
    }

    @Test
    fun `call check in success`() {
        // given
        val errorObserver: Observer<String?> = mockk(relaxed = true)
        val navigateObserver: Observer<Long?> = mockk(relaxed = true)

        // when
        addObservers(errorObserver = errorObserver, navigateObserver = navigateObserver)

        // then
        viewModel.callCheckIn(VALID_ID_STRING)

        verify(exactly = 1) { navigateObserver.onChanged(1L) }
        verify(exactly = 0) { errorObserver.onChanged(any()) }

        // finally
        removeObservers(errorObserver = errorObserver, navigateObserver = navigateObserver)
    }

    @Test
    fun `call check in invalid id error`() {
        // given
        val errorObserver: Observer<String?> = mockk(relaxed = true)
        val navigateObserver: Observer<Long?> = mockk(relaxed = true)

        // when
        addObservers(errorObserver = errorObserver, navigateObserver = navigateObserver)

        // then
        viewModel.callCheckIn(INVALID_ID_STRING)

        verify(exactly = 0) { navigateObserver.onChanged(any()) }
        verify(exactly = 1) { errorObserver.onChanged(ERROR_NAVIGATE) }

        // finally
        removeObservers(errorObserver = errorObserver, navigateObserver = navigateObserver)
    }

    private fun addObservers(
        loadingObserver: Observer<Boolean?>? = null,
        resultObserver: Observer<EventDetailPresenter?>? = null,
        errorObserver: Observer<String?>? = null,
        navigateObserver: Observer<Long?>? = null
    ) {
        loadingObserver?.let { viewModel.loading.observeForever(it) }
        resultObserver?.let { viewModel.event.observeForever(it) }
        errorObserver?.let { viewModel.error.observeForever(it) }
        navigateObserver?.let { viewModel.navigate.observeForever(it) }
    }

    private fun removeObservers(
        loadingObserver: Observer<Boolean?>? = null,
        resultObserver: Observer<EventDetailPresenter?>? = null,
        errorObserver: Observer<String?>? = null,
        navigateObserver: Observer<Long?>? = null
    ) {
        loadingObserver?.let { viewModel.loading.removeObserver(it) }
        resultObserver?.let { viewModel.event.removeObserver(it) }
        errorObserver?.let { viewModel.error.removeObserver(it) }
        navigateObserver?.let { viewModel.navigate.removeObserver(it) }
    }

}