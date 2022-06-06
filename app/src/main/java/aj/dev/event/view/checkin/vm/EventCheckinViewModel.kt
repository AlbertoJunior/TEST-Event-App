package aj.dev.event.view.checkin.vm

import aj.dev.event.data.model.Temperature
import aj.dev.event.listener.LoadingHandlerListener
import aj.dev.event.view.checkin.ValidateFormException
import aj.dev.event.view.detail.vm.EventsCheckinMethods
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

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _loadingShow = MutableLiveData<Boolean?>()
    override val loading: LiveData<Boolean?> = _loadingShow

    fun checkIn(id: Long, name: String?, email: String?) {
        viewModelScope.launch {
            _loadingShow.value = true
            try {
                val nameValidated = validateName(name)
                val emailValidated = validateEmail(email)
                eventsAPI.checkIn(id, nameValidated, emailValidated)
            } catch (e: ValidateFormException) {
                _error.value = e.message
            } catch (e: Exception) {
                _error.value = "Não foi possível fazer Check-In nesse evento."
            }
            _loadingShow.value = false
        }
    }

    private fun validateEmail(email: String?): String {
        val emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}".toRegex()
        email?.let {
            if (it.isNotBlank() && it.isNotEmpty() && emailRegex.matches(email))
                return it
        }
        throw ValidateFormException("Email inválido")
    }

    private fun validateName(name: String?): String {
        name?.let {
            if (it.isNotBlank() && it.isNotEmpty())
                return it
        }
        throw ValidateFormException("Nome inválido")
    }

    fun clearError() {
        _error.value = null
    }

    override fun clearLoading() {
        _loadingShow.value = null
    }

    override fun showLoading() {
        _loadingShow.value = true
    }

    override fun hideLoading() {
        _loadingShow.value = false
    }

}