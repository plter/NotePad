package com.topyunp.notepadandroid.helpers

import java.util.*

object DateHelper {

    fun formatTime(t: Int): String {
        return if (t >= 10) {
            t.toString()
        } else {
            "0$t"
        }
    }

    fun convertToReadable(d: Calendar): String {
        return "${formatTime(d.get(Calendar.YEAR))}-${formatTime(d.get(Calendar.MONTH) + 1)}-${
        formatTime(d.get(Calendar.DAY_OF_MONTH))} ${formatTime(d.get(Calendar.HOUR_OF_DAY))}:${
        formatTime(d.get(Calendar.MINUTE))}:${formatTime(d.get(Calendar.SECOND))}"
    }

    fun getCurrentTimeString(
        dateSp: String = "-",
        sp: String = " ",
        timeSp: String = ":"
    ): String {
        val c = Calendar.getInstance()
        return c[Calendar.YEAR].toString() + dateSp +
                formatTime(c[Calendar.MONTH] + 1) + dateSp +
                formatTime(c[Calendar.DAY_OF_MONTH]) + sp +
                formatTime(c[Calendar.HOUR_OF_DAY]) + timeSp +
                formatTime(c[Calendar.MINUTE]) + timeSp +
                formatTime(c[Calendar.SECOND])
    }
}