package com.example.quanpham.activity

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
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.services.ResetReceiver
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.getEndOfDay
import com.example.quanpham.utility.getEndOfDayMinus
import com.example.quanpham.utility.getStartOfDay
import com.example.quanpham.utility.getStartOfDayMinus
import java.util.Calendar
import kotlin.concurrent.thread


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
        getStepsDay()
        openNextScreen()
    }
    private fun getStepsDay(){
        SharedPreferenceUtils.dayStep = database.stepDao().getStepsDay(
            getStartOfDay(System.currentTimeMillis()),
            getEndOfDay(System.currentTimeMillis())
        )
        SharedPreferenceUtils.yesterdayStep = database.stepDao().getStepsDay(
            getStartOfDayMinus(System.currentTimeMillis(),1),
            getEndOfDayMinus(System.currentTimeMillis(),1)
        )
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel2 = NotificationChannel(
                Constant.CHANNEL_ID_UPDATE,
                "CHANNEL_ID_UPDATE",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel2.description ="CHANNEL_ID_UPDATE"

            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager?.createNotificationChannel(channel2)
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