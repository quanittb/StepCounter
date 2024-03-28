package com.example.quanpham.dialog

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import com.example.quanpham.databinding.DialogStepGoalBinding
import com.example.quanpham.lib.SharedPreferenceUtils
import com.google.android.material.bottomsheet.BottomSheetDialog


class StepGoalBottomDialog(context: Context, private val listener : OnClickBottomSheetListener) : BottomSheetDialog(context) {
    val binding = DialogStepGoalBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val values = arrayListOf<Int>()
        for (i in 500..40000 step 500)
        {
            values.add(i)
        }
        val displayValues = values.map { it.toString() }.toTypedArray()

        // Thiết lập font cho giá trị trong NumberPicker
        // val customFont = Typeface.createFromAsset(context.getFontCompat(), "font/mukta_semi_bold.ttf")
        for (i in 0 until binding.pickGoal.childCount) {
            val child = binding.pickGoal.getChildAt(i)
            if (child is EditText) {
//                child.typeface = context.getFontCompat(R.font.mukta_semi_bold)
            }

        }
        binding.pickGoal.minValue = 0
        binding.pickGoal.maxValue = values.size - 1
        binding.pickGoal.displayedValues = displayValues
        for (i in 0 until  values.size)
        {
            if (values[i] == SharedPreferenceUtils.targetStep.toInt() ){
                binding.pickGoal.value = i
            }
        }


        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            SharedPreferenceUtils.targetStep = values[binding.pickGoal.value].toLong()
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
    interface OnClickBottomSheetListener{
        fun onClickSaveFrom()
    }

}