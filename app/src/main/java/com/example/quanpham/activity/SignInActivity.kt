package com.example.quanpham.activity

import android.content.Context
import android.content.Intent
import com.example.quanpham.R
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.databinding.ActivitySigninBinding
import com.example.quanpham.model.Users
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.showToast

class SignInActivity : BaseActivity<ActivitySigninBinding>() {
    override fun getViewBinding() = ActivitySigninBinding.inflate(layoutInflater)
    private var email = ""
    private var name = ""
    private var pass = ""
    private var repass = ""

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
        repass = binding.repass.text.toString()
        name = binding.userName.text.toString()

        if (email.isEmpty() || pass.isEmpty() || repass.isEmpty() || name.isEmpty()) {
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
                showToast("Tesst")
                singInApp()
            } else {
                showToast(getString(R.string.enter_all_value))
            }
        }
        binding.txtSignup.setOnClickListener { v->
            SignUpActivity.start(this,true)
        }
    }

    private fun singInApp() {

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { auth ->
                val user = Users(auth.user?.uid, name, email, pass)
                firestore.collection(Constant.KEY_USER).document(user.id!!).set(user)
                    .addOnSuccessListener {
                        MainActivity.startMain(this, true)
                        finish()
                    }.addOnFailureListener {
                        showToast(it.message.toString())
                    }
            }.addOnFailureListener {
                showToast(it.message.toString())
            }


    }
}

