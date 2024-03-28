package com.mobiai.app.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.quanpham.R
import com.example.quanpham.databinding.DialogMetricBinding
import com.example.quanpham.lib.SharedPreferenceUtils
import com.google.android.material.bottomsheet.BottomSheetDialog


class MetricDialog(context: Context, private val listener : OnClickBottomSheetListener) : BottomSheetDialog(context) {
    val binding = DialogMetricBinding.inflate(layoutInflater)
    var chooseUnit : Boolean = SharedPreferenceUtils.unit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        chooseUnit = SharedPreferenceUtils.unit
        if(!chooseUnit)
        {
            showClickImgUnit(binding.txtLbs)
        }
        else{
            showClickImgUnit(binding.txtKg)

        }
        binding.txtLbs.setOnClickListener {
            showClickImgUnit(binding.txtLbs)
        }

        binding.txtKg.setOnClickListener {
            showClickImgUnit(binding.txtKg)
        }


        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
//            val df = DecimalFormat("#.#")
//            if(SharedPreferenceUtils.unit && !chooseUnit && SharedPreferenceUtils.height !=0f && SharedPreferenceUtils.weight !=0f)
//            {
//                SharedPreferenceUtils.height = df.format(SharedPreferenceUtils.height / 30.48).toFloat()
//                SharedPreferenceUtils.stepLength = df.format(SharedPreferenceUtils.stepLength / 30.48).toFloat()
//                SharedPreferenceUtils.weight = df.format(SharedPreferenceUtils.weight * 2.20462).toFloat()
//
//            }
//            if(!SharedPreferenceUtils.unit && chooseUnit && SharedPreferenceUtils.height !=0f && SharedPreferenceUtils.weight !=0f ){
//                SharedPreferenceUtils.height = df.format(SharedPreferenceUtils.height * 30.48).toFloat()
//                SharedPreferenceUtils.stepLength = df.format(SharedPreferenceUtils.stepLength * 30.48).toFloat()
//                SharedPreferenceUtils.weight = df.format(SharedPreferenceUtils.weight / 2.20462).toFloat()
//
//            }
            SharedPreferenceUtils.unit = chooseUnit
            listener.onClickSaveFrom()
            checkHideBottomSheet()
        }
    }

    private fun showClickImgUnit(view: View) {
        val typeface = ResourcesCompat.getFont(context, R.font.mukta_semi_bold)
        val typeface_2 = ResourcesCompat.getFont(context, R.font.mukta_regular)
        if (view == binding.txtKg) {
            chooseUnit = true
            binding.txtKg.setTextColor(context.resources.getColor(R.color.selected_text_color_sex))
            binding.txtKg.textSize = 22f
            binding.txtKg.typeface = typeface
            binding.txtKg.setBackgroundResource(R.drawable.bg_selected_dialog)
            binding.txtLbs.setBackgroundResource(R.drawable.bg_unselected_dialog)
            binding.txtLbs.setTextColor(context.resources.getColor(R.color.un_selected_text_color_sex))
            binding.txtLbs.textSize = 20f
            binding.txtLbs.typeface = typeface_2

        } else {
            chooseUnit = false
            binding.txtLbs.setTextColor(context.resources.getColor(R.color.selected_text_color_sex))
            binding.txtLbs.setBackgroundResource(R.drawable.bg_selected_dialog)
            binding.txtLbs.textSize = 22f
            binding.txtLbs.typeface = typeface
            binding.txtKg.setTextColor(context.resources.getColor(R.color.un_selected_text_color_sex))
            binding.txtKg.setBackgroundResource(R.drawable.bg_unselected_dialog)
            binding.txtKg.textSize = 20f
            binding.txtKg.typeface = typeface_2
        }
    }
    fun checkShowBottomSheet(){
        if (!isShowing) {
            show()
        }
    }
    fun checkHideBottomSheet(){
        if (isShowing) {
            dismiss()
        }
    }
    interface OnClickBottomSheetListener{
        fun onClickSaveFrom()
    }

}