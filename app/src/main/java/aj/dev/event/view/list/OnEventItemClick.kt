package aj.dev.event.view.list

import aj.dev.event.view.list.vm.EventListPresenter

interface OnEventItemClick {
    fun onEventItemClicked(item: EventListPresenter)
}