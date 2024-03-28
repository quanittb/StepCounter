package com.example.quanpham.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.EditorInfo
import com.example.quanpham.R
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.databinding.ActivitySignInBinding
import com.example.quanpham.fragment.ForgotPassFragment
import com.example.quanpham.model.Users
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.makeGone
import com.example.quanpham.utility.makeVisible
import com.example.quanpham.utility.showToast
import com.google.firebase.Firebase
import com.google.firebase.database.database

class SignInActivity : BaseActivity<ActivitySignInBinding>() {
    override fun getViewBinding() = ActivitySignInBinding.inflate(layoutInflater)
    private var email = ""
    private var pass = ""
    private var isPasswordVisible = false


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
        email = binding.tvEmail.text.toString()
        pass = binding.tvPassword.text.toString()

        if (email.isEmpty() || pass.isEmpty()) {
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }
        return true
    }

    override fun createView() {
        binding.llLoading.makeGone()
        binding.btnSignin.setOnClickListener {
            if (checkValue()) {
                hideKeyboard()
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
        binding.ivVisiblePass.setOnClickListener {
            isPasswordVisible = ! isPasswordVisible
            if(isPasswordVisible){
                binding.tvPassword.transformationMethod = null
                binding.tvPassword.setSelection(binding.tvPassword.length())
            }
            else{
                binding.tvPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.tvPassword.setSelection(binding.tvPassword.length())
            }
        }
        binding.tvPassword.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                if (checkValue()) {
                    hideKeyboard()
                    signApp()
                } else {
                    showToast(getString(R.string.enter_all_value))
                }
                true
            }
            else
                false
        }
    }
    private fun signApp() {
        binding.llLoading.makeVisible()
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
                binding.llLoading.makeGone()
            }
    }
}