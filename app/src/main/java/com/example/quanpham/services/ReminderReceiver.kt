package com.example.quanpham.services

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.quanpham.R
import com.example.quanpham.activity.SplashActivity
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.NotificationManager
import com.example.quanpham.utility.logD
import java.util.Calendar

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            showNotification(context)
        }
    }

    private fun showNotification(context: Context) {
        val stepYesterday = SharedPreferenceUtils.yesterdayStep
        val appIntent = Intent(context, SplashActivity::class.java)
        val appPendingIntent = PendingIntent.getActivity(
            context,
            0,
            appIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, Constant.CHANNEL_ID_ALARM)
        } else {
            NotificationCompat.Builder(context)
        }
        builder.setSmallIcon(R.mipmap.ic_app_launcher_q_round)
            .setContentTitle("${context.getString(R.string.yesterday_total_steps)} $stepYesterday")
            .setContentText(context.getString(R.string.check_your_walking_report))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(appPendingIntent)
            .setAutoCancel(true)
            .setOngoing(true)
            NotificationManager.sendNotification(context,
                NotificationManager.FULLSCREEN_ALARM_NOTIFICATION_ID, builder.build())

    }
}


