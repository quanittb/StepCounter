
package com.example.quanpham.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.quanpham.R
import com.example.quanpham.db.AppDatabase
import com.example.quanpham.db.model.Steps
import com.example.quanpham.fragment.HomeFragment
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.NotificationManager
import com.example.quanpham.utility.getEndOfDay
import com.example.quanpham.utility.getEndOfYesterday
import com.example.quanpham.utility.getStartOfDay
import com.example.quanpham.utility.getStartOfYesterday
import com.example.quanpham.utility.logD
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log

class ResetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var intent = Intent(context,ResetStepForegroundService::class.java)
        ContextCompat.startForegroundService(context!!, intent)
        logD("ddax chayj vào receiver")
    }

}
