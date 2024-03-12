package com.example.quanpham.fragment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quanpham.R
import com.example.quanpham.activity.LanguageActivity
import com.example.quanpham.activity.SignInActivity
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentSettingsBinding
import com.example.quanpham.language.Language
import com.example.quanpham.lib.SharedPreferenceUtils
import java.lang.StringBuilder
import java.text.DecimalFormat

class SettingFragment : BaseFragment<FragmentSettingsBinding>() {

    var listLanguages: ArrayList<Language> = arrayListOf()
    val df = DecimalFormat("#.#")
    companion object{
        fun instance() : SettingFragment{
            return newInstance(SettingFragment::class.java)
        }
        const val LINK_TERM ="https://sites.google.com/view/step-counter-walk-tracker-tos/home"
        const val LINK_POLICY ="https://sites.google.com/view/step-counter-track-walk-policy/home"
    }
    override fun onStop() {
        super.onStop()
        Log.d("abcd","Stop setting")
    }

    override fun initView() {
        setValue()
        binding.btnStepGoal.setOnClickListener {
        }
        binding.txtSex.setOnClickListener {
        }
        binding.txtAge.setOnClickListener {
        }
        binding.txtWeight.setOnClickListener {
        }
        binding.txtMetric.setOnClickListener {
        }
        binding.txtSensitive.setOnClickListener {
        }
        binding.btnLanguage.setOnClickListener{
            LanguageActivity.start(requireContext(), openFromMain = true, clearTask = false)
        }
        binding.btnReminder.setOnClickListener{
        }
        binding.btnStepLength.setOnClickListener{
        }

        binding.title.setOnClickListener {
            SignInActivity.start(this@SettingFragment.requireContext(),true)
            auth.signOut()
        }

    }

    override fun onResume() {
        super.onResume()
        setValue()
    }
    fun setValue(){
        initDataLanguage()
        checkLanguage()


    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container,false)
    }
    fun initDataLanguage() {
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
    fun checkLanguage(){
        for (language in listLanguages) {
            if (SharedPreferenceUtils.languageCode == language.locale) {
                binding.txtNameLanguage.text = language.title
            }
        }
    }


}