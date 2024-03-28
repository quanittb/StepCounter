package com.example.quanpham.permission

import android.Manifest
import android.os.Build
import androidx.activity.result.ActivityResultLauncher

/**
 *  this class is used for check/ request device shared-storage permission on all android version
 **/
object StoragePermissionUtils {

    /**
     *  check read_permission to access media files that other apps create
     */
    fun isAPI33OrHigher(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun requestNotifyPermission(resultLauncher: ActivityResultLauncher<Array<String>>){
        PermissionUtils.requestMultiplePermission(
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            resultLauncher
        )
    }

    fun requestActivityRecognitionLogPermission(resultLauncher: ActivityResultLauncher<Array<String>>){
        PermissionUtils.requestMultiplePermission(
            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
            resultLauncher
        )
    }

}