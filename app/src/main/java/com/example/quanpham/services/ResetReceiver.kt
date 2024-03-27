package com.example.quanpham.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room
import com.example.quanpham.db.AppDatabase
import com.example.quanpham.fragment.HomeFragment
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.utility.getStartOfDay
import com.example.quanpham.utility.getStartOfHour

class ResetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var database: AppDatabase = Room.databaseBuilder(
            context!!,
            AppDatabase::class.java, "step-db"
        )
            .allowMainThreadQueries()
            .build()
        SharedPreferenceUtils.yesterdayStep = SharedPreferenceUtils.dayStep
        var steps = database.stepDao().getStepsDay(getStartOfDay(System.currentTimeMillis()+10), System.currentTimeMillis()+20)
        SharedPreferenceUtils.dayStep = steps
        HomeFragment.currentStep.postValue(SharedPreferenceUtils.dayStep.toInt())
    }

}