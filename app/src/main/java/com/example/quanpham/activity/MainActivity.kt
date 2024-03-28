package com.example.quanpham.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.quanpham.R
import com.example.quanpham.databinding.ActivityMainBinding
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.db.model.Steps
import com.example.quanpham.db.model.Weights
import com.example.quanpham.fragment.HomeFragment
import com.example.quanpham.fragment.SettingFragment
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.Constant.CHANNEL_ID_STEP
import com.example.quanpham.utility.showToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class MainActivity : BaseActivity<ActivityMainBinding>(){
    companion object{
        fun startMain(context: Context, clearTask : Boolean ){
            val intent = Intent(context, MainActivity::class.java).apply {
                if(clearTask){
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(intent)
        }

    }
    private val HOME_FRAGMENT = HomeFragment.instance()
//    private val REPORT_FRAGMENT = ReportFragment.instance()
//    private val HEALTHY_FRAGMENT = HealthFragment.instance()
    private val SETTINGS_FRAGMENT = SettingFragment.instance()
    private var currentFragment  : Fragment = HOME_FRAGMENT

    private fun initBottomNav(){
        binding.bottomNav.setOnApplyWindowInsetsListener(null)
        supportFragmentManager.beginTransaction().add(binding.layoutAddFragmentMain.id,HOME_FRAGMENT).
        show(HOME_FRAGMENT).commit()
//        supportFragmentManager.beginTransaction().add(binding.layoutAddFragmentMain.id,REPORT_FRAGMENT).
//        hide(REPORT_FRAGMENT).commit()
//        supportFragmentManager.beginTransaction().add(binding.layoutAddFragmentMain.id,HEALTHY_FRAGMENT).
//        hide(HEALTHY_FRAGMENT).commit()
//        supportFragmentManager.beginTransaction().add(binding.layoutAddFragmentMain.id,SETTINGS_FRAGMENT).
//        hide(SETTINGS_FRAGMENT).commit()
    }
    private fun showFragment(showFragment: Fragment, hideFragment: Fragment){
        supportFragmentManager.beginTransaction().remove(hideFragment).commit()
        supportFragmentManager.beginTransaction().add(binding.layoutAddFragmentMain.id,showFragment).commit()
    }
    private fun setListeners() {
        binding.bottomNav.selectedItemId = R.id.action_home
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    if (currentFragment != HOME_FRAGMENT){
                        showFragment(HOME_FRAGMENT,currentFragment)
                        currentFragment = HOME_FRAGMENT

                    }
                }
//                R.id.action_report -> {
//                    if (currentFragment != REPORT_FRAGMENT){
//                        showFragment(REPORT_FRAGMENT,currentFragment)
//                        currentFragment = REPORT_FRAGMENT
//                    }
//                }
//
//                R.id.action_health -> {
//                    if (currentFragment != HEALTHY_FRAGMENT){
//                        showFragment(HEALTHY_FRAGMENT,currentFragment)
//                        currentFragment = HEALTHY_FRAGMENT
//                    }
//                }
                R.id.action_setting -> {
                    if (currentFragment != SETTINGS_FRAGMENT){
                        showFragment(SETTINGS_FRAGMENT,currentFragment)
                        currentFragment = SETTINGS_FRAGMENT
                    }
                }
            }
            true
        }
    }


    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun createView() {
        initBottomNav()
        setListeners()
    }

}