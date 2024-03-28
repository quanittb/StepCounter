package com.mobiai.app.ui.dialog


import android.content.Context
import com.example.quanpham.databinding.DialogPermissionBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class PermissionDialog(context : Context, callback :  () -> Unit) : BottomSheetDialog(context) {
    val binding = DialogPermissionBinding.inflate(layoutInflater)

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