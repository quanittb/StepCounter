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
import com.example.quanpham.notilock.InfoStepNotiLock
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.NotificationManager
import java.util.Calendar

class InfoStepReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val fullscreenIntent = Intent(context, InfoStepNotiLock::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, fullscreenIntent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_CANCEL_CURRENT)
        val contentIntent = Intent(context, SplashActivity::class.java)

        val pendingIntent1 = PendingIntent.getActivity(
            context, 1234, contentIntent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_CANCEL_CURRENT
        )
        val remoteViewCollapse =
            RemoteViews(context.packageName, R.layout.layout_noti_info_step_collapse).apply {
                setOnClickPendingIntent(R.id.iv_add_1,pendingIntent1)
                setOnClickPendingIntent(R.id.root_info_step_1,pendingIntent1)
                setTextColor(R.id.tv_content, ContextCompat.getColor(context, R.color.text_color_notify))

            }
        val remoteViewExpand =
            RemoteViews(context.packageName, R.layout.layout_noti_info_step_expand).apply {
                setOnClickPendingIntent(R.id.iv_add_info_2,pendingIntent1)
                setOnClickPendingIntent(R.id.root_info_step_2,pendingIntent1)
                setTextColor(R.id.tv_content, ContextCompat.getColor(context, R.color.text_color_notify))
                setTextColor(R.id.title, ContextCompat.getColor(context, R.color.text_color_notify))
                setTextColor(R.id.title_day, ContextCompat.getColor(context, R.color.text_color_notify))
                setTextColor(R.id.title_week, ContextCompat.getColor(context, R.color.text_color_notify))
                setTextColor(R.id.title_month, ContextCompat.getColor(context, R.color.text_color_notify))
                setTextViewText(R.id.txt_day, calculateTotalStepInDay())
                setTextViewText(R.id.txt_week_2, calculatorStepWeek())
                setTextViewText(R.id.txt_month_2, calculateTotalStepOfMonth())
            }

        val notificationBuilder = NotificationCompat.Builder(context, Constant.CHANNEL_ID_INFO_STEP)
            .setSmallIcon(R.mipmap.ic_app_launcher_q)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setCustomContentView(remoteViewCollapse)
            .setCustomBigContentView(remoteViewExpand)
            .setFullScreenIntent(pendingIntent, true)
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        NotificationManager.cancelNotification(context, NotificationManager.FULLSCREEN_REMINDER_NOTIFICATION_ID_INFO_STEP, notificationBuilder.build())
        NotificationManager.sendNotification(context, NotificationManager.FULLSCREEN_REMINDER_NOTIFICATION_ID_INFO_STEP, notificationBuilder.build())

        if(isScreenLocked(context)){
            NotificationManager.showNotificationInfoStep(context,hour,minute)
        }
    }
    private fun isScreenLocked(context: Context): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager?
        return keyguardManager != null && keyguardManager.isKeyguardLocked
    }
    private fun calculatorStepWeek(): String{
        val currentWeek: MutableList<Int> = mutableListOf()
        val dayOfYear = getDayOfYear()
        currentWeek.clear()
        getCurrentWeekInList(dayOfYear)?.let { currentWeek.addAll(ArrayList(it)) }
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR,currentWeek.first())
        val startOfWeek = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR,7)
        val endOfWeek = calendar.timeInMillis
        return database.stepDao().getStepsDay(DateUtils.getStartOfDay(startOfWeek), DateUtils.getEndOfDay(endOfWeek)).toString()
    }
    private fun getCurrentWeekInList(dayOfYear: Int): MutableList<Int>? {
        val listWeek: MutableList<MutableList<Int>> = mutableListOf()
        listWeek.addAll(generateWeeksInYear(getCurrentYear())) // list các ngày tuần hiện tại
        for (index in 0 until listWeek.size) {
            val item = listWeek[index]
            if (item[0] <= dayOfYear && item[item.size - 1] >= dayOfYear) {
                return item
            }
        }
        return null
    }

    private fun calculateTotalStepOfMonth(): String{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,1)
        val startOfMonth = calendar.timeInMillis
        calendar.add(Calendar.MONTH,1)
        calendar.add(Calendar.DAY_OF_YEAR,-1)
        val endOfMonth = calendar.timeInMillis
        return database.stepDao().getStepsDay(DateUtils.getStartOfDay(startOfMonth), DateUtils.getEndOfDay(endOfMonth)).toString()
    }
    private fun calculateTotalStepInDay() : String{
        val calendar = Calendar.getInstance()
        return database.stepDao().getStepsDay(DateUtils.getStartOfDay(calendar.timeInMillis), DateUtils.getEndOfDay(calendar.timeInMillis)).toString()
    }
}