package com.mobiai.app.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.quanpham.R
import com.example.quanpham.databinding.DialogHeightBinding
import com.example.quanpham.lib.SharedPreferenceUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.DecimalFormat


class HeightDialog(context: Context, private val listener : OnClickBottomSheetListener) : BottomSheetDialog(context) {
    val binding = DialogHeightBinding.inflate(layoutInflater)
    private var isChooseUnit = SharedPreferenceUtils.unit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setValue()
        if(SharedPreferenceUtils.unit)
            showClickUnit(binding.cm)
        else
            showClickUnit(binding.ft)
        if(SharedPreferenceUtils.height!= 0f){
            val parts = SharedPreferenceUtils.height.toString().split(".")
            val numInt = parts[0]
            val numDecimal = parts[1]
            binding.pickHeightInt.value = numInt.toInt()
            binding.pickHeightDecimal.value = numDecimal.toInt()
        }
        binding.cm.setOnClickListener{
            showClickUnit(binding.cm)
        }
        binding.ft.setOnClickListener{
            showClickUnit(binding.ft)
        }
        binding.pickHeightInt.setOnValueChangedListener { picker, oldVal, newVal ->
            if (!isChooseUnit) {
                SharedPreferenceUtils.height1_temporary = "${binding.pickHeightInt.value}.${binding.pickHeightDecimal.value}".toFloat()
                if (newVal == 8) {
                    binding.pickHeightDecimal.maxValue = 2
                    binding.pickHeightDecimal.minValue = 0
                }
                else if (newVal == 0) {
                    binding.pickHeightDecimal.minValue = 8
                    binding.pickHeightDecimal.maxValue = 9
                }
                else {
                    binding.pickHeightDecimal.maxValue = 9
                    binding.pickHeightDecimal.minValue = 0
                }

            }
            if (isChooseUnit) {
                SharedPreferenceUtils.height0_temporary = "${binding.pickHeightInt.value}.${binding.pickHeightDecimal.value}".toFloat()
                if (newVal == 250) {
                    binding.pickHeightDecimal.maxValue = 0
                } else {
                    binding.pickHeightDecimal.maxValue = 9
                    binding.pickHeightDecimal.minValue = 0
                }
            }
        }
        binding.pickHeightDecimal.setOnValueChangedListener { picker, oldVal, newVal ->
            if (!isChooseUnit) {
                SharedPreferenceUtils.height1_temporary = "${binding.pickHeightInt.value}.${binding.pickHeightDecimal.value}".toFloat()

            }
            if (isChooseUnit) {
                SharedPreferenceUtils.height0_temporary = "${binding.pickHeightInt.value}.${binding.pickHeightDecimal.value}".toFloat()
            }
        }


        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            SharedPreferenceUtils.height = "${binding.pickHeightInt.value}.${binding.pickHeightDecimal.value}".toFloat()
            if(!isChooseUnit){
                SharedPreferenceUtils.height = SharedPreferenceUtils.height * 30.48.toFloat()

            }
            SharedPreferenceUtils.unit = isChooseUnit
            listener.onClickSaveFrom()
            dismiss()

//            checkHideBottomSheet()
        }
    }

    fun setValue(){
        when(SharedPreferenceUtils.unit){
            true ->{
                showClickUnit(binding.cm)
                if(SharedPreferenceUtils.height0_temporary == 0f){
                    binding.pickHeightInt.value = 170
                    binding.pickHeightDecimal.value = 0
                }
                else{
                    val parts = SharedPreferenceUtils.height0_temporary.toString().split(".")
                    val numInt = parts[0]
                    val numDecimal = parts[1]
                    binding.pickHeightInt.value = numInt.toInt()
                    binding.pickHeightDecimal.value = numDecimal.toInt()
                }
            }

            false -> {
                showClickUnit(binding.ft)
                if(SharedPreferenceUtils.height1_temporary == 0f){
                    binding.pickHeightInt.value = 4
                    binding.pickHeightDecimal.value = 0
                }
                else{
                    val parts = (SharedPreferenceUtils.height1_temporary).toString().split(".")
                    val numInt = parts[0]
                    val numDecimal = parts[1]
                    binding.pickHeightInt.value = numInt.toInt()
                    binding.pickHeightDecimal.value = numDecimal.toInt()
                }
            }
        }
    }
    private fun showClickUnit(view: View) {
        var df = DecimalFormat("#.#")
        if (view == binding.cm) {
            isChooseUnit = true
            binding.cm.setBackgroundResource(R.drawable.bg_unit)
            binding.ft.setBackgroundResource(R.drawable.bg_unselected_unit)
            binding.cm.setTextColor(context.getColor(R.color.selected_unit))
            binding.ft.setTextColor(context.getColor(R.color.unselected_unit))
            binding.pickHeightInt.maxValue = 250
            binding.pickHeightInt.minValue = 25
            binding.pickHeightDecimal.maxValue = 9
            binding.pickHeightDecimal.minValue = 0
            if(SharedPreferenceUtils.height0_temporary == 0f){
                binding.pickHeightInt.value = 170
                binding.pickHeightDecimal.value = 0
            }
            else{
                val parts = SharedPreferenceUtils.height0_temporary.toString().split(".")
                val numInt = parts[0]
                val numDecimal = parts[1]
                binding.pickHeightInt.value = numInt.toInt()
                binding.pickHeightDecimal.value = numDecimal.toInt()
                if(numInt.toInt() == 250) binding.pickHeightDecimal.maxValue = 0
            }

        } else {
            isChooseUnit = false
            binding.ft.setBackgroundResource(R.drawable.bg_unit)
            binding.cm.setBackgroundResource(R.drawable.bg_unselected_unit)
            binding.ft.setTextColor(context.getColor(R.color.selected_unit))
            binding.cm.setTextColor(context.getColor(R.color.unselected_unit))
            binding.pickHeightInt.maxValue = 8
            binding.pickHeightInt.minValue = 0
            binding.pickHeightDecimal.maxValue = 9
            binding.pickHeightDecimal.minValue = 0
            if(SharedPreferenceUtils.height1_temporary == 0f){
                binding.pickHeightInt.value = 4
                binding.pickHeightDecimal.value = 0
            }
            else{
                val parts = SharedPreferenceUtils.height1_temporary.toString().split(".")
                val numInt = parts[0]
                val numDecimal = parts[1]
                binding.pickHeightInt.value = numInt.toInt()
                binding.pickHeightDecimal.value = numDecimal.toInt()
                if(numInt.toInt() == 0){
                    binding.pickHeightDecimal.maxValue = 9
                    binding.pickHeightDecimal.minValue = 8
                }
                if(numInt.toInt() == 8){
                    binding.pickHeightDecimal.maxValue = 2
                    binding.pickHeightDecimal.minValue = 0
                }
            }
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