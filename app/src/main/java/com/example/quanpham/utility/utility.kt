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

fun getHour(inputDate: String = getTimeNow()): Int {
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    val date = inputDate.let { format.parse(it) }

    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.HOUR_OF_DAY)
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
