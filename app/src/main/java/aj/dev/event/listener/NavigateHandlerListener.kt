package aj.dev.event.listener

import androidx.lifecycle.LiveData

interface NavigateHandlerListener {
    fun clearNavigate()
    val navigate: LiveData<Long?>
}