package aj.dev.event.view.checkin.vm

import aj.dev.event.MainDispatcherRule
import aj.dev.event.R
import aj.dev.event.listener.ProviderHandler
import aj.dev.event.view.checkin.exception.ValidateEmailException
import aj.dev.event.view.checkin.exception.ValidateNameException
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
internal class EventCheckinViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var resource: ProviderHandler

    @MockK
    private lateinit var api: EventsCheckinMethods

    private lateinit var viewModel: EventCheckinViewModel

    companion object {
        private const val VALID_ID = 1L
        private const val VALID_NAME = "mock"
        private const val INVALID_NAME = ""
        private const val VALID_EMAIL = "mock@mock.com"
        private const val INVALID_EMAIL = "mock@"
        private const val RESULT_SUCCESS = "O seu Check-In foi realizado com sucesso!"
        private const val RESULT_NAME_ERROR = "Nome inválido"
        private const val RESULT_EMAIL_ERROR = "Email inválido"
        private const val RESULT_SERVICE_ERROR = "Não foi possível fazer Check-In nesse evento."
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = EventCheckinViewModel(api, resource)

        coEvery { resource.getString(R.string.check_in_success) } returns RESULT_SUCCESS
        coEvery { resource.getString(R.string.name_error) } returns RESULT_NAME_ERROR
        coEvery { resource.getString(R.string.email_error) } returns RESULT_EMAIL_ERROR
        coEvery { resource.getString(R.string.check_in_error_service) } returns RESULT_SERVICE_ERROR
    }

    @Test
    fun `check-in event success`() {
        // given
        val loadingObserver: Observer<Boolean?> = mockk(relaxed = true)
        val resultObserver: Observer<String?> = mockk(relaxed = true)
        val errorServiceObserver: Observer<String?> = mockk(relaxed = true)
        val errorNameObserver: Observer<String?> = mockk(relaxed = true)
        val errorEmailObserver: Observer<String?> = mockk(relaxed = true)

        // when
        coEvery { api.checkIn(VALID_ID, VALID_NAME, VALID_EMAIL) } returns Unit
        addObservers(
            loadingObserver,
            resultObserver,
            errorServiceObserver,
            errorNameObserver,
            errorEmailObserver
        )

        //then
        viewModel.checkIn(VALID_ID, VALID_NAME, VALID_EMAIL)
        verify { loadingObserver.onChanged(true) }
        verify(exactly = 0) { errorServiceObserver.onChanged(any()) }
        verify(exactly = 0) { errorNameObserver.onChanged(any()) }
        verify(exactly = 0) { errorEmailObserver.onChanged(any()) }
        verify { resultObserver.onChanged(RESULT_SUCCESS) }
        verify { loadingObserver.onChanged(false) }

        // finally
        removeObservers(
            loadingObserver,
            resultObserver,
            errorServiceObserver,
            errorNameObserver,
            errorEmailObserver
        )
    }

    @Test
    fun `check-in event error invalid name`() {
        // given
        val expectedResult: ValidateNameException = mockk(relaxed = true)

        val loadingObserver: Observer<Boolean?> = mockk(relaxed = true)
        val resultObserver: Observer<String?> = mockk(relaxed = true)
        val errorServiceObserver: Observer<String?> = mockk(relaxed = true)
        val errorNameObserver: Observer<String?> = mockk(relaxed = true)
        val errorEmailObserver: Observer<String?> = mockk(relaxed = true)

        // when
        coEvery { api.checkIn(VALID_ID, INVALID_NAME, VALID_EMAIL) } throws expectedResult
        addObservers(
            loadingObserver,
            resultObserver,
            errorServiceObserver,
            errorNameObserver,
            errorEmailObserver
        )

        //then
        viewModel.checkIn(VALID_ID, INVALID_NAME, VALID_EMAIL)
        verify { loadingObserver.onChanged(true) }
        verify(exactly = 0) { errorServiceObserver.onChanged(any()) }
        verify(exactly = 1) { errorNameObserver.onChanged(RESULT_NAME_ERROR) }
        verify(exactly = 0) { errorEmailObserver.onChanged(any()) }
        verify(exactly = 0) { resultObserver.onChanged(any()) }
        verify { loadingObserver.onChanged(false) }

        // finally
        removeObservers(
            loadingObserver,
            resultObserver,
            errorServiceObserver,
            errorNameObserver,
            errorEmailObserver
        )
    }

    @Test
    fun `check-in event error invalid email`() {
        // given
        val expectedResult: ValidateEmailException = mockk(relaxed = true)

        val loadingObserver: Observer<Boolean?> = mockk(relaxed = true)
        val resultObserver: Observer<String?> = mockk(relaxed = true)
        val errorServiceObserver: Observer<String?> = mockk(relaxed = true)
        val errorNameObserver: Observer<String?> = mockk(relaxed = true)
        val errorEmailObserver: Observer<String?> = mockk(relaxed = true)

        // when
        coEvery { api.checkIn(VALID_ID, VALID_NAME, INVALID_EMAIL) } throws expectedResult
        addObservers(
            loadingObserver,
            resultObserver,
            errorServiceObserver,
            errorNameObserver,
            errorEmailObserver
        )

        //then
        viewModel.checkIn(VALID_ID, VALID_NAME, INVALID_EMAIL)
        verify { loadingObserver.onChanged(true) }
        verify(exactly = 0) { errorServiceObserver.onChanged(any()) }
        verify(exactly = 0) { errorNameObserver.onChanged(any()) }
        verify(exactly = 1) { errorEmailObserver.onChanged(RESULT_EMAIL_ERROR) }
        verify(exactly = 0) { resultObserver.onChanged(any()) }
        verify { loadingObserver.onChanged(false) }

        // finally
        removeObservers(
            loadingObserver,
            resultObserver,
            errorServiceObserver,
            errorNameObserver,
            errorEmailObserver
        )
    }

    @Test
    fun `check-in event error in the service`() {
        // given
        val expectedResult = Exception(RESULT_SERVICE_ERROR)
        val loadingObserver: Observer<Boolean?> = mockk(relaxed = true)
        val resultObserver: Observer<String?> = mockk(relaxed = true)
        val errorServiceObserver: Observer<String?> = mockk(relaxed = true)
        val errorNameObserver: Observer<String?> = mockk(relaxed = true)
        val errorEmailObserver: Observer<String?> = mockk(relaxed = true)

        // when
        coEvery { api.checkIn(VALID_ID, VALID_NAME, VALID_EMAIL) } throws expectedResult
        addObservers(
            loadingObserver,
            resultObserver,
            errorServiceObserver,
            errorNameObserver,
            errorEmailObserver
        )

        //then
        viewModel.checkIn(VALID_ID, VALID_NAME, VALID_EMAIL)
        verify { loadingObserver.onChanged(true) }
        verify(exactly = 1) { errorServiceObserver.onChanged(RESULT_SERVICE_ERROR) }
        verify(exactly = 0) { errorNameObserver.onChanged(any()) }
        verify(exactly = 0) { errorEmailObserver.onChanged(any()) }
        verify(exactly = 0) { resultObserver.onChanged(any()) }
        verify { loadingObserver.onChanged(false) }

        // finally
        removeObservers(
            loadingObserver,
            resultObserver,
            errorServiceObserver,
            errorNameObserver,
            errorEmailObserver
        )
    }

    private fun removeObservers(
        loadingObserver: Observer<Boolean?>,
        resultObserver: Observer<String?>,
        errorServiceObserver: Observer<String?>,
        errorNameObserver: Observer<String?>,
        errorEmailObserver: Observer<String?>
    ) {
        viewModel.loading.removeObserver(loadingObserver)
        viewModel.checkInResult.removeObserver(resultObserver)
        viewModel.errorService.removeObserver(errorServiceObserver)
        viewModel.errorName.removeObserver(errorNameObserver)
        viewModel.errorEmail.removeObserver(errorEmailObserver)
    }

    private fun addObservers(
        loadingObserver: Observer<Boolean?>,
        resultObserver: Observer<String?>,
        errorServiceObserver: Observer<String?>,
        errorNameObserver: Observer<String?>,
        errorEmailObserver: Observer<String?>
    ) {
        viewModel.loading.observeForever(loadingObserver)
        viewModel.checkInResult.observeForever(resultObserver)
        viewModel.errorService.observeForever(errorServiceObserver)
        viewModel.errorName.observeForever(errorNameObserver)
        viewModel.errorEmail.observeForever(errorEmailObserver)
    }
}