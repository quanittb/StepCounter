package com.example.quanpham.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import com.example.quanpham.R
import com.example.quanpham.base.BaseAdapter
import com.example.quanpham.databinding.ItemLanguageBinding
import com.example.quanpham.language.Language


class LanguageAdapter(val context : Context, val listener : OnLanguageClickListener) : BaseAdapter<Language, ItemLanguageBinding>() {

    override fun bind(binding: ItemLanguageBinding, item: Language, position: Int) {
        binding.txtNameLanguage.text = item.title
        binding.imgIconLanguage.setImageDrawable(AppCompatResources.getDrawable(context, item.flag))
        if(item.isChoose){
            binding.imgChooseLanguage.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_select_language))
        }else{
            binding.imgChooseLanguage.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_un_select_lang))
        }
        binding.root.setOnClickListener {
            listener.onClickItemListener(item)
            for (i in listItem.indices) {
                listItem[i].isChoose = i == listItem.indexOf(item)
            }
            notifyDataSetChanged()
        }
    }


    interface OnLanguageClickListener {
        fun onClickItemListener(language: Language?)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ItemLanguageBinding {
        return ItemLanguageBinding.inflate(inflater,parent,false)
    }
}