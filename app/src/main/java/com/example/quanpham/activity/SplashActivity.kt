package com.example.quanpham.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Handler
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.databinding.ActivitySplashBinding
import com.example.quanpham.lib.SharedPreferenceUtils


class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    companion object {
        private const val TIME_OUT = 30000L
        private const val TIME_DELAY = 5000L
        private val TAG = SplashActivity::class.java.name
    }

    override fun getViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun createView() {
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


    fun openNextScreen() {
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
}