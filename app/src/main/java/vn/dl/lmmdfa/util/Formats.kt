package vn.dl.lmmdfa.util

import java.text.SimpleDateFormat
import java.util.*

object Formats {

    @JvmStatic
    fun formatDate(source: Long): String {
        val sdf = SimpleDateFormat("HH:mm dd/MM/yyyy E", Locale.getDefault())
        return try {
            sdf.format(source)
        } catch (e: IllegalArgumentException) {
            ""
        }
    }
}