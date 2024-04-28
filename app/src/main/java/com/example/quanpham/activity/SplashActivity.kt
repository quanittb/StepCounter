package com.example.quanpham.activity

import DateUtils.getEndOfDay
import DateUtils.getEndOfDayMinus
import DateUtils.getStartOfDay
import DateUtils.getStartOfDayMinus
import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.databinding.ActivitySplashBinding
import com.example.quanpham.fragment.HomeFragment
import com.example.quanpham.language.LanguageUtil
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.services.ResetStepForegroundService
import com.example.quanpham.utility.Constant


class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    companion object {
        var IS_PUSH = false
        fun startMain(context: Context, clearTask : Boolean ){
            val intent = Intent(context, SplashActivity::class.java).apply {
                if(clearTask){
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(intent)
        }
    }

    override fun getViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun createView() {
        LanguageUtil.setupLanguage(this)
        createNotificationChannel()
        getStepsDay()
//        scheduleAlarm(this)
        if(auth.currentUser != null){
            pushData()
        }
        else
            openNextScreen()
    }
//    private fun scheduleAlarm(context: Context) {
//        registerReceiver(ResetReceiver(), IntentFilter("android.intent.action.DATE_CHANGED"))
//        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(context, ResetReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, 0)
//        calendar.set(Calendar.MINUTE, 0)
//        calendar.set(Calendar.SECOND, 0)
//        calendar.add(Calendar.DAY_OF_MONTH,1)
//
//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
//    }
    fun pushData(){
        var intent = Intent(this, ResetStepForegroundService::class.java)
        ContextCompat.startForegroundService(this, intent)
        openNextScreen()
        IS_PUSH = true
    }
    private fun getStepsDay(){
        SharedPreferenceUtils.dayStep = database.stepDao().getStepsDay(
            getStartOfDay(System.currentTimeMillis()),
            getEndOfDay(System.currentTimeMillis())
        )
        SharedPreferenceUtils.yesterdayStep = database.stepDao().getStepsDay(
            getStartOfDayMinus(System.currentTimeMillis(),1),
            getEndOfDayMinus(System.currentTimeMillis(),1)
        ) - SharedPreferenceUtils.dayStep
        HomeFragment.currentStep.postValue(SharedPreferenceUtils.dayStep.toInt())
    }

    private fun hasPostNotificationGranted(): Boolean {
        return isAPI33OrHigher() && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val notificationPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {

        }


    private fun openNextScreen() {
        Handler().postDelayed({
            if (SharedPreferenceUtils.firstOpenApp)
                LanguageActivity.start(this)
            else
                if (auth.currentUser == null) {
                    SignInActivity.start(this, true)
                } else {
                    MainActivity.startMain(this, true)
                }

        }, 2000)
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constant.CHANNEL_ID_STEP,
                "CHANNEL_ID_STEP",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description ="CHANNEL_ID_STEP"

            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager?.createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constant.CHANNEL_ID_ALARM,
                "CHANNEL_ID_ALARM",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description ="CHANNEL_ID_ALARM"

            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager?.createNotificationChannel(channel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constant.CHANNEL_ID_UPDATE,
                "CHANNEL_ID_UPDATE",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description ="CHANNEL_ID_UPDATE"

            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager?.createNotificationChannel(channel)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constant.CHANNEL_ID_INFO_STEP,
                "CHANNEL_ID_INFO_STEP",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description ="CHANNEL_ID_INFO_STEP"

            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager?.createNotificationChannel(channel)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constant.CHANNEL_ID_STEP_GOAL,
                "CHANNEL_ID_STEP_GOAL",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description ="CHANNEL_ID_STEP_GOAL"

            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager?.createNotificationChannel(channel)
        }
    }


//    fun addDB(){
//        var auth = Firebase.auth
//        var db = FirebaseDatabase.getInstance().getReference("Users")
//        val userId = auth.currentUser!!.uid
//        val userName = auth.currentUser!!.email
//        val displayName = auth.currentUser!!.displayName
//        val user = Users(userId,userName,displayName,true,20,160f)
//        db.child(userId).setValue(user)
//    }
}