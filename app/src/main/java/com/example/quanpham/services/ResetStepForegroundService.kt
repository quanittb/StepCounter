package com.example.quanpham.services

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.quanpham.R
import com.example.quanpham.activity.SplashActivity
import com.example.quanpham.db.AppDatabase
import com.example.quanpham.db.model.Steps
import com.example.quanpham.fragment.HomeFragment
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.NotificationManager
import com.example.quanpham.utility.getEndOfDayMinus
import com.example.quanpham.utility.getStartOfDayMinus
import com.example.quanpham.utility.logD
import com.example.quanpham.utility.rxbus.RxBus
import com.example.quanpham.utility.rxbus.StopUpdate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.thread

class ResetStepForegroundService : Service() {
    private lateinit var notification: NotificationCompat.Builder
    val mdatabase = Firebase.database
    private lateinit var serviceIntent:Intent

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        serviceIntent = Intent(this, StepServices::class.java)
        update()
        super.onCreate()
    }
    fun update(){
        val appIntent = Intent(this, SplashActivity::class.java)
        val appPendingIntent = PendingIntent.getActivity(
            this,
            0,
            appIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, Constant.CHANNEL_ID_UPDATE)
        } else {
            NotificationCompat.Builder(this)
        }
            .setSmallIcon(R.mipmap.ic_app_launcher_q)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_app_launcher_q))
            .setContentTitle(this.getString(R.string.app_name))
            .setContentText("Update data")
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(appPendingIntent)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startForeground(NotificationManager.FULLSCREEN_UPDATE_DATA_NOTIFICATION_ID,notification.build())
        }
        var database: AppDatabase = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "step-db"
        )
            .allowMainThreadQueries()
            .build()
        var listRecord: List<Steps> = database.stepDao().getRecordStepsDay(
            getStartOfDayMinus(System.currentTimeMillis() + 2000,1),
            getEndOfDayMinus(System.currentTimeMillis() + 2000,1)
        )
        logD("${listRecord[0].startTime}")
        HomeFragment.currentStep.postValue(SharedPreferenceUtils.dayStep.toInt())
        if(SharedPreferenceUtils.startStep){
                stopService()
                startService()
        }
        val ref = mdatabase.getReference("Steps")
            .child(Firebase.auth.currentUser!!.uid)
        for (i in listRecord) {
            ref.push()
                .setValue(i).addOnSuccessListener {
                    logD("đã đẩy thành công $i")
                    if(i.id == listRecord[listRecord.lastIndex].id) {
                        logD("đã chạy RXbus")
                        RxBus.publish(StopUpdate())
                    }
                }
        }

    }
    private fun startService() {
        startService((serviceIntent))
    }
    private fun stopService(){
        stopService(serviceIntent)
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}