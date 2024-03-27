package com.example.quanpham.activity

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.databinding.ActivitySplashBinding
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.services.ResetReceiver
import com.example.quanpham.utility.Constant
import java.util.Calendar


class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    companion object {
        private const val TIME_OUT = 30000L
        private const val TIME_DELAY = 5000L
        private val TAG = SplashActivity::class.java.name
    }

    override fun getViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun createView() {
        createNotificationChannel()
        scheduleAlarm(this)
        openNextScreen()

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
//        addDB()
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
    }
    fun scheduleAlarm(context: Context) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ResetReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
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