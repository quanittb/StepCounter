
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

fun formatNumbers(inputNums : Double) : String{
    val formattedNumber = String.format("%.2f", inputNums).replace(",", ".")
    return if (formattedNumber.endsWith(".00")) {
        formattedNumber.replace(".00", "")
    } else if (formattedNumber.endsWith("0")) {
        formattedNumber.substring(0, formattedNumber.length - 1)
    } else {
        formattedNumber
    }
}



