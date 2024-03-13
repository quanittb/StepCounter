package com.example.quanpham.activity

import android.content.Context
import android.content.Intent
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
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun createView() {
        initBottomNav()
        setListeners()
        addDB()
    }

}