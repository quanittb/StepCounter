package com.example.quanpham.fragment

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quanpham.R
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentStepLenghtBinding
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.utility.rxbus.RxBus
import com.example.quanpham.utility.rxbus.StepLengthUpdate
import com.mobiai.app.ui.dialog.HeightDialog

class StepLengthFragment : BaseFragment<FragmentStepLenghtBinding>() {

    companion object {
        fun instance(): StepLengthFragment {
            return newInstance(StepLengthFragment::class.java)
        }
    }


    private var bottomSheetHeightDialog: HeightDialog? = null
    override fun initView() {
        checkAutoToggle()

        if (SharedPreferenceUtils.unit)
            binding.txtStepLength.text = String.format("%.2f",SharedPreferenceUtils.stepLength).replace(",",".")
        else
            binding.txtStepLength.text = String.format("%.2f",SharedPreferenceUtils.stepLength/30.48.toFloat()).replace(",",".")

        binding.btnMinus.setOnClickListener {
            SharedPreferenceUtils.autoCalculateLength = false
            checkAutoToggle()
            if (binding.txtStepLength.text.toString().toFloat() > 1f)
                binding.txtStepLength.text =
                    String.format("%.2f",binding.txtStepLength.text.toString().toFloat() - 1).replace(",",".")
        }
        binding.btnPlus.setOnClickListener {
            SharedPreferenceUtils.autoCalculateLength = false
            checkAutoToggle()
            binding.txtStepLength.text =
                String.format("%.2f",binding.txtStepLength.text.toString().toFloat() + 1).replace(",",".")

        }
        binding.toggleAutoCalculate.setOnClickListener {
            SharedPreferenceUtils.autoCalculateLength = !SharedPreferenceUtils.autoCalculateLength
            checkAutoToggle()

        }
        binding.btnSave.setOnClickListener {
            val floatInput = binding.txtStepLength.text.toString().replace(",",".")
            if (SharedPreferenceUtils.unit )
                SharedPreferenceUtils.stepLength = binding.txtStepLength.text.toString().replace(",",".").toFloat()
            else if (!SharedPreferenceUtils.unit && String.format("%.2f",binding.txtStepLength.text.toString().toFloat()).replace(",",".").toFloat() > 0f  )
                SharedPreferenceUtils.stepLength =
                    binding.txtStepLength.text.toString().toFloat() * 30.48.toFloat()
            RxBus.publish(StepLengthUpdate(binding.txtStepLength.text.toString()))
                handlerBackPressed()
        }
        binding.btnClose.setOnClickListener {
            handlerBackPressed()

        }
        updateData()
        binding.textHeight.setOnClickListener {
            openHeightBottomSheet()
        }
    }

    fun updateData() {
        val textToAppend: String?
        if (SharedPreferenceUtils.unit)
            textToAppend = "${SharedPreferenceUtils.height} ${resources.getText(R.string.cm)}"
        else if (SharedPreferenceUtils.height > 0f)
            textToAppend = "${
                String.format("%.2f",SharedPreferenceUtils.height / 30.48.toFloat())} ${resources.getText(R.string.ft)}"
        else textToAppend = "${String.format("%.2f",SharedPreferenceUtils.height)} ${resources.getText(R.string.ft)}"

        val spannableStringBuilder =
            SpannableStringBuilder(requireContext().resources.getText(R.string.automatically_calculate_on_your_height))
        val span = StyleSpan(Typeface.BOLD)
        val underlineSpan = UnderlineSpan()
        val colorSpan = ForegroundColorSpan(resources.getColor(R.color.highlight_color))
        val spannableString = SpannableString(textToAppend)
        spannableString.setSpan(span, 0, textToAppend.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(
            underlineSpan,
            0,
            textToAppend.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            colorSpan,
            0,
            textToAppend.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableStringBuilder.append(spannableString)
        binding.textHeight.text = spannableStringBuilder
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStepLenghtBinding {
        return FragmentStepLenghtBinding.inflate(inflater, container, false)
    }

    fun checkAutoToggle() {
        if (SharedPreferenceUtils.autoCalculateLength){
            binding.toggleAutoCalculate.setImageDrawable(resources.getDrawable(R.drawable.toogle_on_reminder_svg))
            if (SharedPreferenceUtils.unit)
                binding.txtStepLength.text = String.format("%.2f",(SharedPreferenceUtils.height * 0.4).toFloat()).replace(",",".")
            else
                binding.txtStepLength.text = String.format("%.2f",(SharedPreferenceUtils.height * 0.4 / 30.48).toFloat()).replace(",",".")
        }
        else
            binding.toggleAutoCalculate.setImageDrawable(resources.getDrawable(R.drawable.toogle_off_reminder))

    }

    private fun openHeightBottomSheet() {
        Log.d("abcde","${SharedPreferenceUtils.height} va ${SharedPreferenceUtils.height0_temporary}")
        bottomSheetHeightDialog == null
        if (bottomSheetHeightDialog == null) {
            bottomSheetHeightDialog = HeightDialog(
                requireContext(),
                object : HeightDialog.OnClickBottomSheetListener {
                    override fun onClickSaveFrom() {
                        if (SharedPreferenceUtils.unit)
                        {
                            if(SharedPreferenceUtils.autoCalculateLength)
                                binding.txtStepLength.text = String.format("%.2f",SharedPreferenceUtils.height * 0.4).replace(",",".")
                            else
                                binding.txtStepLength.text = String.format("%.2f",SharedPreferenceUtils.stepLength).replace(",",".")
                        }
                        else{
                            if(SharedPreferenceUtils.autoCalculateLength)
                                binding.txtStepLength.text =
                                    String.format("%.2f",SharedPreferenceUtils.height / 30.48 *0.4.toFloat()).replace(",",".")
                            else
                                binding.txtStepLength.text =
                                    String.format("%.2f",SharedPreferenceUtils.stepLength / 30.48.toFloat()).replace(",",".")

                        }

                        updateData()
                    }
                })
        }

        bottomSheetHeightDialog?.checkShowBottomSheet()
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        hideFragment(this@StepLengthFragment)
    }
}