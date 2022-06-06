package aj.dev.event.data.model

import android.os.Build
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

data class Temperature(
    val people: List<Any?>,
    val date: Long,
    val description: String,
    val image: String,
    val longitude: Double,
    val latitude: Double,
    val price: Double,
    val title: String,
    val id: String
) : Serializable {
    fun getDateFormated(): String {
        return SimpleDateFormat("dd-M-yyyy", Locale("pt", "BR")).format(Date(date))
    }
}