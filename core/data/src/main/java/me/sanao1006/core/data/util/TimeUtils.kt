package me.sanao1006.core.data.util

import android.content.Context
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import me.snao1006.res_value.ResString
import java.time.format.DateTimeFormatter

object TimeUtils {
    fun formatCreatedAt(createdAt: String): String {
        val instant = Instant.parse(createdAt)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        return localDateTime.toJavaLocalDateTime().format(formatter)
    }
}

fun getRelativeTimeString(context: Context, isoDateString: String): String {
    val dateTime = Instant.parse(isoDateString).toLocalDateTime(TimeZone.currentSystemDefault())
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val yearsBetween = now.year - dateTime.year
    val monthsBetween = (now.year - dateTime.year) * 12 + (now.monthNumber - dateTime.monthNumber)
    val daysBetween = now.date.dayOfYear - dateTime.date.dayOfYear
    val hoursBetween = now.hour - dateTime.hour
    val minutesBetween = now.minute - dateTime.minute
    return when {
        yearsBetween > 0 -> context.getString(ResString.date_year_ago, yearsBetween.toString())
        monthsBetween > 0 -> context.getString(ResString.date_month_ago, monthsBetween.toString())
        daysBetween > 0 -> context.getString(ResString.date_day_ago, daysBetween.toString())
        hoursBetween > 0 -> context.getString(ResString.date_hour_ago, hoursBetween.toString())
        minutesBetween > 0 -> context.getString(
            ResString.date_minute_ago,
            minutesBetween.toString()
        )

        else -> context.getString(ResString.date_now)
    }
}
