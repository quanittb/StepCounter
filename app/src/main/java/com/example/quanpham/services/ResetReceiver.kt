//package com.example.quanpham.services
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.util.Log
//import androidx.room.Room
//import com.example.quanpham.db.AppDatabase
//import com.example.quanpham.fragment.HomeFragment
//import com.example.quanpham.lib.SharedPreferenceUtils
//import com.example.quanpham.utility.getStartOfDay
//import com.example.quanpham.utility.getStartOfHour
//import com.example.quanpham.utility.logD
//
//class ResetReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
//        var database: AppDatabase = Room.databaseBuilder(
//            context!!,
//            AppDatabase::class.java, "step-db"
//        )
//            .allowMainThreadQueries()
//            .build()
//        SharedPreferenceUtils.yesterdayStep = SharedPreferenceUtils.dayStep
//        var steps = database.stepDao().getStepsDay(getStartOfDay(System.currentTimeMillis()+1000), System.currentTimeMillis()+2000)
//        SharedPreferenceUtils.dayStep = steps
//        HomeFragment.currentStep.postValue(SharedPreferenceUtils.dayStep.toInt())
//        logD("đã chạy vào receiver")
//    }
//
//}
package com.example.quanpham.services

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room
import com.example.quanpham.db.AppDatabase
import com.example.quanpham.db.model.Steps
import com.example.quanpham.fragment.HomeFragment
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.utility.getEndOfDay
import com.example.quanpham.utility.getEndOfYesterday
import com.example.quanpham.utility.getStartOfDay
import com.example.quanpham.utility.getStartOfYesterday
import com.example.quanpham.utility.logD
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log

class ResetReceiver : BroadcastReceiver() {

    val mdatabase = Firebase.database


//    fun getCurrentDate(): String {
//        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//        val currentDate = Date()
//        return dateFormat.format(currentDate)
//    }

    override fun onReceive(context: Context?, intent: Intent?) {

        var database: AppDatabase = Room.databaseBuilder(
            context!!,
            AppDatabase::class.java, "step-db"
        )
            .allowMainThreadQueries()
            .build()
        var listRecord: List<Steps> = database.stepDao().getRecordStepsDay(
            getStartOfYesterday(System.currentTimeMillis() + 2000),
            getEndOfYesterday(System.currentTimeMillis() + 2000)
        )
    logD("${listRecord[0].startTime}")
        SharedPreferenceUtils.yesterdayStep = SharedPreferenceUtils.dayStep
        var steps = database.stepDao().getStepsDay(
            getStartOfDay(System.currentTimeMillis() + 2000),
            getEndOfDay(System.currentTimeMillis() + 2000))
        SharedPreferenceUtils.dayStep = steps
        HomeFragment.currentStep.postValue(SharedPreferenceUtils.dayStep.toInt())

        val ref = mdatabase.getReference("Steps")
            .child(Firebase.auth.currentUser!!.uid)
        for (i in listRecord) {
            ref.push()
                .setValue(i).addOnSuccessListener {
                    logD("đã đẩy thành công $i")
                }

        }
        logD("đã chạy vào rcv")


        HomeFragment.currentStep.postValue(SharedPreferenceUtils.dayStep.toInt())
    }

}