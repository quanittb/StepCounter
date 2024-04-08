package com.mobiai.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quanpham.base.BaseAdapter
import com.example.quanpham.databinding.ItemReportBinding
import com.example.quanpham.db.model.Report
import com.example.quanpham.utility.formatNumbers

class ReportAdapter(val context : Context) : BaseAdapter<Report, ItemReportBinding>() {

    override fun bind(binding: ItemReportBinding, item: Report, position: Int) {
        binding.image.setImageResource(item.imageId)
        binding.index.text = formatNumbers(item.indexNumber.toFloat())
        binding.unit.text = context.getString(item.unitStringId)
        binding.increaseIndex.text = "+${item.increaseIndex}"
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ItemReportBinding {
        return ItemReportBinding.inflate(inflater, parent, false)
    }


}