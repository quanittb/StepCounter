package com.example.quanpham.dialog

import DateUtils
import android.content.Context
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.example.quanpham.R
import com.example.quanpham.base.BaseDialog
import com.example.quanpham.databinding.DialogAddWeightBinding
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.utility.formatNumbers
import com.example.quanpham.utility.rxbus.ChangeUnit
import com.example.quanpham.utility.rxbus.RxBus
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener
import java.util.Calendar
import java.util.Date


class AddWeightDialog(
    context: Context,
    private val onSaveChangeWeightListener: OnSaveChangeWeightListener
) :
    BaseDialog(context, R.style.Theme_Dialog) {
    private val TAG = AddWeightDialog::javaClass.name
    private val maxWeightInKg = 300
    private val minWeightInKg = 15
    private val maxWeightInLb = 661
    private val minWeightInLb = 33
    private var lbToKg : Float = 0.45359237f
    private val KG = "KG"
    private val LB = "LB"
    private var weightUnit = KG
    private var rsDate: Date? = null

    val binding = DialogAddWeightBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)

        weightUnit = if (SharedPreferenceUtils.unit) KG else LB
        updateWeighUnit()
        binding.run {
            dayDatePicker.apply {
                this.setStartDate(1, DateUtils.getCurrentMonth(), DateUtils.getCurrentYear() )
                this.setEndDate(DateUtils.getCurrentDay(), DateUtils.getCurrentMonth(), DateUtils.getCurrentYear() )

            }
            dayDatePicker.getSelectedDate(OnDateSelectedListener { date ->
                date?.let {
                    Log.d(TAG, "getSelectedDate1: $it")
                    rsDate = it
                }

            })

            tvCancel.setOnClickListener {
                onSaveChangeWeightListener.onNotSave()
                dismiss()
            }

            tvOk.setOnClickListener {
                val weight = binding.edtWeight.text.toString().trim()

                if (weight.isNotEmpty() && isValidWeight(weight)) {
                    val weightFormated =
                        if (weightUnit == LB)
                            getWeightInUnit(weight.toFloat(),  KG)
                        else weight.toFloat()

                    if (rsDate != null) {
                        val calendar: Calendar = Calendar.getInstance()
                        calendar.time = rsDate!!
                        SharedPreferenceUtils.unit = weightUnit == KG
                        onSaveChangeWeightListener.onSave(weightFormated, calendar)
                        RxBus.publish(ChangeUnit())
                        dismiss()
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.invalid_date), Toast.LENGTH_SHORT
                        ).show()
                    }
                } else{
                    showAlert(context.getString(R.string.invalid_weight))
                    return@setOnClickListener
                 }
            }

            tvKg.setOnClickListener {
                if (weightUnit == KG) return@setOnClickListener
                weightUnit = KG
                updateWeighUnit()
                val currentWeightText = binding.edtWeight.text.toString().trim()
                if (currentWeightText.isNotEmpty()) {
                    binding.edtWeight.text = Editable.Factory.getInstance()
                        .newEditable(formatNumbers(getWeightInUnit(currentWeightText.toFloat(), KG)))
                    binding.edtWeight.setSelection(binding.edtWeight.text.toString().length)
                }
            }

            tvLb.setOnClickListener {
                if (weightUnit == LB) return@setOnClickListener
                weightUnit = LB
                updateWeighUnit()
                val currentWeightText = binding.edtWeight.text.toString().trim()
                if (currentWeightText.isNotEmpty()) {
                    binding.edtWeight.text = Editable.Factory.getInstance()
                        .newEditable(formatNumbers(getWeightInUnit(currentWeightText.toFloat(), LB)))
                }
            }

        }

    }

    private fun isValidWeight(weight: String): Boolean {
        val weightToDouble = weight.toDouble()
        return when (weightUnit) {
            KG -> {
                weightToDouble in 15.0..300.0
            }

            LB -> {
                weightToDouble in 33.0..661.0
            }

            else -> {
                false
            }

        }
    }

    private fun updateWeighUnit() {
        binding.run {
            if (weightUnit == KG) {
                tvKg.isSelected = true
                tvLb.isSelected = false
            } else {
                tvKg.isSelected = false
                tvLb.isSelected = true
            }
        }
    }

    private fun getWeightInUnit(weight: Float, unitToChange: String): Float {
        return when (unitToChange) {
            KG -> {
                weight * lbToKg
            }

            LB -> {
                weight / lbToKg
            }

            else -> {
                weight
            }

        }
    }
    private fun showAlert(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }



    interface OnSaveChangeWeightListener {
        fun onSave(weight: Float, calendar: Calendar)
        fun onNotSave()
    }
}