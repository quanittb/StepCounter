package com.example.quanpham.dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.databinding.ReportDropDownMenuBinding
object ReportDropDownMenu {

    const val WEEK = "WEEK"
    const val MONTH = "MONTH"
    const val DAY = "DAY"
    fun showDropDown(
        activity: BaseActivity<*>,
        view: View,
        timeType: String,
        callback: (timeType : String) -> Unit
    ) {
        val layoutInflater: LayoutInflater =
            activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ReportDropDownMenuBinding.inflate(layoutInflater)
        val popupView: View = binding.root
        val popupWindow = PopupWindow(popupView, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)

        when (timeType) {
            WEEK -> binding.week.isChecked = true
            MONTH -> binding.month.isChecked = true
            DAY -> binding.day.isChecked = true

            else -> binding.week.isChecked = true
        }
        binding.week.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked) {
                    popupWindow.dismiss()
                    callback(WEEK)
                }
            }
        }

        binding.month.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked) {
                    popupWindow.dismiss()
                    callback(MONTH)
                }
            }
        }

        binding.day.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked) {
                    popupWindow.dismiss()
                    callback(DAY)
                }
            }
        }


        popupWindow.isOutsideTouchable = true
        popupWindow.showAsDropDown(view)
    }
}