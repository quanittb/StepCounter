package com.example.quanpham.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.quanpham.R
import com.example.quanpham.activity.MainActivity
import com.example.quanpham.activity.SplashActivity
import com.example.quanpham.db.AppDatabase
import com.example.quanpham.db.model.Steps
import com.example.quanpham.fragment.HomeFragment
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.utility.Constant.CALO_STEP
import com.example.quanpham.utility.Constant.CHANNEL_ID_STEP
import com.example.quanpham.utility.Constant.KcalOne
import com.example.quanpham.utility.NotificationManager
import com.example.quanpham.utility.NotificationManager.Companion.FULLSCREEN_REMINDER_NOTIFICATION_ID
import com.example.quanpham.utility.convertSecondToTime
import com.example.quanpham.utility.getStartOfHour
import java.util.Date


class StepServices : Service() , SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var accelerator: Sensor? = null
    private var numSteps = 0
    lateinit var database: AppDatabase
    private lateinit var notification: NotificationCompat.Builder
    var currentStep = SharedPreferenceUtils.dayStep.toInt()
    var targetStep = SharedPreferenceUtils.targetStep.toInt()


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notification()
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerator = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        sensorManager!!.registerListener(
            this, accelerator,
            SensorManager.SENSOR_DELAY_FASTEST
        )
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "step-db"
        )
            .allowMainThreadQueries()
            .build()
        notification()
    }
    private fun stepCount() {
        SharedPreferenceUtils.dayStep++
        if(SharedPreferenceUtils.dayStep > SharedPreferenceUtils.targetStep)
            SharedPreferenceUtils.targetStep = SharedPreferenceUtils.dayStep
        currentStep = SharedPreferenceUtils.dayStep.toInt()
        targetStep = SharedPreferenceUtils.targetStep.toInt()
        val currentTime = Date()
        var steps = database.stepDao().getStepsHour(getStartOfHour(System.currentTimeMillis()), System.currentTimeMillis())
        if(steps == null)
            database.stepDao().insert(Steps(null,1,currentTime,1, CALO_STEP.toLong(),(SharedPreferenceUtils.stepLength*0.001).toLong()))
        else{
            steps.step++
            steps.calo = (steps.step * CALO_STEP).toLong()
            steps.distance += (SharedPreferenceUtils.stepLength*0.001).toLong()
            steps.activeTime = steps.activeTime.plus(1)
            database.stepDao().updateStep(steps)
        }
        HomeFragment.currentStep.postValue(SharedPreferenceUtils.dayStep.toInt())
        notification()
    }
    @SuppressLint("RemoteViewLayout")
    private fun notification(){
        val remoteViews = RemoteViews(packageName, R.layout.layout_notify_all)
        remoteViews.setTextViewText(R.id.tv_number_step, SharedPreferenceUtils.dayStep.toString())
        remoteViews.setTextViewText(R.id.tv_number_calo,(SharedPreferenceUtils.dayStep * KcalOne).toInt().toString())
        remoteViews.setTextViewText(R.id.tv_timer,(convertSecondToTime(SharedPreferenceUtils.dayStep)))
        remoteViews.setProgressBar(R.id.determinateBar,targetStep,currentStep,false)
        if (currentStep< targetStep/4){
            remoteViews.setProgressBar(R.id.determinateBar,targetStep,currentStep,false)
            remoteViews.setViewVisibility(R.id.determinateBar, View.VISIBLE)
            remoteViews.setViewVisibility(R.id.determinateBar2, View.GONE)
            remoteViews.setViewVisibility(R.id.determinateBar3, View.GONE)
            remoteViews.setViewVisibility(R.id.determinateBar4, View.GONE)
        }
        else if (currentStep <targetStep/2){
            remoteViews.setProgressBar(R.id.determinateBar2,targetStep,currentStep,false)
            remoteViews.setViewVisibility(R.id.determinateBar2, View.VISIBLE)

            remoteViews.setViewVisibility(R.id.determinateBar, View.GONE)
            remoteViews.setViewVisibility(R.id.determinateBar3, View.GONE)
            remoteViews.setViewVisibility(R.id.determinateBar4, View.GONE)

        }
        else if (currentStep <= (targetStep / 4)*3){
            remoteViews.setProgressBar(R.id.determinateBar3,targetStep,currentStep,false)
            remoteViews.setViewVisibility(R.id.determinateBar3, View.VISIBLE)
            remoteViews.setViewVisibility(R.id.determinateBar2, View.GONE)
            remoteViews.setViewVisibility(R.id.determinateBar, View.GONE)
            remoteViews.setViewVisibility(R.id.determinateBar4, View.GONE)
        }
        else{
            remoteViews.setProgressBar(R.id.determinateBar4,targetStep,currentStep,false)
            remoteViews.setViewVisibility(R.id.determinateBar4, View.VISIBLE)
            remoteViews.setViewVisibility(R.id.determinateBar2, View.GONE)
            remoteViews.setViewVisibility(R.id.determinateBar3, View.GONE)
            remoteViews.setViewVisibility(R.id.determinateBar, View.GONE)
        }

        val appIntent = Intent(this, SplashActivity::class.java)

        val appPendingIntent = PendingIntent.getActivity(
            this,
            0,
            appIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, CHANNEL_ID_STEP)
        } else {
            NotificationCompat.Builder(this)
        }
            .setSmallIcon(R.mipmap.ic_app_launcher_q)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.ic_app_launcher_q))
            .setContentTitle(this.getString(R.string.app_name))
            .setContentText(currentStep.toString())
            .setAutoCancel(false)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(currentStep.toString())
            )
            .setContentIntent(appPendingIntent)
            .setContent(remoteViews)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(Notification.PRIORITY_HIGH)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startForeground(FULLSCREEN_REMINDER_NOTIFICATION_ID,notification.build())
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        numSteps++
        stepCount()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        NotificationManager.cancelNotification(this, FULLSCREEN_REMINDER_NOTIFICATION_ID, notification.build())
        sensorManager!!.unregisterListener(this)
    }

}