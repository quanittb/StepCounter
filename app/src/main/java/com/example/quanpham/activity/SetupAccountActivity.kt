package com.example.quanpham.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.example.quanpham.base.BaseActivity
import com.example.quanpham.databinding.ActivitySetupAccountBinding
import com.mobiai.app.ui.fragment.SetupAccountGenderFragment

class SetupAccountActivity : BaseActivity<ActivitySetupAccountBinding>() {
    companion object{
        fun startMain(context: Context, clearTask : Boolean ){
            val intent = Intent(context, SetupAccountActivity::class.java).apply {
                if(clearTask){
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(intent)
        }

    }

    override fun getViewBinding(): ActivitySetupAccountBinding {
        return ActivitySetupAccountBinding.inflate(layoutInflater)
    }

    override fun createView() {
        addFragment(SetupAccountGenderFragment.instance())

    }
    private fun hideNavigationBar() {
        window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }

    override fun onResume() {
        super.onResume()
//        hideNavigationBar()
    }

}