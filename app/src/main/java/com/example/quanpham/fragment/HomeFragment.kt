package com.example.quanpham.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    companion object{
        fun instance(): HomeFragment{
            return newInstance(HomeFragment::class.java)
        }
    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        usLoggin?.observeForever {
            binding.btnStepGoal.text=""+it?.email
        }

    }

    override fun onStop() {
        super.onStop()
        Log.d("abcd","Stop home")
    }
}