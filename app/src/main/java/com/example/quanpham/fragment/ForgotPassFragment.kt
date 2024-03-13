package com.example.quanpham.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quanpham.R
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmnetForgotPassBinding
import com.example.quanpham.utility.showToast

class ForgotPassFragment : BaseFragment<FragmnetForgotPassBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmnetForgotPassBinding.inflate(inflater)

    override fun initView() {
        binding.btnSignin.setOnClickListener {
            auth.sendPasswordResetEmail(binding.email.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast("Send successfully")
                        requireActivity().supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                        )
                            .remove(this).commit()
                    }
                }
                .addOnFailureListener {
                    showToast(it.message.toString())
                }
        }
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        requireActivity().supportFragmentManager.beginTransaction().
        setCustomAnimations(
            R.anim.slide_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.slide_out
        ).remove(this).commit()
    }
}