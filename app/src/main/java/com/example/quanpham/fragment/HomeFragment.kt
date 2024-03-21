package com.example.quanpham.fragment

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.quanpham.R
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentHomeBinding
import com.example.quanpham.db.model.Steps
import com.example.quanpham.db.model.Weights
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.services.StepServices
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.getHour
import com.example.quanpham.utility.getTimeNow
import com.example.quanpham.utility.logD
import com.example.quanpham.utility.makeGone
import com.example.quanpham.utility.makeVisible
import com.example.quanpham.utility.showToast
import java.util.Calendar
import java.util.Date

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    companion object {
        var currentStep = MutableLiveData<Int>()
        fun instance(): HomeFragment {
            return newInstance(HomeFragment::class.java)
        }
    }
    private lateinit var serviceIntent:Intent


    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        usLoggin?.observe(this@HomeFragment) {
            binding.btnStepGoal.text = "" + it?.userName
            serviceIntent = Intent(requireContext(), StepServices::class.java)
            setListener()
            setWellcome()
            startService()
        }
//        addDB()
    }
    private fun setListener(){
        binding.tvContentHeader.text = SharedPreferenceUtils.dayStep.toString()
        binding.tvStepRealTime.text = SharedPreferenceUtils.dayStep.toString()
        currentStep.observe(this@HomeFragment){
            binding.tvContentHeader.text = it.toString()
            binding.tvStepRealTime.text = it.toString()
        }
        binding.ivStepStart.setOnClickListener{
            if(!SharedPreferenceUtils.startStep){
                SharedPreferenceUtils.startStep = true
                startService()
                binding.ivStepStart.setImageResource(R.drawable.ic_pause_step_home)
                binding.tvPause.makeGone()
                binding.lnTarget.makeVisible()
            }
            else{
                stopService()
                SharedPreferenceUtils.startStep = false
                binding.ivStepStart.setImageResource(R.drawable.ic_step_pause)
                binding.tvPause.makeVisible()
                binding.lnTarget.makeGone()
            }
            }
    }
    private fun startService() {
        requireContext().startService(serviceIntent)
    }
    private fun stopService() {
        requireContext().stopService(serviceIntent)
    }
    override fun onStop() {
        super.onStop()
        Log.d("abcd", "Stop home")
    }

    fun addDB() {
        try {
            val currentUser = auth.currentUser
            currentUser?.let { user ->
                fbDatabase.getReference(Constant.KEY_WEIGHT)
                    .child(user.uid)
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
                val currentTime = Date()
                fbDatabase.getReference(Constant.KEY_STEP)
                    .child(user.uid)
                    .push()
                    .setValue(Steps(1, 1000, currentTime , 20, 150, 2))
            } ?: run {
                // Handle case where currentUser is null
                showToast("User not authenticated")
            }
        } catch (e: Exception) {
            Log.d("abcd", "Err ${e.message}")
        }
    }

    private fun setWellcome(){
        val hour = getHour()
        when(hour){
            in 5..12 -> binding.tvWellCome.text = getString(R.string.good_morning)
            in 13..17 ->binding.tvWellCome.text = getString(R.string.good_afternoon)
           else ->binding.tvWellCome.text = getString(R.string.good_evening)
        }
    }

}