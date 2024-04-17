package com.example.quanpham.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import com.example.quanpham.R
import com.example.quanpham.activity.SplashActivity
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentReminderBinding
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.services.ReminderReceiver
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.NotificationManager
import com.example.quanpham.utility.logD
import com.example.quanpham.utility.rxbus.ReminderUpdateTime
import com.example.quanpham.utility.rxbus.RxBus
import com.mobiai.app.ui.dialog.TimeDialog
import java.util.Calendar

class ReminderDetailFragment : BaseFragment<FragmentReminderBinding>() {
    companion object {
        fun instance(): ReminderDetailFragment {
            return newInstance(ReminderDetailFragment::class.java)
        }
    }

    private var checkAlarm = SharedPreferenceUtils.alarm
    private var bottomSheetTimeDialog: TimeDialog? = null

    @SuppressLint("SetTextI18n")
    override fun initView() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(requireContext(), ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE)
        checkAutoToggle()
        if (SharedPreferenceUtils.minuteAlarm >= 10)
            binding.txtTime.text =
                "${SharedPreferenceUtils.hourAlarm}:${SharedPreferenceUtils.minuteAlarm}"
        else
            binding.txtTime.text =
                "${SharedPreferenceUtils.hourAlarm}:0${SharedPreferenceUtils.minuteAlarm}"
        binding.txtTime.setOnClickListener {
            openTimeBottomSheet()
        }
        binding.turnOnReminder.setOnClickListener {
            checkAlarm = !checkAlarm
            checkAutoToggle()
        }
        binding.btnSave.setOnClickListener {
            RxBus.publish(ReminderUpdateTime(binding.txtTime.text.toString()))
            SharedPreferenceUtils.alarm = checkAlarm
            if (SharedPreferenceUtils.alarm) {
                setAlarm(alarmManager,pendingIntent)
            } else {
                cancelAlarm(alarmManager,pendingIntent)
            }
           handlerBackPressed()
        }
        binding.btnClose.setOnClickListener {
            handlerBackPressed()
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReminderBinding {
        return FragmentReminderBinding.inflate(inflater, container, false)
    }
    override fun handlerBackPressed() {
        super.handlerBackPressed()
        hideFragment(this@ReminderDetailFragment)
    }
    private fun openTimeBottomSheet() {
        if (bottomSheetTimeDialog == null) {
            bottomSheetTimeDialog = TimeDialog(
                requireContext(),
                object : TimeDialog.OnClickBottomSheetListener {
                    @SuppressLint("SetTextI18n")
                    override fun onClickSaveFrom() {
                        if (SharedPreferenceUtils.minuteAlarm >= 10)
                            binding.txtTime.text =
                                "${SharedPreferenceUtils.hourAlarm}:${SharedPreferenceUtils.minuteAlarm}"
                        else
                            binding.txtTime.text =
                                "${SharedPreferenceUtils.hourAlarm}:0${SharedPreferenceUtils.minuteAlarm}"
                    }

                })
        }
        bottomSheetTimeDialog?.checkShowBottomSheet()
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(alarmManager : AlarmManager, pendingIntent: PendingIntent){
        val calendar = Calendar.getInstance()
        calendar.apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, SharedPreferenceUtils.hourAlarm)
            set(Calendar.MINUTE, SharedPreferenceUtils.minuteAlarm)
            set(Calendar.SECOND, 0)
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

            alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
    private fun cancelAlarm(alarmManager: AlarmManager, pendingIntent: PendingIntent){
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkAutoToggle() {
        if (checkAlarm) {
            binding.turnOnReminder.setImageDrawable(resources.getDrawable(R.drawable.toogle_on_reminder_svg))
        } else {
            binding.turnOnReminder.setImageDrawable(resources.getDrawable(R.drawable.toogle_off_reminder))
        }
    }

}


