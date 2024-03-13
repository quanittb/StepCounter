package com.example.quanpham.activity

import android.content.Context
import android.content.Intent
import com.example.quanpham.R
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.databinding.ActivitySignInBinding
import com.example.quanpham.fragment.ForgotPassFragment
import com.example.quanpham.model.Users
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.showToast

class SignInActivity : BaseActivity<ActivitySignInBinding>() {
    override fun getViewBinding() = ActivitySignInBinding.inflate(layoutInflater)
    private var email = ""
    private var pass = ""

    companion object {
        fun start(context: Context, clearTask: Boolean) {
            val intent = Intent(context, SignInActivity::class.java).apply {
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
        pass = binding.passrod.text.toString()

        if (email.isEmpty() || pass.isEmpty()) {
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }
        return true
    }

    override fun createView() {
        binding.btnSignin.setOnClickListener {
            if (checkValue()) {
                signApp()
            } else {
                showToast(getString(R.string.enter_all_value))
            }
        }

        binding.txtSignup.setOnClickListener {
            supportFragmentManager.popBackStack()
            SignUpActivity.start(this,false)
        }
        binding.txtfogotpass.setOnClickListener {
            supportFragmentManager.popBackStack()
            addFragment(ForgotPassFragment(),android.R.id.content,true)
        }
    }
    private fun signApp() {
        auth.signInWithEmailAndPassword(email,pass)
            .addOnSuccessListener {
                firestore.collection(Constant.KEY_USER)
                    .document(it.user!!.uid)
                    .get()
                    .addOnSuccessListener {

                        usLoggin?.postValue(it.toObject(Users::class.java))
                        MainActivity.startMain(this,true)

                    }
                    .addOnFailureListener {
                        showToast(it.message.toString())
                    }
            }
            .addOnFailureListener {
                showToast(it.message.toString())
            }
    }
}