package aj.dev.event.listener

import androidx.lifecycle.LiveData

interface LoadingHandlerListener {
    fun clearLoading()
    fun showLoading()
    fun hideLoading()
    val loading: LiveData<Boolean?>
}