package com.example.quanpham.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.example.quanpham.R
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentHomeBinding
import com.example.quanpham.db.model.Steps
import com.example.quanpham.db.model.Weights
import com.example.quanpham.dialog.StepGoalBottomDialog
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.permission.StoragePermissionUtils
import com.example.quanpham.services.StepServices
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.getHour
import com.example.quanpham.utility.makeGone
import com.example.quanpham.utility.makeVisible
import com.example.quanpham.utility.showToast
import com.mobiai.app.ui.dialog.PermissionDialog
import com.mobiai.app.ui.dialog.PermissionReject1Dialog
import com.mobiai.app.ui.dialog.PermissionReject2Dialog
import com.mobiai.app.ui.dialog.PermissionRequiredDialog
import java.util.Date

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    companion object {
        var currentStep = MutableLiveData<Int>()
        fun instance(): HomeFragment {
            return newInstance(HomeFragment::class.java)
        }
    }
    private lateinit var serviceIntent:Intent
    private var bottomSheetStepGoalDialog: StepGoalBottomDialog? = null
    private var permissionDialog: PermissionDialog? = null
    private var permissionRequiredDialog: PermissionRequiredDialog? = null
    private var permissionReject1Dialog: PermissionReject1Dialog? = null
    private var goToSettingDialog: PermissionReject2Dialog? = null
    private var isGotoSettingActivity = false


    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        val permissions = arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION
        )
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ){
                showRequirePermissionActivityDialog()
            }
        }
        if (SharedPreferenceUtils.setOrStartGoal){
            binding.lnGoal.makeGone()
        }
        usLoggin?.observe(this@HomeFragment) {
            serviceIntent = Intent(requireContext(), StepServices::class.java)
            setListener()
            setWelcome()
            if(SharedPreferenceUtils.startStep)
                startService()
        }
//        addDB()
    }
    private fun setListener(){
        binding.tvContentHeader.text = SharedPreferenceUtils.yesterdayStep.toString()
        binding.tvStepRealTime.text = SharedPreferenceUtils.dayStep.toString()
        currentStep.observe(this@HomeFragment){
            binding.tvStepRealTime.text = it.toString()
        }
        binding.ivStepStart.setOnClickListener{
            if(!SharedPreferenceUtils.startStep){
                SharedPreferenceUtils.startStep = true
                startService()
                binding.ivStepStart.setImageResource(R.drawable.ic_pause_step_home)
                binding.tvPause.makeGone()
                binding.lnTarget.makeVisible()
            }
            else{
                stopService()
                SharedPreferenceUtils.startStep = false
                binding.ivStepStart.setImageResource(R.drawable.ic_step_pause)
                binding.tvPause.makeVisible()
                binding.lnTarget.makeGone()
                }
            }
        binding.btnStepGoal.setOnClickListener {
            openStepGoalBottomSheet()
            binding.lnGoal.makeGone()
            SharedPreferenceUtils.setOrStartGoal = false
        }
        binding.ivClose.setOnClickListener {
            binding.lnGoal.makeGone()
        }
    }
    private fun startService() {
        requireContext().startService((serviceIntent))
    }
    private fun stopService() {
        requireContext().stopService(serviceIntent)
    }
    override fun onStop() {
        super.onStop()
    }

    fun addDB() {
        try {
            val currentUser = auth.currentUser
            currentUser?.let { user ->
                fbDatabase.getReference(Constant.KEY_WEIGHT)
                    .child(user.uid)
                    .push()
                    .setValue(Weights(1, 55F, ""))
                    .addOnSuccessListener {
                        showToast("Thành công")
                        Log.d("abcd", "đã chạy")
                    }
                    .addOnFailureListener {
                        showToast(it.message.toString())
                        Log.d("abcd", "lỗi ${it.message}")
                    }
                val currentTime = Date()
                fbDatabase.getReference(Constant.KEY_STEP)
                    .child(user.uid)
                    .push()
                    .setValue(Steps(1, 1000, currentTime , 20, 150, 2))
            } ?: run {
                // Handle case where currentUser is null
                showToast("User not authenticated")
            }
        } catch (e: Exception) {
            Log.d("abcd", "Err ${e.message}")
        }
    }

    private fun setWelcome(){
        val hour = getHour()
        when(hour){
            in 5..12 -> binding.tvWellCome.text = getString(R.string.good_morning)
            in 13..17 ->binding.tvWellCome.text = getString(R.string.good_afternoon)
           else ->binding.tvWellCome.text = getString(R.string.good_evening)
        }
    }
    private fun openStepGoalBottomSheet() {
        if (bottomSheetStepGoalDialog == null) {
            bottomSheetStepGoalDialog = StepGoalBottomDialog(
                requireContext(),
                object : StepGoalBottomDialog.OnClickBottomSheetListener {
                    override fun onClickSaveFrom() {
                        binding.btnStepGoal.text = SharedPreferenceUtils.targetStep.toString()
                    }

                })
        }

        bottomSheetStepGoalDialog?.checkShowBottomSheet()
    }


    private val requestMultipleActivityPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            //todo contact
            isGotoSettingActivity = false
            if (SharedPreferenceUtils.startStep){
                startService()
            }
        } else {
            isGotoSettingActivity = true
            showRequirePermissionRejectActivityDialog()
        }
    }
    private fun checkPermission() {
        val permissions = arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION
        )
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if(SharedPreferenceUtils.firstPermissionRequired){
                    showRequirePermissionDialog()
                    SharedPreferenceUtils.firstPermissionRequired = false
                }
            }
        }
    }
    private fun checkPermissionOnResume(permission:String) {
        if (permission == Manifest.permission.ACTIVITY_RECOGNITION){
            isGotoSettingActivity = false
            if (ActivityCompat.checkSelfPermission(requireContext(),permission) == PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED ){

                }
                else{
                    SharedPreferenceUtils.checkCountRejectPermission  ++
                    if(SharedPreferenceUtils.checkCountRejectPermission >2)
                        showRequirePermissionReject2ActivityDialog()
                    else
                        showRequirePermissionRejectActivityDialog()
                }
            }
            else{
                checkPermission()
                if(SharedPreferenceUtils.checkCountRejectPermission >2)
                    showRequirePermissionReject2ActivityDialog()
                else
                    showRequirePermissionRejectActivityDialog()
            }
        }
    }
    private fun showRequirePermissionRejectActivityDialog() {
        if (permissionDialog !=null)
            permissionDialog!!.hide()
        if (permissionReject1Dialog== null){
            permissionReject1Dialog = PermissionReject1Dialog(
                requireContext(),
            ) {
                StoragePermissionUtils.requestActivityRecognitionLogPermission(requestMultipleActivityPermissionsLauncher)
                isGotoSettingActivity = true
                SharedPreferenceUtils.checkCountRejectPermission ++
            }
        }
        if (!permissionReject1Dialog!!.isShowing) {
            permissionReject1Dialog!!.show()
        }
    }
    private fun showRequirePermissionReject2ActivityDialog() {
        if (permissionDialog !=null)
            permissionDialog!!.hide()
        if (permissionReject1Dialog !=null)
            permissionReject1Dialog!!.hide()
        if (goToSettingDialog == null){
            goToSettingDialog = PermissionReject2Dialog(
                requireContext(),
            ) {
                isGotoSettingActivity = true
                gotoSetting()
            }
        }
        if (!goToSettingDialog!!.isShowing) {
            goToSettingDialog!!.show()
        }
    }
    private fun showRequirePermissionDialog() {
        if (permissionDialog !=null)
            permissionDialog!!.hide()
        if (permissionReject1Dialog !=null)
            permissionReject1Dialog!!.hide()
        if (goToSettingDialog !=null)
            goToSettingDialog!!.hide()
        if (permissionRequiredDialog== null){
            permissionRequiredDialog = PermissionRequiredDialog(
                requireContext(),
            ) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:${requireContext().packageName}")
                startActivity(intent)
            }
        }
        if (!permissionRequiredDialog!!.isShowing) {
            permissionRequiredDialog!!.show()
        }
    }
    private fun showRequirePermissionActivityDialog() {
        if (permissionDialog== null){
            permissionDialog = PermissionDialog(
                requireContext(),
            ) {
                StoragePermissionUtils.requestActivityRecognitionLogPermission(requestMultipleActivityPermissionsLauncher)
                isGotoSettingActivity = true
                SharedPreferenceUtils.checkCountRejectPermission ++
            }
        }
        if(SharedPreferenceUtils.checkCountRejectPermission>1)
            showRequirePermissionRejectActivityDialog()
        else if (!permissionDialog!!.isShowing && SharedPreferenceUtils.firstPermissionRequired) {
            permissionDialog!!.show()
        }
    }

    override fun onResume() {
        if (isGotoSettingActivity) {
            isGotoSettingActivity = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACTIVITY_RECOGNITION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    if (SharedPreferenceUtils.startStep){
                        startService()
                    }
                    Handler().postDelayed({
                        if(SharedPreferenceUtils.firstPermissionRequired){
                            showRequirePermissionDialog()
                            SharedPreferenceUtils.firstPermissionRequired = false
                        }
                    }, 100)
                } else {
                    if(SharedPreferenceUtils.checkCountRejectPermission
                        >2)
                        showRequirePermissionReject2ActivityDialog()
                    else
                        showRequirePermissionRejectActivityDialog()
                }
            } else {
                checkPermission()
                if(SharedPreferenceUtils.checkCountRejectPermission >2)
                    showRequirePermissionReject2ActivityDialog()
                else
                    showRequirePermissionRejectActivityDialog()
            }
        } else if (isGotoSettingActivity) {
            checkPermissionOnResume(Manifest.permission.ACTIVITY_RECOGNITION)
        } else {
            checkPermission()
        }
        super.onResume()
    }

}