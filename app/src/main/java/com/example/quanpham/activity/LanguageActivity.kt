package com.example.quanpham.activity

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build

import android.util.Log
import com.example.quanpham.R
import com.example.quanpham.adapter.LanguageAdapter

import com.example.quanpham.base.BaseActivity
import com.example.quanpham.databinding.ActivityLanguageBinding
import com.example.quanpham.language.Language
import com.example.quanpham.language.LanguageUtil
import com.example.quanpham.lib.SharedPreferenceUtils


class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    companion object {
        const val OPEN_FROM_MAIN = "open_from_main"
        fun start(context: Context,openFromMain : Boolean = false, clearTask : Boolean = true){
            val intent = Intent(context, LanguageActivity::class.java).apply {
                if(clearTask){
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                if (openFromMain){
                    putExtra(OPEN_FROM_MAIN, true)
                }
            }
            context.startActivity(intent)
        }
    }
    private val TAG = LanguageActivity::javaClass.name
    var listLanguages: ArrayList<Language> = arrayListOf()
    var languageCode = "en"
    lateinit var languageAdapter: LanguageAdapter

    fun getLayoutResourceId(): Int = R.layout.activity_language

    override fun getViewBinding(): ActivityLanguageBinding  = ActivityLanguageBinding.inflate(layoutInflater)

    override fun createView() {
        showFullscreen(true)
        getDataLanguage()
        if (!intent.getBooleanExtra(OPEN_FROM_MAIN, false)) {
        } else {

        }
        binding.imgConfirm.setOnClickListener {
            changeLanguage()
            Log.d("TAGGG", "---------->  getLangueCode: $languageCode")
        }
        binding.imgBack.setOnClickListener {
            finish()
        }

    }




    private fun initAdapter(){
        languageAdapter =  LanguageAdapter(this, object : LanguageAdapter.OnLanguageClickListener{
            override fun onClickItemListener(language: Language?) {
                languageCode = language!!.locale
            }
        })
        languageAdapter.setItems(listLanguages)
        binding.recyclerViewLanguage.adapter = languageAdapter
    }

    private fun getDataLanguage() {
        initData()
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales.get(0)
        } else {
            Resources.getSystem().configuration.locale
        }
        var languageSystem: Language? = null
        var position = 0
        for (language in listLanguages) {
            if (intent.getBooleanExtra(OPEN_FROM_MAIN, false)) {
                if (SharedPreferenceUtils.languageCode == language.locale) {
                    languageSystem = language
                    languageCode = languageSystem.locale
                }
            }else
                if (language.locale.equals(locale.language)) {
                    languageSystem = language
                    languageCode = locale.language
                }
        }
        if (languageSystem != null) {
            listLanguages.remove(languageSystem)
            listLanguages.add(0, languageSystem)
        }
        listLanguages[position].isChoose = true
        initAdapter()
    }

    private fun changeLanguage() {
        SharedPreferenceUtils.languageCode = languageCode

        LanguageUtil.changeLang(SharedPreferenceUtils.languageCode!!, this)
        SharedPreferenceUtils.firstOpenApp = false
        if(usLoggin?.value==null){
            SignInActivity.start(this, true)
        }else{
            MainActivity.startMain(this, true)
        }

    }

    fun initData() {
        listLanguages = ArrayList()
        listLanguages.add(Language(R.drawable.flag_en, getString(R.string.language_english), "en"))
        listLanguages.add(Language(R.drawable.flag_vn_vietnam,
            getString(R.string.language_vietnam),
            "vi"))
        listLanguages.add(Language(R.drawable.flag_fr_france,
            getString(R.string.language_france),
            "fr"))
        listLanguages.add(Language(R.drawable.flag_es_spain,
            getString(R.string.language_spain),
            "es"))

        listLanguages.add(Language(R.drawable.flag_de_germany,
            getString(R.string.language_germany),
            "de"))
        listLanguages.add(Language(R.drawable.flag_ko_korean,
            getString(R.string.language_korean),
            "ko"))

    }




}