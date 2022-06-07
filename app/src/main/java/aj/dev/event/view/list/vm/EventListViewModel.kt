package aj.dev.event.view.list.vm

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
class EventListViewModel @Inject constructor(
    private val eventsAPI: EventsListMethods,
    private val resource: ProviderHandler
) : ViewModel(), NavigateHandlerListener, LoadingHandlerListener {

    private val _events = MutableLiveData<List<TemperatureListPresenter>?>()
    val events: LiveData<List<TemperatureListPresenter>?> = _events

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _loading = MutableLiveData<Boolean?>()
    override val loading: LiveData<Boolean?> = _loading

    private val _navigate = MutableLiveData<Long?>()
    override val navigate: LiveData<Long?> = _navigate

    fun fetchEvents() {
        viewModelScope.launch {
            try {
                _events.value = eventsAPI.fetchEvents()
            } catch (e: Exception) {
                _error.value = resource.getString(R.string.error_fetch_events)
                _events.value = null
            }
        }
    }

    fun onItemClicked(itemId: String?) {
        try {
            itemId?.toLong()
        } catch (e: Exception) {
            0L
        }.also { id ->
            if (id == 0L) {
                _error.value = resource.getString(R.string.error_detail_event)
            } else {
                _navigate.value = id
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    override fun clearNavigate() {
        _navigate.value = null
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

}