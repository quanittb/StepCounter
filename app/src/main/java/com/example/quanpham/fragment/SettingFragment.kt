package com.example.quanpham.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quanpham.R
import com.example.quanpham.activity.LanguageActivity
import com.example.quanpham.activity.SignInActivity
import com.example.quanpham.activity.SignUpActivity
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentSettingsBinding
import com.example.quanpham.dialog.StepGoalBottomDialog
import com.example.quanpham.dialog.StepGoalBottomDialog.OnClickBottomSheetListener
import com.example.quanpham.language.Language
import com.example.quanpham.lib.SharedPreferenceUtils
import com.mobiai.app.ui.dialog.AgeDialog
import com.mobiai.app.ui.dialog.GenderDialog
import java.text.DecimalFormat

class SettingFragment : BaseFragment<FragmentSettingsBinding>() {

    var listLanguages: ArrayList<Language> = arrayListOf()
    private var bottomSheetStepGoalDialog: StepGoalBottomDialog? = null
    private var bottomSheetGenderDialog: GenderDialog? = null
    private var bottomSheetAgeDialog: AgeDialog? = null

    companion object{
        fun instance() : SettingFragment{
            return newInstance(SettingFragment::class.java)
        }
    }
    override fun onStop() {
        super.onStop()
    }

    override fun initView() {
        setValue()
        binding.btnStepGoal.setOnClickListener {
            openStepGoalBottomSheet()
        }
        binding.txtSex.setOnClickListener {
            openGenderBottomSheet()
        }
        binding.txtAge.setOnClickListener {
            openAgeBottomSheet()
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

        binding.txtLogOut.setOnClickListener {
            SignInActivity.start(this@SettingFragment.requireContext(),true)
            auth.signOut()
        }

    }
    private fun openStepGoalBottomSheet() {
        if (bottomSheetStepGoalDialog == null) {
            bottomSheetStepGoalDialog = StepGoalBottomDialog(
                requireContext(),
                object : OnClickBottomSheetListener {
                    override fun onClickSaveFrom() {
                        binding.btnStepGoal.text = SharedPreferenceUtils.targetStep.toString()

                    }

                })
        }

        bottomSheetStepGoalDialog?.checkShowBottomSheet()
    }

    private fun openGenderBottomSheet() {
        if (bottomSheetGenderDialog == null) {
            bottomSheetGenderDialog = GenderDialog(
                requireContext(),
                object : OnClickBottomSheetListener {
                    override fun onClickSaveFrom() {
                        if(SharedPreferenceUtils.selectSex == 1)
                            binding.txtSex.text = getText(R.string.male)
                        else if(SharedPreferenceUtils.selectSex == 0)
                            binding.txtSex.text = getText(R.string.female)
                    }

                })
        }

        bottomSheetGenderDialog?.checkShowBottomSheet()
    }
    private fun openAgeBottomSheet() {
        if (bottomSheetAgeDialog == null) {
            bottomSheetAgeDialog = AgeDialog(
                requireContext(),
                object : OnClickBottomSheetListener {
                    override fun onClickSaveFrom() {
                       binding.txtAge.text = SharedPreferenceUtils.age.toString()
                    }

                })
        }

        bottomSheetAgeDialog?.checkShowBottomSheet()
    }
    override fun onResume() {
        super.onResume()
        setValue()
    }
    fun setValue(){
        initDataLanguage()
        checkLanguage()
        binding.btnStepGoal.text = SharedPreferenceUtils.targetStep.toString()
        if(SharedPreferenceUtils.selectSex == 1)
            binding.txtSex.text = getText(R.string.male)
        else if(SharedPreferenceUtils.selectSex == 0)
            binding.txtSex.text = getText(R.string.female)

        binding.txtAge.text = SharedPreferenceUtils.age.toString()


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