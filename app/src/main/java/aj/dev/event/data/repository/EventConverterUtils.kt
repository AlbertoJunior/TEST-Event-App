package aj.dev.event.data.repository

import aj.dev.event.data.model.Temperature
import aj.dev.event.view.detail.vm.TemperatureDetailPresenter
import aj.dev.event.view.list.vm.TemperatureListPresenter
import java.text.SimpleDateFormat
import java.util.*

internal class EventConverterUtils {
    companion object {
        private val DATE_FORMAT =
            SimpleDateFormat("dd-M-yyyy", Locale("pt", "BR"))

        private fun longToDateString(value: Long) = DATE_FORMAT.format(Date(value))

        fun temperatureToTemperatureListPresenter(item: Temperature) = TemperatureListPresenter(
            item.id,
            item.title,
            item.price,
            longToDateString(item.date)
        )

        fun temperatureToTemperatureDetailPresenter(item: Temperature) = TemperatureDetailPresenter(
            item.id,
            item.people ?: emptyList(),
            item.title,
            item.description,
            item.price,
            longToDateString(item.date),
            item.image,
            item.longitude,
            item.latitude
        )
    }
}