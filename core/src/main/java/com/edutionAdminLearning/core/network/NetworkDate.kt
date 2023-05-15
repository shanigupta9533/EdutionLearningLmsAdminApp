package com.edutionAdminLearning.core.network

import com.edutionAdminLearning.core.type.EMPTY
import com.edutionAdminLearning.core.utils.PwDateTime.now
import com.edutionAdminLearning.core.utils.PwDateTime.stringToDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

object NetworkDate {

    // API response date format
    // Sun, 12 Jun 2022 06:41:35 GMT
    //
    // API response server time
    // 2022-08-18T04:23:37+00:00

    private const val API_HEADER_DATE_FMT = "EEE, dd MMM yyyy HH:mm:ss z"
    private const val PW_SERVER_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ"
    const val PW_APP_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val PW_DATA_USAGE_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
    const val PW_DATE_PATTERN = "yyyy-MM-dd"
    const val PW_CALENDAR_DATE_PATTERN = "dd MMM yyyy"
    const val TIMER_DATE_PATTERN = "mm:ss"
    const val UTC = "UTC"
    private const val IST_TIME_ZONE = "Asia/Kolkata"
    private var isTimeSet: AtomicBoolean = AtomicBoolean(false)

    var saveDate: Date? = null
    var dateTimeOffSet: Long = 0


    private val _date = MutableStateFlow(now())
    val date = _date.asStateFlow()

    fun setDate(serverDateTime: String?) {
        _date.value = serverDateTime?.let { stringToDate(it, API_HEADER_DATE_FMT) } ?: now()
    }

    fun setServerTime(serverDateTime: String?) {
        if (isTimeSet.get()) return
        getIndianTimeZone(serverDateTime ?: getCurrentDate())?.let {
            if (saveDate != null && (it.before(saveDate) || it == saveDate)) return@let
            saveDate = it
            dateTimeOffSet = System.currentTimeMillis() - it.time
            isTimeSet.set(true)
        }
    }

    fun getCurrentDate(): String {
        val c = getCurrentServerDate()
        val df = SimpleDateFormat(PW_APP_DATE_PATTERN, Locale.getDefault())
        return df.format(c)
    }

    fun getUTCCurrentDate(): String {
        val c = getCurrentServerDate()
        val df = SimpleDateFormat(PW_APP_DATE_PATTERN, Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone(UTC)
        return df.format(c)
    }

    fun getTodayDate(): String {
        val c = getCurrentServerDate()
        val df = SimpleDateFormat(PW_DATE_PATTERN, Locale.getDefault())
        return df.format(c)
    }

    fun getPwCalendar(): Calendar {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat(PW_APP_DATE_PATTERN, Locale.getDefault())
        sdf.parse(getCurrentDate())?.let { cal.time = it }
        return cal
    }

    fun getDateFromTimestamp(timestamp: Long, datePattern : String): String {
        return try {
            val sdf = SimpleDateFormat(datePattern, Locale.getDefault())
            val netDate = Date(timestamp)
            sdf.format(netDate)
        } catch (e: Exception) {
            String.EMPTY
        }
    }

    fun getCurrentServerDate(): Date {
        return Date((System.currentTimeMillis() - dateTimeOffSet) + 500)
    }

    private fun getIndianTimeZone(serverDate: String): Date? {
        return try {

            val simpleDateFormat = SimpleDateFormat(PW_SERVER_DATE_PATTERN, Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getTimeZone(UTC)

            val indianDateFormat = SimpleDateFormat(PW_APP_DATE_PATTERN, Locale.getDefault())
            indianDateFormat.timeZone = TimeZone.getDefault()


            val timeStamp = simpleDateFormat.parse(serverDate)
            return if (timeStamp != null) {
                val date = indianDateFormat.format(timeStamp)
                indianDateFormat.parse(date)
            } else null

        } catch (e: Exception) {
            null
        }
    }

}