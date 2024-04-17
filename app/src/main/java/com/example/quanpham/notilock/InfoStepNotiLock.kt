package com.example.quanpham.notilock

import DateUtils.generateWeeksInYear
import DateUtils.getCurrentYear
import DateUtils.getDayOfYear
import android.content.Intent
import com.example.quanpham.activity.SplashActivity
import com.example.quanpham.app.database
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.databinding.ActivityInfoStepNotiLockBinding
import com.example.quanpham.lib.SharedPreferenceUtils
import java.util.Calendar


class InfoStepNotiLock : BaseActivity<ActivityInfoStepNotiLockBinding>() {

    override fun getViewBinding(): ActivityInfoStepNotiLockBinding = ActivityInfoStepNotiLockBinding.inflate(layoutInflater)

    override fun createView() {
        binding.txtDay.text = SharedPreferenceUtils.dayStep.toString()
        binding.txtWeek.text = calculatorStepWeek()
        binding.txtMonth.text = calculateTotalStepOfMonth()
        binding.btnAdd.setOnClickListener{
            val intent = Intent(this@InfoStepNotiLock, SplashActivity::class.java)
            startActivity(intent)
        }
        calculatorStepWeek()
        calculateTotalStepOfMonth()
        showOnLockscreen()
        dismissKeyguard()
    }
    private fun calculatorStepWeek(): String{
        val currentWeek: MutableList<Int> = mutableListOf()
        val dayOfYear = getDayOfYear()
        currentWeek.clear()
        getCurrentWeekInList(dayOfYear)?.let { currentWeek.addAll(ArrayList(it)) }
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR,currentWeek.first())
        val startOfWeek = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR,7)
        val endOfWeek = calendar.timeInMillis
        return database.stepDao().getStepsDay(DateUtils.getStartOfDay(startOfWeek), DateUtils.getEndOfDay(endOfWeek)).toString()
    }
    private fun getCurrentWeekInList(dayOfYear: Int): MutableList<Int>? {
        val listWeek: MutableList<MutableList<Int>> = mutableListOf()
        listWeek.addAll(generateWeeksInYear(getCurrentYear())) // list các ngày tuần hiện tại
        for (index in 0 until listWeek.size) {
            val item = listWeek[index]
            if (item[0] <= dayOfYear && item[item.size - 1] >= dayOfYear) {
                return item
            }
        }
        return null
    }
    private fun calculateTotalStepOfMonth(): String{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,1)
        val startOfMonth = calendar.timeInMillis
        calendar.add(Calendar.MONTH,1)
        calendar.add(Calendar.DAY_OF_YEAR,-1)
        val endOfMonth = calendar.timeInMillis
        return database.stepDao().getStepsDay(DateUtils.getStartOfDay(startOfMonth), DateUtils.getEndOfDay(endOfMonth)).toString()
    }
}