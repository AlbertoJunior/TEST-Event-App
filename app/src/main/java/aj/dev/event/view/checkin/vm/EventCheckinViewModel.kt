package aj.dev.event.view.checkin.vm

import aj.dev.event.data.model.Temperature
import aj.dev.event.listener.LoadingHandlerListener
import aj.dev.event.view.checkin.exception.ValidateEmailException
import aj.dev.event.view.checkin.exception.ValidateNameException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventCheckinViewModel @Inject constructor(private val eventsAPI: EventsCheckinMethods) :
    ViewModel(), LoadingHandlerListener {

    private val _events = MutableLiveData<Temperature?>()
    val events: LiveData<Temperature?> = _events

    private val _checkInResult = MutableLiveData<Boolean?>()
    val checkInResult: LiveData<Boolean?> = _checkInResult

    private val _errorService = MutableLiveData<String?>()
    val errorService: LiveData<String?> = _errorService
    private val _errorName = MutableLiveData<String?>()
    val errorName: LiveData<String?> = _errorName
    private val _errorEmail = MutableLiveData<String?>()
    val errorEmail: LiveData<String?> = _errorEmail

    private val _loading = MutableLiveData<Boolean?>()
    override val loading: LiveData<Boolean?> = _loading

    fun checkIn(id: Long, name: String?, email: String?) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val nameValidated = validateName(name)
                val emailValidated = validateEmail(email)
                eventsAPI.checkIn(id, nameValidated, emailValidated)
                _checkInResult.value = true
            } catch (e: ValidateNameException) {
                _errorName.value = e.message
            } catch (e: ValidateEmailException) {
                _errorEmail.value = e.message
            } catch (e: Exception) {
                _errorService.value = "Não foi possível fazer Check-In nesse evento."
            }
            _loading.value = false
        }
    }

    private fun validateEmail(email: String?): String {
        val emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}".toRegex()
        email?.let {
            if (it.isNotBlank() && it.isNotEmpty() && emailRegex.matches(email))
                return it
        }
        throw ValidateEmailException("Email inválido")
    }

    private fun validateName(name: String?): String {
        name?.let {
            if (it.isNotBlank() && it.isNotEmpty())
                return it
        }
        throw ValidateNameException("Nome inválido")
    }

    fun clearErrorName() {
        _errorName.value = null
    }

    fun clearErrorEmail() {
        _errorEmail.value = null
    }

    fun clearErrorService() {
        _errorService.value = null
    }

    override fun clearLoading() {
        _loading.value = null
    }

    override fun showLoading() {
        _loading.value = true
    }

    override fun hideLoading() {
        _loading.value = false
    }

    fun clearCheckInResult() {
        _checkInResult.value = null
    }

}