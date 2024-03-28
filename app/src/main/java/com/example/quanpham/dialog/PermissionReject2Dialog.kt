package com.mobiai.app.ui.dialog

import android.content.Context
import com.example.quanpham.databinding.DialogPermissionReject2Binding
import com.google.android.material.bottomsheet.BottomSheetDialog

class PermissionReject2Dialog(context : Context, callback :  () -> Unit) : BottomSheetDialog(context) {
    val binding = DialogPermissionReject2Binding.inflate(layoutInflater)

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