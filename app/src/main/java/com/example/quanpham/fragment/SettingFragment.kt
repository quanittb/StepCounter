package com.example.quanpham.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.quanpham.R
import com.example.quanpham.activity.LanguageActivity
import com.example.quanpham.activity.SignInActivity
import com.example.quanpham.activity.SignUpActivity
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentSettingsBinding
import com.example.quanpham.db.model.Weights
import com.example.quanpham.dialog.StepGoalBottomDialog
import com.example.quanpham.dialog.StepGoalBottomDialog.OnClickBottomSheetListener
import com.example.quanpham.language.Language
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.Constant.cmToIn
import com.example.quanpham.utility.Constant.kgToLb
import com.example.quanpham.utility.Constant.lbToKg
import com.example.quanpham.utility.getEndOfDay
import com.example.quanpham.utility.getStartOfDay
import com.example.quanpham.utility.rxbus.ChangeUnit
import com.example.quanpham.utility.rxbus.NumberHeight
import com.example.quanpham.utility.rxbus.RxBus
import com.example.quanpham.utility.rxbus.StepLengthUpdate
import com.example.quanpham.utility.rxbus.WeightUpdate
import com.example.quanpham.utility.rxbus.listenEvent
import com.mobiai.app.ui.dialog.AgeDialog
import com.mobiai.app.ui.dialog.GenderDialog
import com.mobiai.app.ui.dialog.MetricDialog
import com.mobiai.app.ui.dialog.WeightDialog
import java.text.DecimalFormat
import java.util.Date

class SettingFragment : BaseFragment<FragmentSettingsBinding>() {

    var listLanguages: ArrayList<Language> = arrayListOf()
    private var bottomSheetStepGoalDialog: StepGoalBottomDialog? = null
    private var bottomSheetGenderDialog: GenderDialog? = null
    private var bottomSheetAgeDialog: AgeDialog? = null
    private var bottomSheetWeightDialog: WeightDialog? = null
    private var bottomSheetMetricDialog: MetricDialog? = null


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
        handlerEvent()
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
            openWeightBottomSheet()
        }
        binding.txtMetric.setOnClickListener {
            openMetricBottomSheet()
        }
        binding.btnLanguage.setOnClickListener{
            LanguageActivity.start(requireContext(), openFromMain = true, clearTask = false)
        }
        binding.btnReminder.setOnClickListener{
        }
        binding.btnStepLength.setOnClickListener{
            addFragment(StepLengthFragment.instance())
        }

        binding.txtLogOut.setOnClickListener {
            SignInActivity.start(this@SettingFragment.requireContext(),true)
            auth.signOut()
        }

    }
    fun setValue(){
        initDataLanguage()
        checkLanguage()
        binding.tvName.text = SharedPreferenceUtils.name
        if(SharedPreferenceUtils.selectSex==0)
            binding.txtSex.text = resources.getString(R.string.female)
        if(SharedPreferenceUtils.selectSex==1)
            binding.txtSex.text = resources.getString(R.string.male)
        binding.btnStepGoal.text = SharedPreferenceUtils.targetStep.toString()
        if(SharedPreferenceUtils.selectSex == 1)
            binding.txtSex.text = getText(R.string.male)
        else if(SharedPreferenceUtils.selectSex == 0)
            binding.txtSex.text = getText(R.string.female)

        binding.txtAge.text = SharedPreferenceUtils.age.toString()
        if(SharedPreferenceUtils.unit)
            binding.txtWeight.text = SharedPreferenceUtils.weight.toString()
        else
            binding.txtWeight.text = "${SharedPreferenceUtils.weight * kgToLb}"
        binding.txtStepLength.text = SharedPreferenceUtils.stepLength.toString()

    }
    private fun handlerEvent() {
        addDispose(listenEvent({
            when (it) {
                is StepLengthUpdate -> {
                    binding.txtStepLength.text = SharedPreferenceUtils.stepLength.toString()
                }
            }
        }, {}))
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
    private fun openWeightBottomSheet() {
        if (bottomSheetWeightDialog == null) {
            bottomSheetWeightDialog = WeightDialog(
                requireContext(),
                object : WeightDialog.OnClickBottomSheetListener {
                    override fun onClickSaveFrom() {
                        Log.d("abcd","weight : ${SharedPreferenceUtils.weight}")
                        if(SharedPreferenceUtils.unit)
                        {
                            binding.txtMetric.text = resources.getString(R.string.kg_cm_km)
                            if(SharedPreferenceUtils.weight !=0f)
                                binding.txtWeight.text = "${String.format("%.2f",SharedPreferenceUtils.weight)} ${resources.getString(R.string.kg)}"
                            if(SharedPreferenceUtils.stepLength !=0f)
                                binding.txtStepLength.text = "${String.format("%.2f",SharedPreferenceUtils.stepLength)} ${resources.getString(R.string.cm)}"
                        }
                        else
                        {
                            binding.txtMetric.text = resources.getString(R.string.lbs_ft_mile)
                            if(SharedPreferenceUtils.stepLength !=0f)
                                binding.txtStepLength.text = "${String.format("%.2f",SharedPreferenceUtils.height * cmToIn)} ${resources.getString(R.string.ft)}"
                            if(SharedPreferenceUtils.weight !=0f)
                            //binding.txtWeight.text = "${String.format("%.2f",SharedPreferenceUtils.weight * 2.20462.toFloat())} ${resources.getString(R.string.lbs)}"
                                binding.txtWeight.text = "${SharedPreferenceUtils.weight1_temporary} ${resources.getString(R.string.lbs)}"
                        }
                        updateWeightDB(SharedPreferenceUtils.unit)
                    }
                })
        }

        bottomSheetWeightDialog?.checkShowBottomSheet()
    }
    private fun openMetricBottomSheet() {
        bottomSheetMetricDialog = null
        if (bottomSheetMetricDialog == null) {
            bottomSheetMetricDialog = MetricDialog(
                requireContext(),
                object : MetricDialog.OnClickBottomSheetListener {
                    override fun onClickSaveFrom() {
                        Log.d("TAG", "onClickSaveFrom: ${SharedPreferenceUtils.unit}")
                        if(SharedPreferenceUtils.unit)
                        {
                            RxBus.publish(NumberHeight(true))
                            binding.txtMetric.text = resources.getString(R.string.kg_cm_km)

                            if(SharedPreferenceUtils.weight !=0f){
                                binding.txtWeight.text = "${String.format("%.2f",SharedPreferenceUtils.weight)} ${resources.getString(R.string.kg)}"
                            }
                            if(SharedPreferenceUtils.stepLength !=0f){
                                binding.txtStepLength.text = "${String.format("%.2f",SharedPreferenceUtils.stepLength)} ${resources.getString(R.string.cm)}"
                            }

                        }
                        else
                        {
                            RxBus.publish(NumberHeight(false))
                            binding.txtMetric.text = resources.getString(R.string.lbs_ft_mile)
                            if(SharedPreferenceUtils.stepLength !=0f)
                                binding.txtStepLength.text = "${String.format("%.2f",SharedPreferenceUtils.stepLength / 30.48.toFloat())} ${resources.getString(R.string.ft)}"
                            if(SharedPreferenceUtils.weight !=0f)
                                binding.txtWeight.text = "${String.format("%.2f",SharedPreferenceUtils.weight * 2.20462.toFloat())} ${resources.getString(R.string.lbs)}"
                        }
                        RxBus.publish(ChangeUnit())
                    }


                })
        }

        bottomSheetMetricDialog?.checkShowBottomSheet()
    }
    private fun updateWeightDB(isKg:Boolean){
            if (database.weightDao().getWeightDay(getStartOfDay(System.currentTimeMillis()),
                    getEndOfDay(System.currentTimeMillis())) == null){
                val weight = Weights(null,null, Date())
                if(isKg){
                    weight.weight = SharedPreferenceUtils.weight
                    SharedPreferenceUtils.unit = true
                }
                else{
                    weight.weight = SharedPreferenceUtils.weight * lbToKg
                    SharedPreferenceUtils.unit = false
                }
                database.weightDao().insert(weight)
            }
            else{
                val weight = database.weightDao().getWeightDay(getStartOfDay(System.currentTimeMillis()),
                    getEndOfDay(System.currentTimeMillis()))
                if(isKg){
                    weight.weight = SharedPreferenceUtils.weight
                    SharedPreferenceUtils.unit = true
                }
                else{
                    weight.weight = SharedPreferenceUtils.weight * lbToKg
                    SharedPreferenceUtils.unit = false
                }
                database.weightDao().updateWeight(weight)
            }
    }
    override fun onResume() {
        super.onResume()
        setValue()
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