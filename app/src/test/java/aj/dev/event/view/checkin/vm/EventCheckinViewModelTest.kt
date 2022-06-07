package aj.dev.event.view.checkin.vm

import androidx.lifecycle.LiveData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class EventCheckinViewModelTest {
    private lateinit var viewModel: EventCheckinViewModel

    @Before
    fun setUp() {
        viewModel = EventCheckinViewModel(mockk(), mockk())
    }

    @Test
    fun getCheckInResult() {
        val result: LiveData<String?> = mockk()
        viewModel.checkIn(1L, "mock", "mock@mock.com")
        every { viewModel.checkInResult } returns result
    }

    @Test
    fun getErrorService() {
    }

    @Test
    fun getErrorName() {
    }

    @Test
    fun getErrorEmail() {
    }

    @Test
    fun getLoading() {
    }

    @Test
    fun checkIn() {
    }

    @Test
    fun clearErrorName() {
    }

    @Test
    fun clearErrorEmail() {
    }

    @Test
    fun clearErrorService() {
    }

    @Test
    fun clearLoading() {
    }

    @Test
    fun showLoading() {
    }

    @Test
    fun hideLoading() {
    }

    @Test
    fun clearCheckInResult() {
    }
}