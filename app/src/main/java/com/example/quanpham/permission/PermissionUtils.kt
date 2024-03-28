package com.example.quanpham.permission

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionUtils {

    fun isPermissionGrandted(
        context:Any?,
        permissionArray: Array<String>,
    ): Boolean{
        var isGranted = true

        if (CurrentAPI.isApi23orHigher()){
            for (permission in permissionArray){
                when (context){
                    is AppCompatActivity -> {
                        isGranted = ContextCompat.checkSelfPermission(
                            context,
                            permission
                        ) == PackageManager.PERMISSION_GRANTED
                    }
                    is Fragment -> {
                        isGranted = ContextCompat.checkSelfPermission(
                            context.requireContext(),
                            permission
                        ) == PackageManager.PERMISSION_GRANTED
                    }
                }
                if (!isGranted){
                    break
                }
            }
        }
        return  isGranted
    }


    fun requestPermission(permissionArray: String, resultLauncher: ActivityResultLauncher<String>? = null){
        resultLauncher?.launch(permissionArray)
    }
    fun requestMultiplePermission(permissionArray: Array<String>, resultLauncher: ActivityResultLauncher<Array<String>>? = null){
        resultLauncher?.launch(permissionArray)
    }
}