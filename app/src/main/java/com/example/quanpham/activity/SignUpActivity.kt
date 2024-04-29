package com.example.quanpham.activity

import android.content.Context
import android.content.Intent
import android.text.method.PasswordTransformationMethod
import android.util.Patterns.EMAIL_ADDRESS
import com.example.quanpham.R
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.databinding.ActivitySignUpBinding
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.model.Users
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.showToast

class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {
    override fun getViewBinding() = ActivitySignUpBinding.inflate(layoutInflater)
    private var email = ""
    private var name = ""
    private var pass = ""
    private var repass = ""
    private var isPasswordVisible = false
    private var isRePassVisible = false

    companion object {
        fun start(context: Context, clearTask: Boolean) {
            val intent = Intent(context, SignUpActivity::class.java).apply {
                if (clearTask) {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(intent)
        }
    }

    private fun checkValue(): Boolean {
        email = binding.email.text.toString()
        name = binding.tvName.text.toString()
        pass = binding.password.text.toString()
        repass = binding.repass.text.toString()

        if (email.isEmpty() || pass.isEmpty() || repass.isEmpty() ||  name.isEmpty()) {
            return false
        }

        if (!EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }
        if(pass != repass)
            return false
        return true
    }

    override fun createView() {
        binding.btnSignUp.setOnClickListener {
            if (checkValue()) {
                singUpApp()
            } else {
                showToast(getString(R.string.enter_all_value))
            }
        }
        binding.visiblePass.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.password.transformationMethod = null
                binding.password.setSelection(binding.password.length())
            } else {
                binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.password.setSelection(binding.password.length())
            }
        }
        binding.visibleRepass.setOnClickListener{
            isRePassVisible = !isRePassVisible
            if (isRePassVisible) {
                binding.repass.transformationMethod = null
                binding.repass.setSelection(binding.repass.length())
            } else {
                binding.repass.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.repass.setSelection(binding.repass.length())
            }
        }

        binding.txtSignIn.setOnClickListener { v->
            supportFragmentManager.popBackStack()
            SignInActivity.start(this,false)
        }
    }

    private fun singUpApp() {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { auth ->
                val user = Users(auth.user?.uid, email,name, pass, null,true,null)
                firestore.collection(Constant.KEY_USER).document(user.id!!).set(user)
                    .addOnSuccessListener {
                        SharedPreferenceUtils.isSetupAccount = true
                        SetupAccountActivity.startMain(this, true)
                        showToast(getString(R.string.success))
                        finish()
                    }.addOnFailureListener {
                        showToast(it.message.toString())
                    }
            }.addOnFailureListener {
                showToast(it.message.toString())
            }


    }
}

