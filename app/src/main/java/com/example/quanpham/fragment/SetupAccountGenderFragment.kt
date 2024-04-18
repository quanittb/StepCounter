package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quanpham.R
import com.example.quanpham.activity.MainActivity
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentSetupAccountGenderBinding
import com.example.quanpham.lib.SharedPreferenceUtils

class SetupAccountGenderFragment : BaseFragment<FragmentSetupAccountGenderBinding>() {
    private var isChooseSex = -1

//    private var countReject: Int  = 1

    companion object{
        fun instance() : SetupAccountGenderFragment {
            return newInstance(SetupAccountGenderFragment::class.java)
        }
    }
    override fun initView() {
        when(SharedPreferenceUtils.selectSex){
            0 ->{
                showClickImgSex(binding.imgWomen)
            }
            1 ->{
                showClickImgSex(binding.imgMan)
            }
        }
        binding.btnBack.setOnClickListener{
            handlerBackPressed()
        }
        binding.imgWomen.setOnClickListener {
            showClickImgSex(binding.imgWomen)
        }
        binding.imgMan.setOnClickListener {
            showClickImgSex(binding.imgMan)
        }
        binding.btnNextStep.setOnClickListener {
            when (isChooseSex) {
                0 -> {
                    SharedPreferenceUtils.selectSex = 0
                    attachFragment()
                }

                1 -> {
                    SharedPreferenceUtils.selectSex = 1
                    attachFragment()
                }

                else -> {
                    SharedPreferenceUtils.selectSex = -1
                }
            }
        }
        binding.btnSkip.setOnClickListener{
            SharedPreferenceUtils.selectSex = -1
            MainActivity.startMain(requireContext(),true)
        }
    }


    private fun attachFragment() {
        replaceFragment(SetupAccountTallFragment.instance())
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSetupAccountGenderBinding {
        return FragmentSetupAccountGenderBinding.inflate(inflater, container,false)
    }

    private fun showClickImgSex(view: View) {
        if (view == binding.imgWomen) {
            isChooseSex = 0
            binding.imgWomen.setImageResource(R.drawable.img_women_true)
            binding.imgMan.setImageResource(R.drawable.img_man_false)
            binding.tvFemale.setTextColor(resources.getColor(R.color.selected_text_color_sex))
            binding.tvMale.setTextColor(resources.getColor(R.color.un_selected_text_color_sex))
        } else {
            isChooseSex = 1
            binding.imgWomen.setImageResource(R.drawable.img_women_false)
            binding.imgMan.setImageResource(R.drawable.img_man_true)
            binding.tvFemale.setTextColor(resources.getColor(R.color.un_selected_text_color_sex))
            binding.tvMale.setTextColor(resources.getColor(R.color.selected_text_color_sex))

        }
    }
    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }

}