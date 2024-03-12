package com.example.quanpham.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.ActivityForgotPassBinding
import com.example.quanpham.utility.showToast

class ForgotPassFragment : BaseFragment<ActivityForgotPassBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = ActivityForgotPassBinding.inflate(inflater)

    override fun initView() {
        binding.btnSignin.setOnClickListener {
            auth.sendPasswordResetEmail(binding.email.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast("Send successfull")
                        closeFragment(this)
                    }
                }
                .addOnFailureListener {
                    showToast(it.message.toString())
                }
        }
        binding.btnBack.setOnClickListener {  closeFragment(this) }
    }
}