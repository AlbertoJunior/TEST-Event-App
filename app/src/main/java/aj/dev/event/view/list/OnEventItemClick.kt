package aj.dev.event.view.list

import aj.dev.event.view.list.vm.TemperatureListPresenter

interface OnEventItemClick {
    fun onEventItemClicked(item: TemperatureListPresenter)
}