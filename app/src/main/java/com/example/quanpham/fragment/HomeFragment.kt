package com.example.quanpham.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentHomeBinding
import com.example.quanpham.db.model.Steps
import com.example.quanpham.db.model.Weights
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.showToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    companion object {
        fun instance(): HomeFragment {
            return newInstance(HomeFragment::class.java)
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        usLoggin?.observe(this@HomeFragment) {
            binding.btnStepGoal.text = "" + it?.email

        }
        addDB()


    }

    override fun onStop() {
        super.onStop()
        Log.d("abcd", "Stop home")
    }

    fun addDB() {
        try {


            val database = Firebase.database
            val myRef = database.getReference("message")

            myRef.setValue("Hello, World!")
            Log.d("abcd", "đã chạy vào đây")

            val auth = FirebaseAuth.getInstance()
            val fbDatabase = FirebaseDatabase.getInstance()

            val currentUser = auth.currentUser
            currentUser?.let { user ->
                fbDatabase.getReference(Constant.KEY_WEIGHT)
                    .child("123")
                    .push()
                    .setValue(Weights(1, 55F, ""))
                    .addOnSuccessListener {
                        showToast("Thành công")
                        Log.d("abcd", "đã chạy")
                    }
                    .addOnFailureListener {
                        showToast(it.message.toString())
                        Log.d("abcd", "lỗi ${it.message}")
                    }

                fbDatabase.getReference(Constant.KEY_STEP)
                    .child(user.uid)
                    .push()
                    .setValue(Steps(1, 1000, "", 20, 150, 2))
            } ?: run {
                // Handle case where currentUser is null
                showToast("User not authenticated")
            }
        } catch (e: Exception) {
            Log.d("abcd", "Err ${e.message}")
        }
    }

}