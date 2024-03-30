package com.mobiai.app.ui.dialog

import android.content.Context
import android.os.Bundle
import com.example.quanpham.databinding.DialogAgeBinding
import com.example.quanpham.dialog.StepGoalBottomDialog
import com.example.quanpham.lib.SharedPreferenceUtils
import com.google.android.material.bottomsheet.BottomSheetDialog


class AgeDialog(context: Context, private val listener : StepGoalBottomDialog.OnClickBottomSheetListener) : BottomSheetDialog(context) {
    val binding = DialogAgeBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Thiết lập font cho giá trị trong NumberPicker
        // val customFont = Typeface.createFromAsset(context.getFontCompat(), "font/mukta_semi_bold.ttf")

        binding.pickAge.value = SharedPreferenceUtils.age

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            SharedPreferenceUtils.age = binding.pickAge.value
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