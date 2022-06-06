package aj.dev.event.view.detail.vm

import aj.dev.event.data.model.People

data class TemperatureDetailPresenter(
    val id: String,
    val people: List<People>,
    val title: String,
    val description: String,
    val price: Double,
    val date: String,
    val image: String,
    val longitude: Double,
    val latitude: Double,
)
