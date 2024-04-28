package com.example.quanpham.services

import DateUtils.generateWeeksInYear
import DateUtils.getCurrentYear
import DateUtils.getDayOfYear
import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.quanpham.R
import com.example.quanpham.activity.SplashActivity
import com.example.quanpham.app.database
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.notilock.InfoStepNotiLock
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.NotificationManager
import java.util.Calendar

class StepGoalReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val contentIntent = Intent(context, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 12,contentIntent , PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_CANCEL_CURRENT)
        var content =
        if(SharedPreferenceUtils.dayStep >= SharedPreferenceUtils.targetStep)
            context.getString(R.string.pass_noti)
        else context.getString(R.string.not_pass_noti)

        val notificationBuilder = NotificationCompat.Builder(context, Constant.CHANNEL_ID_STEP_GOAL)
            .setSmallIcon(R.mipmap.ic_app_launcher_q)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(pendingIntent, true)
            .setContentTitle(content)
            .setContentIntent(pendingIntent)
        NotificationManager.cancelNotification(context, NotificationManager.FULLSCREEN_REMINDER_NOTIFICATION_ID_STEP_GOAL, notificationBuilder.build())
        NotificationManager.sendNotification(context, NotificationManager.FULLSCREEN_REMINDER_NOTIFICATION_ID_STEP_GOAL, notificationBuilder.build())
        NotificationManager.showNotificationStepGoal(context)
    }
}