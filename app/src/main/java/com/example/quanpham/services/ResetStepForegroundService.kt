package com.example.quanpham.services

import DateUtils.getStartOfDay
import DateUtils.getStartOfDayMinus
import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.quanpham.R
import com.example.quanpham.activity.SplashActivity
import com.example.quanpham.db.AppDatabase
import com.example.quanpham.db.model.Rank
import com.example.quanpham.db.model.Steps
import com.example.quanpham.db.model.StepsFirebase
import com.example.quanpham.fragment.HomeFragment
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.NotificationManager
import com.example.quanpham.utility.rxbus.RxBus
import com.example.quanpham.utility.rxbus.StopUpdate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ResetStepForegroundService() : Service() {
    private lateinit var notification: NotificationCompat.Builder
    val mdatabase = Firebase.database
    var pushData: PushData? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        update()
        super.onCreate()
    }
    fun setCallback(callback: PushData) {
        this.pushData = callback
    }
    // Khai báo một LocalBinder bên trong Service
    inner class LocalBinder : Binder() {
        fun getService(): ResetStepForegroundService = this@ResetStepForegroundService
    }

    private val binder = LocalBinder()
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun update() {
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
            startForeground(
                NotificationManager.FULLSCREEN_UPDATE_DATA_NOTIFICATION_ID,
                notification.build()
            )
        }
        var database: AppDatabase = Room.databaseBuilder(
            this,
            AppDatabase::class.java, Constant.STEP_DB
        )
            .allowMainThreadQueries()
            .build()
        var listRecord: List<Steps> = database.stepDao().getRecordStepsToPush(
            getStartOfDayMinus(System.currentTimeMillis(), 1),
            System.currentTimeMillis()
        )
        HomeFragment.currentStep.postValue(SharedPreferenceUtils.dayStep.toInt())

        // xử lý push step
        val refStep = mdatabase.getReference(Constant.KEY_STEP)
            .child(Firebase.auth.currentUser!!.uid)
        refStep.addValueEventListener(object : ValueEventListener {
            var count = 0
            var checkOnly = false
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!checkOnly) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val data = snapshot.getValue(StepsFirebase::class.java)
                            data?.let {
                                listRecord.forEach { it1 ->
                                    if (it1.startTime.time == it.startTime.time && !it1.isPush) {
                                        it1.isPush = true
                                        database.stepDao().updateStep(it1)
                                        refStep.child(snapshot.key!!).setValue(it1)
                                        return@forEach
                                    }
                                }
                            }
                            count++
                            if (count == dataSnapshot.childrenCount.toInt()) {
                                for (i in listRecord) {
                                    if (!i.isPush) {
                                        refStep.push().setValue(i)
                                    }
                                    if (listRecord.indexOf(i) == listRecord.lastIndex) {
                                        database.stepDao().updateStatePushForStep()
                                        RxBus.publish(StopUpdate())
                                    }
                                }
                            }

                        }
                    } else {
                        for (i in listRecord) {
                            if (!i.isPush) {
                                refStep.push().setValue(i)
                            }
                            if (listRecord.indexOf(i) == listRecord.lastIndex) {
                                database.stepDao().updateStatePushForStep()
                                RxBus.publish(StopUpdate())
                                pushData?.pushComplete()
                            }
                        }

                    }
                    checkOnly = true
                    Handler().postDelayed({
                        stopForeground(true)
                        stopSelf()},2000)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })
        // Xử lý cho rank
        val refRank = mdatabase.getReference(Constant.KEY_RANK)
            .child(getStartOfDay(System.currentTimeMillis()).toString())

        refRank.child(Firebase.auth.currentUser!!.uid).setValue(
            Rank(
                Firebase.auth.currentUser!!.uid,
                SharedPreferenceUtils.name,
                SharedPreferenceUtils.dayStep.toInt()
            )
        )

    }


}
interface PushData{
    fun pushComplete()
}