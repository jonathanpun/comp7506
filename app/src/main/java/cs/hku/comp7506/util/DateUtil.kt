package cs.hku.comp7506.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
     fun getDateTime(second:Long): String? {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(second* 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}