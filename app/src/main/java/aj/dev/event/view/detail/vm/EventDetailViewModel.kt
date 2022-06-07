package aj.dev.event.view.detail.vm

import aj.dev.event.R
import aj.dev.event.listener.LoadingHandlerListener
import aj.dev.event.listener.NavigateHandlerListener
import aj.dev.event.listener.ProviderHandler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val eventsAPI: EventsDetailMethods,
    private val resource: ProviderHandler
) : ViewModel(), LoadingHandlerListener, NavigateHandlerListener {

    private val _event = MutableLiveData<EventDetailPresenter?>()
    val event: LiveData<EventDetailPresenter?> = _event

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _loadingShow = MutableLiveData<Boolean?>()
    override val loading: LiveData<Boolean?> = _loadingShow

    private val _navigate = MutableLiveData<Long?>()
    override val navigate: LiveData<Long?> = _navigate

    fun fetchEvent(id: Long) {
        viewModelScope.launch {
            _loadingShow.value = true
            try {
                _event.value = eventsAPI.fetchEventDetail(id)
            } catch (e: Exception) {
                _error.value = resource.getString(R.string.error_fetch_detail_event)
                _event.value = null
            }
            _loadingShow.value = false
        }
    }

    fun callCheckIn(id: String?) {
        id?.let {
            try {
                _navigate.value = it.toLong()
            } catch (e: NumberFormatException) {
                _error.value = resource.getString(R.string.error_check_in_event)
            }
        }
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

    override fun clearNavigate() {
        _navigate.value = null
    }

}