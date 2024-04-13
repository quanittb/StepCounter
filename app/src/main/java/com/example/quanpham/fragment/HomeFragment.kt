package com.example.quanpham.fragment

import DateUtils.convertSecondToTime
import DateUtils.getHour
import DateUtils.getStartOfDayMinus
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.quanpham.R
import com.example.quanpham.activity.SplashActivity
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentHomeBinding
import com.example.quanpham.dialog.StepGoalBottomDialog
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.model.Users
import com.example.quanpham.permission.StoragePermissionUtils
import com.example.quanpham.services.ResetStepForegroundService
import com.example.quanpham.services.StepServices
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.Constant.KcalOne
import com.example.quanpham.utility.makeGone
import com.example.quanpham.utility.makeVisible
import com.example.quanpham.utility.showToast
import com.mobiai.app.ui.dialog.PermissionDialog
import com.mobiai.app.ui.dialog.PermissionReject1Dialog
import com.mobiai.app.ui.dialog.PermissionReject2Dialog
import com.mobiai.app.ui.dialog.PermissionRequiredDialog

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    companion object {
        var currentStep = MutableLiveData<Int>()
        fun instance(): HomeFragment {
            return newInstance(HomeFragment::class.java)
        }
    }

    private lateinit var serviceIntent: Intent
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
            ) {
                showRequirePermissionActivityDialog()
            }
        }
        if (!SharedPreferenceUtils.setOrStartGoal) {
            binding.lnGoal.makeGone()
        }
        serviceIntent = Intent(requireContext(), StepServices::class.java)
        getLoginUser()
        if (!SplashActivity.IS_PUSH) {
            pushData()
        }
        setListener()
        setWelcome()
        updateUI()
        if (SharedPreferenceUtils.startStep) {
            Handler().postDelayed({
                startService()
            }, 1000)
        }
    }

    var user = Users()
    private fun getLoginUser() {
        if (auth.currentUser != null) {
            firestore.collection(Constant.KEY_USER)
                .document(auth.currentUser!!.uid)
                .get()
                .addOnSuccessListener {
                    user = it.toObject(Users::class.java)!!
                    SharedPreferenceUtils.name = user.name
                    SharedPreferenceUtils.height = user.height!!
                    SharedPreferenceUtils.selectSex = if (user.gender) 1 else 0  // nam 1 , nu 0
                    SharedPreferenceUtils.age = user.age!!
                    SharedPreferenceUtils.stepLength = user.height!! * 0.4f

                }
                .addOnFailureListener {
                    showToast(it.message.toString())
                }
        }

    }

    fun getDataSevenDayRecent() {
        var totalStep = database.stepDao().getStepsDay(
            getStartOfDayMinus(System.currentTimeMillis(), 7),
            System.currentTimeMillis()
        )
        binding.tvStepNow.text = "${totalStep / 7}"
    }

    fun updateUI() {
        binding.chart.setArcValue(
            SharedPreferenceUtils.targetStep.toFloat(),
            SharedPreferenceUtils.dayStep.toFloat()
        )
        val stepCountKm =
            (SharedPreferenceUtils.dayStep * (SharedPreferenceUtils.stepLength / 100000))
        val stepCountKcal = SharedPreferenceUtils.dayStep * KcalOne
        val formattedNumberKcal = String.format("%.1f", stepCountKcal)
        val formattedNumberKm = String.format("%.2f", stepCountKm)
        val activeTime = convertSecondToTime(SharedPreferenceUtils.dayStep)

        binding.tvKmHome.text = formattedNumberKm
        binding.tvKcalHome.text = formattedNumberKcal
        binding.tvTimerMinHome.text = activeTime
        getDataSevenDayRecent()

        if (SharedPreferenceUtils.dayStep > 0) {
            binding.ivFire.makeVisible()
        }
        if ((SharedPreferenceUtils.dayStep.toFloat() / SharedPreferenceUtils.targetStep) * 100 < 25f) {
            binding.ivFire.setImageResource(R.drawable.ic_fire_01)
        } else if ((SharedPreferenceUtils.dayStep.toFloat() / SharedPreferenceUtils.targetStep) * 100 < 50f) {
            binding.ivFire.setImageResource(R.drawable.ic_fire_02)
        } else if ((SharedPreferenceUtils.dayStep.toFloat() / SharedPreferenceUtils.targetStep) * 100 < 75f) {
            binding.ivFire.setImageResource(R.drawable.ic_fire_03)
        } else {
            binding.ivFire.setImageResource(R.drawable.ic_fire_04)
        }
    }

    private fun setListener() {
        binding.tvTarget.text = SharedPreferenceUtils.targetStep.toString()
        binding.tvContentHeader.text = SharedPreferenceUtils.yesterdayStep.toString()
        binding.tvStepRealTime.text = SharedPreferenceUtils.dayStep.toString()
        currentStep.observe(this@HomeFragment) {
            binding.tvStepRealTime.text = it.toString()
            updateUI()
        }
        binding.ivStepStart.setOnClickListener {
            if (!SharedPreferenceUtils.startStep) {
                SharedPreferenceUtils.startStep = true
                startService()
                binding.ivStepStart.setImageResource(R.drawable.ic_pause_step_home)
                binding.tvPause.makeGone()
                binding.lnTarget.makeVisible()
            } else {
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
        if (isAdded)
            requireContext().startService((serviceIntent))
    }

    private fun stopService() {
        if (isAdded)
            requireContext().stopService(serviceIntent)
    }

    override fun onStop() {
        super.onStop()
    }

    fun pushData() {
        var intent = Intent(requireActivity(), ResetStepForegroundService::class.java)
        ContextCompat.startForegroundService(requireContext(), intent)
    }

    private fun setWelcome() {
        val hour = getHour()
        when (hour) {
            in 5..12 -> binding.tvWellCome.text = getString(R.string.good_morning)
            in 13..17 -> binding.tvWellCome.text = getString(R.string.good_afternoon)
            else -> binding.tvWellCome.text = getString(R.string.good_evening)
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
            if (SharedPreferenceUtils.startStep) {
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
                if (SharedPreferenceUtils.firstPermissionRequired) {
                    showRequirePermissionDialog()
                    SharedPreferenceUtils.firstPermissionRequired = false
                }
            }
        }
    }

    private fun checkPermissionOnResume(permission: String) {
        if (permission == Manifest.permission.ACTIVITY_RECOGNITION) {
            isGotoSettingActivity = false
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACTIVITY_RECOGNITION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                } else {
                    SharedPreferenceUtils.checkCountRejectPermission++
                    if (SharedPreferenceUtils.checkCountRejectPermission > 2)
                        showRequirePermissionReject2ActivityDialog()
                    else
                        showRequirePermissionRejectActivityDialog()
                }
            } else {
                checkPermission()
                if (SharedPreferenceUtils.checkCountRejectPermission > 2)
                    showRequirePermissionReject2ActivityDialog()
                else
                    showRequirePermissionRejectActivityDialog()
            }
        }
    }

    private fun showRequirePermissionRejectActivityDialog() {
        if (permissionDialog != null)
            permissionDialog!!.hide()
        if (permissionReject1Dialog == null) {
            permissionReject1Dialog = PermissionReject1Dialog(
                requireContext(),
            ) {
                StoragePermissionUtils.requestActivityRecognitionLogPermission(
                    requestMultipleActivityPermissionsLauncher
                )
                isGotoSettingActivity = true
                SharedPreferenceUtils.checkCountRejectPermission++
            }
        }
        if (!permissionReject1Dialog!!.isShowing) {
            permissionReject1Dialog!!.show()
        }
    }

    private fun showRequirePermissionReject2ActivityDialog() {
        if (permissionDialog != null)
            permissionDialog!!.hide()
        if (permissionReject1Dialog != null)
            permissionReject1Dialog!!.hide()
        if (goToSettingDialog == null) {
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
        if (permissionDialog != null)
            permissionDialog!!.hide()
        if (permissionReject1Dialog != null)
            permissionReject1Dialog!!.hide()
        if (goToSettingDialog != null)
            goToSettingDialog!!.hide()
        if (permissionRequiredDialog == null) {
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
        if (permissionDialog == null) {
            permissionDialog = PermissionDialog(
                requireContext(),
            ) {
                StoragePermissionUtils.requestActivityRecognitionLogPermission(
                    requestMultipleActivityPermissionsLauncher
                )
                isGotoSettingActivity = true
                SharedPreferenceUtils.checkCountRejectPermission++
            }
        }
        if (SharedPreferenceUtils.checkCountRejectPermission > 1)
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
                    if (SharedPreferenceUtils.startStep) {
                        startService()
                    }
                    Handler().postDelayed({
                        if (SharedPreferenceUtils.firstPermissionRequired) {
                            showRequirePermissionDialog()
                            SharedPreferenceUtils.firstPermissionRequired = false
                        }
                    }, 100)
                } else {
                    if (SharedPreferenceUtils.checkCountRejectPermission
                        > 2
                    )
                        showRequirePermissionReject2ActivityDialog()
                    else
                        showRequirePermissionRejectActivityDialog()
                }
            } else {
                checkPermission()
                if (SharedPreferenceUtils.checkCountRejectPermission > 2)
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