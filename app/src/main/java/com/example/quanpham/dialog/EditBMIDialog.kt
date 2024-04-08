package com.mobiai.app.ui.dialog

import android.content.Context
import android.text.Editable
import android.widget.Toast
import com.example.quanpham.R
import com.example.quanpham.base.BaseDialog
import com.example.quanpham.databinding.DialogEditBmiBinding
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.utility.formatNumbers
import org.checkerframework.checker.units.qual.min
import java.text.DecimalFormat
import javax.microedition.khronos.opengles.GL

class EditBMIDialog(context: Context, private val onSaveChangeBmiListener: OnSaveChangeBmiListener) :
    BaseDialog(context, R.style.Theme_Dialog) {
    private val maxWeightInKg = 300
    private val minWeightInKg = 15
    private val maxHeightInCm = 250
    private val maxWeightInLb = 661
    private val minWeightInLb = 33
    private var lbToKg : Float = 0.45359237F
    private var cmToIn : Float = 0.393701f
    private var inToCm : Float = 2.54f
    private val US_UNIT = "US_UNIT"
    private val GLOBAL_UNIT = "GLOBAL_UNIT"
    private var unitType = GLOBAL_UNIT

    val binding = DialogEditBmiBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)

        unitType = if (SharedPreferenceUtils.unit) {
            GLOBAL_UNIT
        } else {
            US_UNIT
        }

        updateUnit()

        binding.run {

            tvCancel.setOnClickListener {
                onSaveChangeBmiListener.onNotSave()
                dismiss()
            }

            tvOk.setOnClickListener {
                val weight = binding.edtWeight.text.toString().trim()
                if (weight.isEmpty() || !isValidWeight(weight)) {
                    showAlert(context.getString(R.string.invalid_weight))
                    return@setOnClickListener
                }
                val height = binding.edtHeight.text.toString().trim()
                if (height.isEmpty() || !isValidHeight(height)) {
                    showAlert(context.getString(R.string.invalid_height))
                    return@setOnClickListener
                }

                val bmi = calculateBMIScore(weight.toFloat(), height.toFloat())
                onSaveChangeBmiListener.onChangeWeight(weight.toFloat())
                onSaveChangeBmiListener.onSave(bmi)
                SharedPreferenceUtils.unit = unitType == GLOBAL_UNIT
                dismiss()
            }

            tvKg.setOnClickListener {
                if (unitType == GLOBAL_UNIT) return@setOnClickListener
                unitType = GLOBAL_UNIT
                updateUnit()
                val currentWeightText = binding.edtWeight.text.toString().trim()
                if (currentWeightText.isNotEmpty()) {
                    binding.edtWeight.text = Editable.Factory.getInstance()
                        .newEditable(formatNumbers(getWeightInUnit(currentWeightText.toFloat(), unitType)))
                    binding.edtWeight.setSelection(binding.edtWeight.text.toString().length)

                }
                val currentHeightText = binding.edtHeight.text.toString().trim()
                if (currentHeightText.isNotEmpty()) {
                    binding.edtHeight.text = Editable.Factory.getInstance()
                        .newEditable(formatNumbers(getHeightInUnit(currentHeightText.toFloat(), unitType)))
                    binding.edtHeight.setSelection(binding.edtHeight.text.toString().length)

                }

            }

            tvLb.setOnClickListener {
                if (unitType == US_UNIT) return@setOnClickListener
                unitType = US_UNIT
                updateUnit()
                val currentHeightText = binding.edtHeight.text.toString().trim()
                if (currentHeightText.isNotEmpty()) {
                    binding.edtHeight.text = Editable.Factory.getInstance()
                        .newEditable(formatNumbers(getHeightInUnit(currentHeightText.toFloat(), unitType)))
                    binding.edtHeight.setSelection(binding.edtHeight.text.toString().length)
                }
                val currentWeightText = binding.edtWeight.text.toString().trim()
                if (currentWeightText.isNotEmpty()) {
                    binding.edtWeight.text = Editable.Factory.getInstance()
                        .newEditable(formatNumbers(getWeightInUnit(currentWeightText.toFloat(), unitType)))
                    binding.edtWeight.setSelection(binding.edtWeight.text.toString().length)

                }
            }

            tvCm.setOnClickListener {
                if (unitType == GLOBAL_UNIT) return@setOnClickListener
                unitType = GLOBAL_UNIT
                updateUnit()
                val currentWeightText = binding.edtWeight.text.toString().trim()
                if (currentWeightText.isNotEmpty()) {
                    binding.edtWeight.text = Editable.Factory.getInstance()
                        .newEditable(formatNumbers(getWeightInUnit(currentWeightText.toFloat(), unitType)))
                    binding.edtWeight.setSelection(binding.edtWeight.text.toString().length)

                }
                val currentHeightText = binding.edtHeight.text.toString().trim()
                if (currentHeightText.isNotEmpty()) {
                    binding.edtHeight.text = Editable.Factory.getInstance()
                        .newEditable(formatNumbers(getHeightInUnit(currentHeightText.toFloat(), unitType)))
                    binding.edtHeight.setSelection(binding.edtHeight.text.toString().length)
                }
            }

            tvInch.setOnClickListener {
                if (unitType == US_UNIT) return@setOnClickListener
                unitType = US_UNIT
                updateUnit()
                val currentHeightText = binding.edtHeight.text.toString().trim()
                if (currentHeightText.isNotEmpty()) {
                    binding.edtHeight.text = Editable.Factory.getInstance()
                        .newEditable(formatNumbers(getHeightInUnit(currentHeightText.toFloat(), unitType)))
                    binding.edtHeight.setSelection(binding.edtHeight.text.toString().length)

                }
                val currentWeightText = binding.edtWeight.text.toString().trim()
                if (currentWeightText.isNotEmpty()) {
                    binding.edtWeight.text = Editable.Factory.getInstance()
                        .newEditable(formatNumbers(getWeightInUnit(currentWeightText.toFloat(), unitType)))
                    binding.edtWeight.setSelection(binding.edtWeight.text.toString().length)

                }
            }
        }

    }


    private fun isValidWeight(weight: String): Boolean {
        val weightToDouble = weight.toDouble()
        return when (unitType) {
            GLOBAL_UNIT -> {
                weightToDouble <= maxWeightInKg
                weightToDouble >= minWeightInKg
            }

            US_UNIT -> {
                weightToDouble <= maxWeightInLb
                weightToDouble  >= minWeightInLb
            }

            else -> {
                false
            }

        }
    }

    private fun isValidHeight(height: String): Boolean {
        val heightToDouble = height.toDouble()
        return when (unitType) {
            GLOBAL_UNIT -> {
                heightToDouble <= maxHeightInCm
            }

            US_UNIT -> {
                heightToDouble * inToCm <= maxHeightInCm
            }

            else -> {
                false
            }

        }
    }

    private fun updateUnit() {
        binding.run {
            if (unitType == GLOBAL_UNIT) {
                tvKg.isSelected = true
                tvLb.isSelected = false
                tvCm.isSelected = true
                tvInch.isSelected = false
            } else {
                tvKg.isSelected = false
                tvLb.isSelected = true
                tvInch.isSelected = true
                tvCm.isSelected = false
            }
        }
    }

    private fun getWeightInUnit(weight: Float, unitToChange: String): Float {
        return when (unitToChange) {
            GLOBAL_UNIT -> {
                weight * lbToKg
            }

            US_UNIT -> {
                weight / lbToKg
            }

            else -> {
                weight
            }

        }
    }

    private fun getHeightInUnit(height: Float, unitToChange: String): Float {
        return when (unitToChange) {
            GLOBAL_UNIT -> {
                height * inToCm
            }

            US_UNIT -> {
                height / cmToIn
            }

            else -> {
                height
            }

        }
    }

    private fun showAlert(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun calculateBMIScore(weight: Float, height: Float): String {
        val weightInKg = if (unitType == GLOBAL_UNIT) weight else weight *  lbToKg
        val heightInMeters = if (unitType == GLOBAL_UNIT) height / 100 else (height * inToCm) / 100
        return formatNumbers(weightInKg / (heightInMeters * heightInMeters))
    }

    interface OnSaveChangeBmiListener {
        fun onSave(bmi : String)
        fun onNotSave()
        fun onChangeWeight(weight: Float)
    }
}