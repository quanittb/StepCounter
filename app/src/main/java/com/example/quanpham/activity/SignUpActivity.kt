package com.example.quanpham.activity

import android.content.Context
import android.content.Intent
import android.util.Patterns.EMAIL_ADDRESS
import com.example.quanpham.R
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.databinding.ActivitySignUpBinding
import com.example.quanpham.model.Users
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.showToast

class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {
    override fun getViewBinding() = ActivitySignUpBinding.inflate(layoutInflater)
    private var email = ""
    private var name = ""
    private var pass = ""
    private var repass = ""

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
        pass = binding.passrod.text.toString()
        repass = binding.repass.text.toString()

        if (email.isEmpty() || pass.isEmpty() || repass.isEmpty() ||  name.isEmpty()) {
            return false
        }

        if (!EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }
        return true
    }

    override fun createView() {
        binding.btnSignUp.setOnClickListener {
            if (checkValue()) {
                showToast("Test")
                singUpApp()
            } else {
                showToast(getString(R.string.enter_all_value))
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
                val user = Users(auth.user?.uid, email,name, pass, 20,true,170f)
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

