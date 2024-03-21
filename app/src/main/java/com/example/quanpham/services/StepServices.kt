package com.example.quanpham.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.quanpham.R
import com.example.quanpham.db.AppDatabase
import com.example.quanpham.db.model.Steps
import com.example.quanpham.fragment.HomeFragment
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.utility.getStartOfHour
import com.example.quanpham.utility.logD
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date


class StepServices : Service() , SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var accelerator: Sensor? = null
    private var numSteps = 0
    private var countStep = 0
    private var countTime = 0
    private var currentTime = 0
    private var countMax = 0
    private var step = Steps()
    private var hourNow = 0
    private var thread :Thread? = null
    private lateinit var notification: NotificationCompat.Builder
    lateinit var database: AppDatabase


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        stepCount()
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
    }

    @SuppressLint("RemoteViewLayout")
    private fun stepCount() {
        SharedPreferenceUtils.dayStep++
        val currentTime = Date()
        var steps = database.stepDao().getStepsHour(getStartOfHour(System.currentTimeMillis()), System.currentTimeMillis())
        if(steps == null)
            database.stepDao().insert(Steps(null,1,currentTime,2,null,null))
        else{
        steps.step++
        steps.activeTime = steps.activeTime?.plus(2)
        database.stepDao().updateStep(steps)
        logD("ddaya laf step cua hour: ${steps.step}")

        }
        HomeFragment.currentStep.postValue(SharedPreferenceUtils.dayStep.toInt())
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
        sensorManager!!.unregisterListener(this)
    }

}