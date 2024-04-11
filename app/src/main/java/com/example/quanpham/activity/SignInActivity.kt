package com.example.quanpham.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.EditorInfo
import com.example.quanpham.R
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.databinding.ActivitySignInBinding
import com.example.quanpham.db.model.Steps
import com.example.quanpham.db.model.StepsFirebase
import com.example.quanpham.fragment.ForgotPassFragment
import com.example.quanpham.model.Users
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.getDateFromTimeMillis
import com.example.quanpham.utility.logD
import com.example.quanpham.utility.makeGone
import com.example.quanpham.utility.makeVisible
import com.example.quanpham.utility.showToast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database

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
                        val ref = fbDatabase.getReference(Constant.KEY_STEP)
                            .child(Firebase.auth.currentUser!!.uid)
//                        val ref1 = mdatabase.getReference("Weights")
//                            .child(com.google.firebase.ktx.Firebase.auth.currentUser!!.uid)
                        var count = 0
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                database.stepDao().deleteAllStep()
                                if(!dataSnapshot.exists())
                                    MainActivity.startMain(this@SignInActivity,true)
                                for (snapshot in dataSnapshot.children) {
                                    val data = snapshot.getValue(StepsFirebase::class.java)
                                    data?.let {
                                                it.isPush = true
                                                val step = Steps(null,it.step,
                                                    getDateFromTimeMillis(it.startTime.time),it.activeTime,it.calo,it.distance,true)
                                                database.stepDao().insert(step)
                                            }
                                    count++

                                    if(count == dataSnapshot.childrenCount.toInt()){
//                                        MainActivity.startMain(this@SignInActivity,true)
                                        SplashActivity.startMain(this@SignInActivity,true)
//                                        ref1.addValueEventListener(object : ValueEventListener {
//                                            override fun onDataChange(snapshot: DataSnapshot) {
//                                                database.weightDao().deleteAllWeights()
//                                                val weights
//                                            }
//
//                                            override fun onCancelled(error: DatabaseError) {
//                                                TODO("Not yet implemented")
//                                            }
//                                        })
                                    }
                                }


                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Xử lý lỗi nếu có
                            }
                        })

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