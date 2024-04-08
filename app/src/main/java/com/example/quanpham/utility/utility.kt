
package com.example.quanpham.utility

import android.util.Log
import android.widget.Toast
import androidx.room.TypeConverter
import com.example.quanpham.app.AppContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun showToast(mess:Any){
    Toast.makeText(AppContext.context, mess.toString(), Toast.LENGTH_LONG).show()
}

fun logD(value: Any){
    Log.d("abcd", "$value")
}

fun formatTimes(timestamp: Any?): String {
    if (timestamp is Long) {
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(date)
    }
    return ""
}

fun getTimeNow(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}
fun getDayOfMonth(timeInMillis: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    return dayOfMonth
}
fun getMonthOfYear(timeInMillis: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    val month = calendar.get(Calendar.MONTH) + 1
    return month
}
fun getDayOfYear(timeInMillis: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
    return dayOfYear
}
fun getHour(inputDate: String = getTimeNow()): Int {
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    val date = inputDate.let { format.parse(it) }

    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.HOUR_OF_DAY)
}
fun getStartOfYear(year: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, Calendar.JANUARY)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun getEndOfYear(year: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, Calendar.DECEMBER)
    calendar.set(Calendar.DAY_OF_MONTH, 31)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.timeInMillis
}
fun getStartOfDay(currentTime: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentTime
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}
fun getEndOfDay(currentTime: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentTime
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.timeInMillis
}
fun getStartDayOfYear(dayOfYear : Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_YEAR,dayOfYear)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}
fun getDayOfYear(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.DAY_OF_YEAR)
}
fun getEndDayOfYear(dayOfYear : Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_YEAR,dayOfYear)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.timeInMillis
}
fun getStartOfDayMinus(currentTime: Long, dayMinus : Int): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentTime
    calendar.add(Calendar.DAY_OF_YEAR,-dayMinus)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}
fun getEndOfDayMinus(currentTime: Long,dayMinus : Int): Long {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_MONTH,-dayMinus)
    calendar.timeInMillis = currentTime
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.timeInMillis
}
fun getStartOfHour(currentTime: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentTime
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}
fun convertSecondToTime(second:Long) : String{
    if(second < 60)
        return "${second}s"
    else if(second < 3600)
        return "${second/60}m ${second%60}s"
    return "${second / 3600}h ${(second%3600)/60}m"
}
fun formatNumbers(inputNums : Float) : String{
    val formattedNumber = String.format("%.2f", inputNums).replace(",", ".")
    return if (formattedNumber.endsWith(".00")) {
        formattedNumber.replace(".00", "")
    } else if (formattedNumber.endsWith("0")) {
        formattedNumber.substring(0, formattedNumber.length - 1)
    } else {
        formattedNumber
    }



}
