package com.mobiai.app.ui.dialog

import android.content.Context
import com.example.quanpham.databinding.DialogRequirePermissionBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
class PermissionRequiredDialog(context : Context, callback :  () -> Unit) : BottomSheetDialog(context) {
    val binding = DialogRequirePermissionBinding.inflate(layoutInflater)

    init {
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        setCancelable(false)

//        binding.tvCancel.setOnClickListener {
//            dismiss()
//        }

        binding.btnGrant.setOnClickListener {
            callback()
            dismiss()
        }
    }

}