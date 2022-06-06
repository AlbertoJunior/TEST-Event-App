package aj.dev.event.listener

import androidx.lifecycle.LiveData

interface ErrorHandlerListener {
    fun clearError()
    val error : LiveData<String?>
}