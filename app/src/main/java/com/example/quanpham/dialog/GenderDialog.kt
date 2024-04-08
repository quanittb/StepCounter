package com.mobiai.app.ui.dialog

import android.content.Context
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.example.quanpham.R
import com.example.quanpham.databinding.DialogGenderBinding
import com.example.quanpham.dialog.StepGoalBottomDialog
import com.example.quanpham.dialog.StepGoalBottomDialog.OnClickBottomSheetListener
import com.example.quanpham.lib.SharedPreferenceUtils
import com.google.android.material.bottomsheet.BottomSheetDialog


class GenderDialog(context: Context, private val listener : OnClickBottomSheetListener) : BottomSheetDialog(context) {
    val binding = DialogGenderBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var chooseSex = SharedPreferenceUtils.selectSex
        val typeface = ResourcesCompat.getFont(context, R.font.mukta_semi_bold)
        val typeface_2 = ResourcesCompat.getFont(context, R.font.mukta_regular)
        if(chooseSex==0)
        {
            binding.txtFemale.setTextColor(context.resources.getColor(R.color.selected_text_color_sex))
            binding.txtFemale.textSize = 22f
            binding.txtFemale.typeface = typeface
            binding.txtMale.typeface = typeface_2
            binding.txtFemale.setBackgroundResource(R.drawable.bg_selected_dialog)
            binding.txtMale.setTextColor(context.resources.getColor(R.color.un_selected_text_color_sex))
            binding.txtMale.textSize = 20f
            binding.txtMale.setBackgroundResource(R.drawable.bg_unselected_dialog)

        }
        else{
            binding.txtMale.setTextColor(context.resources.getColor(R.color.selected_text_color_sex))
            binding.txtMale.textSize = 22f
            binding.txtFemale.setTextColor(context.resources.getColor(R.color.un_selected_text_color_sex))
            binding.txtMale.setBackgroundResource(R.drawable.bg_selected_dialog)
            binding.txtFemale.setBackgroundResource(R.drawable.bg_unselected_dialog)
            binding.txtFemale.textSize = 20f
            binding.txtFemale.typeface = typeface_2
            binding.txtMale.typeface = typeface
        }
        binding.txtFemale.setOnClickListener {
            chooseSex = 0
            binding.txtFemale.setTextColor(context.resources.getColor(R.color.selected_text_color_sex))
            binding.txtFemale.setBackgroundResource(R.drawable.bg_selected_dialog)
            binding.txtFemale.textSize = 22f
            binding.txtMale.setTextColor(context.resources.getColor(R.color.un_selected_text_color_sex))
            binding.txtMale.setBackgroundResource(R.drawable.bg_unselected_dialog)
            binding.txtMale.textSize = 20f
            binding.txtFemale.typeface = typeface
            binding.txtMale.typeface = typeface_2
        }

        binding.txtMale.setOnClickListener {
            chooseSex = 1
            binding.txtMale.setTextColor(context.resources.getColor(R.color.selected_text_color_sex))
            binding.txtMale.textSize = 22f
            binding.txtMale.setBackgroundResource(R.drawable.bg_selected_dialog)
            binding.txtFemale.setBackgroundResource(R.drawable.bg_unselected_dialog)
            binding.txtFemale.setTextColor(context.resources.getColor(R.color.un_selected_text_color_sex))
            binding.txtFemale.textSize = 20f
            binding.txtFemale.typeface = typeface_2
            binding.txtMale.typeface = typeface
        }


        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            SharedPreferenceUtils.selectSex = chooseSex
            listener.onClickSaveFrom()
            checkHideBottomSheet()
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
}