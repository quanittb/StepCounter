package com.mobiai.app.ui.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.quanpham.R
import com.example.quanpham.databinding.DialogWeightBinding
import com.example.quanpham.lib.SharedPreferenceUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.DecimalFormat
import java.util.Calendar


class WeightDialog(context: Context, private val listener : OnClickBottomSheetListener) : BottomSheetDialog(context) {
    val binding = DialogWeightBinding.inflate(layoutInflater)
    private var isChooseUnit = SharedPreferenceUtils.unit
            val df = DecimalFormat("#.#")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        setValue()
        if(SharedPreferenceUtils.unit)
//            showClickUnit(binding.kg)
        else
//            showClickUnit(binding.lbs)

        binding.kg.setOnClickListener{
//            showClickUnit(binding.kg)
        }
        binding.lbs.setOnClickListener{
//            showClickUnit(binding.lbs)
        }
//        binding.pickWeightInt.setOnValueChangedListener { picker, oldVal, newVal ->
//            if(!isChooseUnit){
//                SharedPreferenceUtils.weight1_temporary = "${binding.pickWeightInt.value}.${binding.pickWeightDecimal.value}".toFloat()
//
//                if(newVal == 661){
//                    binding.pickWeightDecimal.maxValue = 0
//                }
//                else{
//                    binding.pickWeightDecimal.maxValue = 9
//                    binding.pickWeightDecimal.minValue = 0
//                }
//            }
//            if(isChooseUnit){
//                SharedPreferenceUtils.weight0_temporary = "${binding.pickWeightInt.value}.${binding.pickWeightDecimal.value}".toFloat()
//                if(newVal == 300){
//                    binding.pickWeightDecimal.maxValue = 0
//                }
//                else{
//                    binding.pickWeightDecimal.maxValue = 9
//                    binding.pickWeightDecimal.minValue = 0
//                }
//            }
//        }
//        binding.pickWeightDecimal.setOnValueChangedListener { picker, oldVal, newVal ->
//            if (!isChooseUnit) {
//                SharedPreferenceUtils.weight1_temporary = "${binding.pickWeightInt.value}.${binding.pickWeightDecimal.value}".toFloat()
//
//            }
//            if (isChooseUnit) {
//                SharedPreferenceUtils.weight0_temporary = "${binding.pickWeightInt.value}.${binding.pickWeightDecimal.value}".toFloat()
//            }
//        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
//            startNotiLockChangeGoalAndWeight()
            Log.d("abcd","weight : ${SharedPreferenceUtils.weight}")
            SharedPreferenceUtils.unit = isChooseUnit
            if(!SharedPreferenceUtils.unit) {
                val a =
                    "${binding.pickWeightInt.value}.${binding.pickWeightDecimal.value}".toFloat()
                SharedPreferenceUtils.weight = df.format(a / 2.20462).replace(",",".").toFloat()
            }
            else
                SharedPreferenceUtils.weight = "${binding.pickWeightInt.value}.${binding.pickWeightDecimal.value}".toFloat()
//            RxBus.publish(WeightUpdate(SharedPreferenceUtils.weight.toString()))
            listener.onClickSaveFrom()
            checkHideBottomSheet()
        }
    }
//    private fun startNotiLockChangeGoalAndWeight(){
//        val calendar = Calendar.getInstance()
//        val hour = calendar.get(Calendar.HOUR_OF_DAY) + 3
//        val minute = calendar.get(Calendar.MINUTE)
//        NotifyManager.showNotificationChangeWeightLock(context, hour, minute)
//    }
//    fun setValue(){
//        when(SharedPreferenceUtils.unit){
//            true ->{
//                if(SharedPreferenceUtils.weight0_temporary == 0f){
//                    binding.pickWeightInt.value = 70
//                    binding.pickWeightDecimal.value = 0
//                }
//                else{
//                    val parts = SharedPreferenceUtils.weight0_temporary.toString().split(".")
//                    val numInt = parts[0]
//                    val numDecimal = parts[1]
//                    binding.pickWeightInt.value = numInt.toInt()
//                    binding.pickWeightDecimal.value = numDecimal.toInt()
//                }
//            }
//            false ->{
//                if(SharedPreferenceUtils.weight1_temporary == 0f){
//                    binding.pickWeightInt.value = 350
//                    binding.pickWeightDecimal.value = 0
//                }
//                else{
//                    val parts = (SharedPreferenceUtils.weight1_temporary).toString().split(".")
//                    val numInt = parts[0]
//                    val numDecimal = parts[1]
//                    binding.pickWeightInt.value = numInt.toInt()
//                    binding.pickWeightDecimal.value = numDecimal.toInt()
//                }
//            }
//        }
//    }
//    private fun showClickUnit(view: View) {
//        if (view == binding.kg) {
//            isChooseUnit = true
//            binding.kg.setBackgroundResource(R.drawable.bg_unit)
//            binding.lbs.setBackgroundResource(R.drawable.bg_unselected_unit)
//            binding.kg.setTextColor(context.getColor(R.color.selected_unit))
//            binding.lbs.setTextColor(context.getColor(R.color.unselected_unit))
//            binding.pickWeightInt.maxValue = 300
//            binding.pickWeightInt.minValue = 15
//            if(SharedPreferenceUtils.weight0_temporary == 0f){
//                binding.pickWeightInt.value = 70
//                binding.pickWeightDecimal.value = 0
//            }
//            else{
//                val parts = SharedPreferenceUtils.weight0_temporary.toString().split(".")
//                val numInt = parts[0]
//                val numDecimal = parts[1]
//                binding.pickWeightInt.value = numInt.toInt()
//                binding.pickWeightDecimal.value = numDecimal.toInt()
//                if(numInt.toInt() == 300){
//                    binding.pickWeightDecimal.maxValue = 0
//                }
//            }
//
//        } else if (view == binding.lbs) {
//            isChooseUnit = false
//            binding.lbs.setBackgroundResource(R.drawable.bg_unit)
//            binding.kg.setBackgroundResource(R.drawable.bg_unselected_unit)
//            binding.lbs.setTextColor(context.getColor(R.color.selected_unit))
//            binding.kg.setTextColor(context.getColor(R.color.unselected_unit))
//            binding.pickWeightInt.maxValue = 661
//            binding.pickWeightInt.minValue = 33
//            if(SharedPreferenceUtils.weight1_temporary == 0f){
//                binding.pickWeightInt.value = 350
//                binding.pickWeightDecimal.value = 0
//            }
//            else{
//                val parts = SharedPreferenceUtils.weight1_temporary.toString().split(".")
//                val numInt = parts[0]
//                val numDecimal = parts[1]
//                binding.pickWeightInt.value = numInt.toInt()
//                binding.pickWeightDecimal.value = numDecimal.toInt()
//                if(numInt.toInt() == 661){
//                    binding.pickWeightDecimal.maxValue = 0
//                }
//            }
//        }
//    }

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