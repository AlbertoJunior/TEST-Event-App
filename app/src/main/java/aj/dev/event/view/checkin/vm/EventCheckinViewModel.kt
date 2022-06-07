package aj.dev.event.view.checkin.vm

import aj.dev.event.R
import aj.dev.event.listener.LoadingHandlerListener
import aj.dev.event.listener.ProviderHandler
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
class EventCheckinViewModel @Inject constructor(
    private val eventsAPI: EventsCheckinMethods,
    private val resource: ProviderHandler
) : ViewModel(), LoadingHandlerListener {

    private val _checkInResult = MutableLiveData<String?>()
    val checkInResult: LiveData<String?> = _checkInResult
    private val _errorService = MutableLiveData<String?>()
    val errorService: LiveData<String?> = _errorService
    private val _errorName = MutableLiveData<String?>()
    val errorName: LiveData<String?> = _errorName
    private val _errorEmail = MutableLiveData<String?>()
    val errorEmail: LiveData<String?> = _errorEmail
    private val _loading = MutableLiveData<Boolean?>()
    override val loading: LiveData<Boolean?> = _loading

    companion object {
        private val EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}".toRegex()
    }

    fun checkIn(id: Long, name: String?, email: String?) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val nameValidated = validateName(name)
                val emailValidated = validateEmail(email)
                eventsAPI.checkIn(id, nameValidated, emailValidated)
                _checkInResult.value = resource.getString(R.string.check_in_success)
            } catch (e: ValidateNameException) {
                _errorName.value = e.message
            } catch (e: ValidateEmailException) {
                _errorEmail.value = e.message
            } catch (e: Exception) {
                _errorService.value = resource.getString(R.string.check_in_error_service)
            }
            _loading.value = false
        }
    }

    private fun validateEmail(email: String?): String {
        email?.let {
            if (EMAIL_REGEX.matches(email))
                return it
        }

        throw ValidateEmailException(resource.getString(R.string.email_error))
    }

    private fun validateName(name: String?): String {
        name?.let {
            if (it.isNotBlank() && it.isNotEmpty())
                return it
        }
        throw ValidateNameException(resource.getString(R.string.name_error))
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