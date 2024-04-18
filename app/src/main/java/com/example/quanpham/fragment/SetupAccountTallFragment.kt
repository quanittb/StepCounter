package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quanpham.R
import com.example.quanpham.activity.MainActivity
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentSetupAccountTallBinding
import com.example.quanpham.lib.SharedPreferenceUtils

class SetupAccountTallFragment : BaseFragment<FragmentSetupAccountTallBinding>() {
    private var isChooseUnit = SharedPreferenceUtils.unit
    companion object {
        fun instance(): SetupAccountTallFragment {
            return newInstance(SetupAccountTallFragment::class.java)
        }
    }

    override fun initView() {
//        initAds()
        binding.btnBack.setOnClickListener {
            handlerBackPressed()
        }
        setValue()
        if(SharedPreferenceUtils.unit)
            showClickUnit(binding.cm)
        else
            showClickUnit(binding.ft)
//        binding.txtTall.text = HtmlCompat.fromHtml(getString(R.string.how_tall_are_you), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.cm.setOnClickListener {
            showClickUnit(binding.cm)
        }
        binding.ft.setOnClickListener {
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
        binding.btnSkip.setOnClickListener {
            MainActivity.startMain(requireContext(),true)
        }
        binding.btnNextStep.setOnClickListener {
            if(isChooseUnit) {
                SharedPreferenceUtils.height =
                    "${binding.pickHeightInt.value}.${binding.pickHeightDecimal.value}".toFloat()
            }
            if(!isChooseUnit)
            {
                SharedPreferenceUtils.height = "${binding.pickHeightInt.value}.${binding.pickHeightDecimal.value}".toFloat()
                SharedPreferenceUtils.height = (SharedPreferenceUtils.height * 30.48).toFloat()
            }
            SharedPreferenceUtils.unit = isChooseUnit

            attachFragment()
        }
    }
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSetupAccountTallBinding {
        return FragmentSetupAccountTallBinding.inflate(inflater, container, false)
    }

    fun setValue(){
        when(SharedPreferenceUtils.unit){
            true ->{
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
        if (view == binding.cm) {
            isChooseUnit = true
            binding.cm.setBackgroundResource(R.drawable.bg_unit)
            binding.ft.setBackgroundResource(R.drawable.bg_unselected_unit)
            binding.cm.setTextColor(resources.getColor(R.color.selected_unit))
            binding.ft.setTextColor(resources.getColor(R.color.unselected_unit))
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
            binding.ft.setTextColor(resources.getColor(R.color.selected_unit))
            binding.cm.setTextColor(resources.getColor(R.color.unselected_unit))
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

    private fun attachFragment() {
        replaceFragment(SetupAccountWeightFragment.instance())
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }
}