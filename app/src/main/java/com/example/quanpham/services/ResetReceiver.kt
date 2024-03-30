
package com.example.quanpham.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.core.content.ContextCompat
import com.example.quanpham.utility.logD
import com.example.quanpham.utility.rxbus.StopUpdate
import com.example.quanpham.utility.rxbus.listenEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class ResetReceiver : BroadcastReceiver() {
    val compositeDisposable = CompositeDisposable()
    override fun onReceive(context: Context?, intent: Intent?) {
        var intent = Intent(context,ResetStepForegroundService::class.java)
        ContextCompat.startForegroundService(context!!, intent)
        compositeDisposable.add(listenEvent({
            when(it){
                is StopUpdate -> {
                    Handler().postDelayed({
                        context.stopService(intent)
                        logD("đã chạy vào stop ")
                    },1000)
                }
            }
        }))
    }

}
