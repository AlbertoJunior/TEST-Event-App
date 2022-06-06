package aj.dev.event.listener

import androidx.lifecycle.LiveData

interface ErrorHandlerListener {
    fun clearError()
    val loading : LiveData<String?>
}