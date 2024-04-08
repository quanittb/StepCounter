package com.example.quanpham.fragment

import DateUtils.getCurrentYear
import DateUtils.getMillis
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quanpham.R
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentHealthBinding
import com.example.quanpham.db.model.Weights
import com.example.quanpham.dialog.AddWeightDialog
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.utility.Constant.KG
import com.example.quanpham.utility.Constant.LB
import com.example.quanpham.utility.Constant.cmToIn
import com.example.quanpham.utility.Constant.kgToLb
import com.example.quanpham.utility.Constant.lbToKg
import com.example.quanpham.utility.formatNumbers
import com.example.quanpham.utility.getDayOfMonth
import com.example.quanpham.utility.getEndOfDay
import com.example.quanpham.utility.getEndOfYear
import com.example.quanpham.utility.getMonthOfYear
import com.example.quanpham.utility.getStartOfDay
import com.example.quanpham.utility.getStartOfDayMinus
import com.example.quanpham.utility.getStartOfYear
import com.example.quanpham.utility.rxbus.ChangeUnit
import com.example.quanpham.utility.rxbus.NumberHeight
import com.example.quanpham.utility.rxbus.WeightUpdate
import com.example.quanpham.utility.rxbus.listenEvent
import com.mobiai.app.ui.dialog.EditBMIDialog
import com.mobiai.base.chart.LineChartView
import java.util.Calendar
import java.util.Date

class HealthFragment : BaseFragment<FragmentHealthBinding>() {
    private val TAG = HealthFragment::javaClass.name
    private val listWeight: MutableList<Weights> = mutableListOf()


    companion object {
        fun instance(): HealthFragment {
            return newInstance(HealthFragment::class.java)
        }
    }


    override fun initView() {
        updateWeightData()
        binding.run {
            tvBmiScore.text =
                SharedPreferenceUtils.bmi?.let { formatNumbers(it) }
            binding.bmiChart.setValue(SharedPreferenceUtils.bmi!!.toFloat())
            calculateAndUpdateBmi()
            ibEdit.setOnClickListener {
                showBmiDialog()
            }
        }

        val listBoxValue = arrayListOf(
            getString(R.string.underweight), getString(R.string.normal),
            getString(R.string.overweight), getString(R.string.obesity), getString(R.string.obesity)
        )
        binding.bmiChart.setListBoxValue(listBoxValue)
        handlerEvent()
        setMarqueeText()
    }

    private fun setMarqueeText() {
        binding.run {
            tvCurrentWeight.isSelected = true
            tvBmiScore.isSelected = true
            tvLast30DaysWeight.isSelected = true
        }
    }

    //            Tính BMI dựa vào đơn vị hệ met và kg
    fun calculateBmiInGlobalUnit(weightInKg: Float, heighInMetter: Float): Float {
        return weightInKg / (heighInMetter * heighInMetter)
    }

    //           Tính BMI dựa vào đơn vị hệ pound và inch
    fun calculateBmiInUSUnit(weightInLb: Float, heighInInches: Float): Float {
        return (weightInLb * 703) / (heighInInches * heighInInches)

    }

    private fun calculateAndUpdateBmi() {
        val unit = getUnitType()
        val bmi =
            if (unit == KG) {
                calculateBmiInGlobalUnit(
                    SharedPreferenceUtils.weight,
                    (SharedPreferenceUtils.height / 100)
                )
            } else {
                calculateBmiInUSUnit(
                    (SharedPreferenceUtils.weight * kgToLb),
                    (SharedPreferenceUtils.height * cmToIn)
                )
            }
        binding.tvBmiScore.text = formatNumbers(bmi)
        binding.bmiChart.setValue(bmi)
    }

    private fun updateWeightData() {
        val unit = getUnitType()
        val listStr: MutableList<String> = mutableListOf()
        val dataLists: MutableList<LineChartView.Data> = mutableListOf()
        val currentYear = getCurrentYear()
        listWeight.clear()
        listWeight.addAll(
            database.weightDao()
                .getWeights(getStartOfYear(getCurrentYear()), getEndOfYear(getCurrentYear()))
        )
        var date = ""
        if (listWeight.size > 0) {
            listWeight.sortBy {
                it.updateTime
            }
            for (i in 0 until listWeight.size) {
                val weight = listWeight[i]

                val month = getMonthOfYear(weight.updateTime!!.getMillis())
                date = DateUtils.getMonthInStringText(requireContext(), month) + "${
                    getDayOfMonth(weight.updateTime!!.getMillis())
                }"
                if (listWeight.size <= 6) {
                    listStr.add(date)
                } else if (i % 5 == 0) {
                    listStr.add(date)
                }
                if (weight.weight != null) {
                    val w =
                        if (unit == KG) weight.weight!!.toFloat() else (weight.weight!! / lbToKg)
                    dataLists.add(
                        LineChartView.Data(
                            w,
                            "${formatNumbers(w)} $unit",
                            date
                        )
                    )
                }
            }
            if (listStr.size > 0) {
                binding.chartView.setVerticalValue(listStr)
            }
            if (dataLists.size > 0) {
                binding.chartView.setData(dataLists)
                val index = dataLists.indexOf(dataLists.find { it.valueDate == date })
                binding.chartView.setSelectToday(index)
            }
        }
        binding.run {
            tvCurrentYear.text = "$currentYear"
            btAddWeight.setOnClickListener {
                openDialogAddWeight()
            }
        }

        showCurrentWeight()
    }

    private fun showCurrentWeight() {
        val unit = getUnitType()
        val currentDayWeight = SharedPreferenceUtils.weight
        val currentDayWeightInUnit = if (unit == KG) currentDayWeight else currentDayWeight / lbToKg
        binding.tvCurrentWeight.text = buildString {
            append(formatNumbers(currentDayWeightInUnit))
            append(unit)
        }
        var lastWeightMax = 0.0f
        var lastWeightMin = currentDayWeight
        var diff = 0f
        val weights = database.weightDao().getWeights(
            getStartOfDayMinus(System.currentTimeMillis(), 30),
            getEndOfDay(System.currentTimeMillis())
        )
        for (weight in weights) {
            if (weight.weight!! > lastWeightMax) {
                lastWeightMax = if (unit == KG) weight.weight!! else weight.weight!! / lbToKg
            }
            if (weight.weight!! < lastWeightMin) {
                lastWeightMin = if (unit == KG) weight.weight!! else weight.weight!! / lbToKg
            }
        }
        if (lastWeightMax <= currentDayWeight) diff = currentDayWeight - lastWeightMin
        if (lastWeightMin >= currentDayWeight) diff = currentDayWeight - lastWeightMax
        binding.tvLast30DaysWeight.apply {
            this.text = if (diff > 0) "+" + formatNumbers(diff) + unit
            else formatNumbers(diff) + unit

            this.setTextColor(
                if (diff >= 0) requireContext().resources.getColor(R.color.increase) else requireContext().getColor(
                    R.color.decrease
                )
            )
        }
    }

    private fun handlerEvent() {
        addDispose(listenEvent({
            when (it) {
                is NumberHeight -> {
                    showCurrentWeight()
                    updateWeightData()
                }

                is WeightUpdate -> {
                    showCurrentWeight()
                    updateWeightData()
                }

                is ChangeUnit -> {
                    showCurrentWeight()
                    updateWeightData()
                }
            }
        }, {}))
    }

    private fun openDialogAddWeight() {
        var weightData = database.weightDao().getWeights(
            getStartOfDay(System.currentTimeMillis()),
            getEndOfDay(System.currentTimeMillis())
        )
        val addWeightDialog =
            AddWeightDialog(requireContext(), object : AddWeightDialog.OnSaveChangeWeightListener {
                override fun onSave(weight: Float, calendar: Calendar) {
                    val cal = Calendar.getInstance()
                    if (calendar.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)) {
                        SharedPreferenceUtils.weight = weight
                    }
                    if (weightData.isEmpty()) {
                        database.weightDao().insert(Weights(null, weight, calendar.time))
                    } else {
                        weightData[0].weight = weight
                        weightData[0].updateTime = calendar.time
                        database.weightDao().updateWeight(weightData[0])
                    }

                    updateWeightData()
                    calculateAndUpdateBmi()
                }

                override fun onNotSave() {
                }

            })

        addWeightDialog.show()
    }

    private fun showBmiDialog() {
        val bmiDialog =
            EditBMIDialog(requireContext(), object : EditBMIDialog.OnSaveChangeBmiListener {
                override fun onSave(bmi: String) {
                    Log.i(TAG, "onSave: $bmi")
                    binding.tvBmiScore.text = bmi
                    binding.bmiChart.setValue(bmi.toFloat())
                    SharedPreferenceUtils.bmi = bmi.toFloat()
                    updateWeightData()
                }

                override fun onNotSave() {
                }

                override fun onChangeWeight(weight: Float) {
                    var weightData = database.weightDao().getWeights(
                        getStartOfDay(System.currentTimeMillis()),
                        getEndOfDay(System.currentTimeMillis())
                    )
                    if (weightData.isEmpty()) {
                        database.weightDao().insert(Weights(null, weight, Date()))
                    } else {
                        weightData[0].weight
                        weightData[0].updateTime = Date()
                        database.weightDao().updateWeight(weightData[0])
                    }
                }

            })
        bmiDialog.show()
    }

    private fun getUnitType(): String {
        return if (SharedPreferenceUtils.unit) KG else LB
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHealthBinding {
        return FragmentHealthBinding.inflate(inflater, container, false)
    }
}