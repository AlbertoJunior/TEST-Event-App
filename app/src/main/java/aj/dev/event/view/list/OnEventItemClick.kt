package aj.dev.event.view.list

import aj.dev.event.data.model.Temperature

interface OnEventItemClick {
    fun onEventItemClicked(item: Temperature)
}