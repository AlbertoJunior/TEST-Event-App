package aj.dev.event.vm

import aj.dev.event.data.model.Temperature
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private val eventsAPI: EventsMethods) : ViewModel() {

    private val _events = MutableLiveData<List<Temperature>?>()
    val events: LiveData<List<Temperature>?> = _events

    fun fetchEvents() {
        viewModelScope.launch {
            try {
                _events.value = eventsAPI.fetchEvents()
            } catch (e: Exception) {
                Log.e("TES", "teste")
                _events.value = null
            }
        }
    }

}