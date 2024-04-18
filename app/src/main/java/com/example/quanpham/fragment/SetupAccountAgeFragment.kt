package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quanpham.activity.MainActivity
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentSetupAccountAgeBinding
import com.example.quanpham.lib.SharedPreferenceUtils

class SetupAccountAgeFragment : BaseFragment<FragmentSetupAccountAgeBinding>() {
    companion object{
        fun instance() : SetupAccountAgeFragment{
            return newInstance(SetupAccountAgeFragment::class.java)
        }
    }
    override fun initView() {
//        initAds()
        binding.btnBack.setOnClickListener{
            handlerBackPressed()
        }
        binding.btnSkip.setOnClickListener {
            MainActivity.startMain(requireContext(),true)
        }
        binding.btnNextStep.setOnClickListener{
            SharedPreferenceUtils.age = binding.pickAge.value
            MainActivity.startMain(requireContext(),true)

        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSetupAccountAgeBinding {
        return FragmentSetupAccountAgeBinding.inflate(inflater, container,false)
    }
    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }
}