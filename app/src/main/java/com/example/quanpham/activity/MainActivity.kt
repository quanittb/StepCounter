package com.example.quanpham.activity

import com.example.quanpham.databinding.ActivityMainBinding
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.fragment.HomeFragment

class MainActivity : BaseActivity<ActivityMainBinding>(){


    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun createView() {
//        database.userDao().insert(Users())
        addFragment(HomeFragment.instance())
    }

}