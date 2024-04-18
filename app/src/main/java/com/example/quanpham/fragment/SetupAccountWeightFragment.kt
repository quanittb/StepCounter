package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quanpham.R
import com.example.quanpham.activity.MainActivity
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentSetupAccountWeightBinding
import com.example.quanpham.db.model.Weights
import com.example.quanpham.lib.SharedPreferenceUtils

class SetupAccountWeightFragment : BaseFragment<FragmentSetupAccountWeightBinding>() {
    private var isChooseUnit = SharedPreferenceUtils.unit
    private val TAG = SetupAccountWeightFragment::javaClass.name
    companion object{
        fun instance() : SetupAccountWeightFragment {
            return newInstance(SetupAccountWeightFragment::class.java)
        }
    }
    override fun initView() {
//        initAds()
        setValue()
        binding.btnBack.setOnClickListener{
            handlerBackPressed()
        }
        if(SharedPreferenceUtils.unit)
            showClickUnit(binding.kg)
        else
            showClickUnit(binding.lbs)
        binding.kg.setOnClickListener{
            showClickUnit(binding.kg)
        }
        binding.lbs.setOnClickListener{
            showClickUnit(binding.lbs)
        }
        binding.pickWeightInt.setOnValueChangedListener { picker, oldVal, newVal ->
            if(!isChooseUnit){
                SharedPreferenceUtils.weight1_temporary = "${binding.pickWeightInt.value}.${binding.pickWeightDecimal.value}".toFloat()

                if(newVal == 661){
                    binding.pickWeightDecimal.maxValue = 0
                }
                else{
                    binding.pickWeightDecimal.maxValue = 9
                    binding.pickWeightDecimal.minValue = 0
                }
            }
            if(isChooseUnit){
                SharedPreferenceUtils.weight0_temporary = "${binding.pickWeightInt.value}.${binding.pickWeightDecimal.value}".toFloat()
                if(newVal == 300){
                    binding.pickWeightDecimal.maxValue = 0
                }
                else{
                    binding.pickWeightDecimal.maxValue = 9
                    binding.pickWeightDecimal.minValue = 0
                }
            }
        }
        binding.pickWeightDecimal.setOnValueChangedListener { picker, oldVal, newVal ->
            if (!isChooseUnit) {
                SharedPreferenceUtils.weight1_temporary = "${binding.pickWeightInt.value}.${binding.pickWeightDecimal.value}".toFloat()

            }
            if (isChooseUnit) {
                SharedPreferenceUtils.weight0_temporary = "${binding.pickWeightInt.value}.${binding.pickWeightDecimal.value}".toFloat()
            }
        }
        binding.btnSkip.setOnClickListener {
            MainActivity.startMain(requireContext(),true)
        }
        binding.btnNextStep.setOnClickListener{

            if(isChooseUnit)
                SharedPreferenceUtils.weight = "${binding.pickWeightInt.value}.${binding.pickWeightDecimal.value}".toFloat()
            else {
                SharedPreferenceUtils.weight = "${binding.pickWeightInt.value}.${binding.pickWeightDecimal.value}".toFloat()
                SharedPreferenceUtils.weight = (SharedPreferenceUtils.weight / 2.20462).toFloat()
            }
            SharedPreferenceUtils.unit = isChooseUnit
            val weight = Weights()
            weight.weight = SharedPreferenceUtils.weight
            weight.updateTime = DateUtils.getDateFromTimeMillis(System.currentTimeMillis())
            attachFragment()
        }
    }

    fun setValue(){
        when(SharedPreferenceUtils.unit){
            true ->{
                if(SharedPreferenceUtils.weight0_temporary == 0f){
                    binding.pickWeightInt.value = 70
                    binding.pickWeightDecimal.value = 0
                }
                else{
                    val parts = SharedPreferenceUtils.weight0_temporary.toString().split(".")
                    val numInt = parts[0]
                    val numDecimal = parts[1]
                    binding.pickWeightInt.value = numInt.toInt()
                    binding.pickWeightDecimal.value = numDecimal.toInt()
                }
            }
            false ->{
                if(SharedPreferenceUtils.weight1_temporary == 0f){
                    binding.pickWeightInt.value = 350
                    binding.pickWeightDecimal.value = 0
                }
                else{
                    val parts = (SharedPreferenceUtils.weight1_temporary).toString().split(".")
                    val numInt = parts[0]
                    val numDecimal = parts[1]
                    binding.pickWeightInt.value = numInt.toInt()
                    binding.pickWeightDecimal.value = numDecimal.toInt()
                }
            }
        }
    }
    private fun showClickUnit(view: View) {
        if (view == binding.kg) {
            isChooseUnit = true
            binding.kg.setBackgroundResource(R.drawable.bg_unit)
            binding.lbs.setBackgroundResource(R.drawable.bg_unselected_unit)
            binding.kg.setTextColor(resources.getColor(R.color.selected_unit))
            binding.lbs.setTextColor(resources.getColor(R.color.unselected_unit))
            binding.pickWeightInt.maxValue = 300
            binding.pickWeightInt.minValue = 15
            if(SharedPreferenceUtils.weight0_temporary == 0f){
                binding.pickWeightInt.value = 70
                binding.pickWeightDecimal.value = 0
            }
            else{
                val parts = SharedPreferenceUtils.weight0_temporary.toString().split(".")
                val numInt = parts[0]
                val numDecimal = parts[1]
                binding.pickWeightInt.value = numInt.toInt()
                binding.pickWeightDecimal.value = numDecimal.toInt()
                if(numInt.toInt() == 300){
                    binding.pickWeightDecimal.maxValue = 0
                }
            }

        } else {
            isChooseUnit = false
            binding.lbs.setBackgroundResource(R.drawable.bg_unit)
            binding.kg.setBackgroundResource(R.drawable.bg_unselected_unit)
            binding.lbs.setTextColor(resources.getColor(R.color.selected_unit))
            binding.kg.setTextColor(resources.getColor(R.color.unselected_unit))
            binding.pickWeightInt.maxValue = 661
            binding.pickWeightInt.minValue = 33
            if(SharedPreferenceUtils.weight1_temporary == 0f){
                binding.pickWeightInt.value = 350
                binding.pickWeightDecimal.value = 0
            }
            else{
                val parts = SharedPreferenceUtils.weight1_temporary.toString().split(".")
                val numInt = parts[0]
                val numDecimal = parts[1].substring(0,1)
                binding.pickWeightInt.value = numInt.toInt()
                binding.pickWeightDecimal.value = numDecimal.toInt()
                if(numInt.toInt() == 661){
                    binding.pickWeightDecimal.maxValue = 0
                }
            }
        }
    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSetupAccountWeightBinding {
        return FragmentSetupAccountWeightBinding.inflate(inflater, container,false)
    }
    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }
    private fun attachFragment() {
        replaceFragment(SetupAccountAgeFragment.instance())
    }

}