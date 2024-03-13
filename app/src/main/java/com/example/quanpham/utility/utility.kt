package com.example.quanpham.utility

import android.widget.Toast
import com.example.quanpham.app.AppContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun showToast(mess:Any){
    Toast.makeText(AppContext.context, mess.toString(), Toast.LENGTH_LONG).show()
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