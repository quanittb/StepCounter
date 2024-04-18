package com.example.quanpham.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.quanpham.R
import com.example.quanpham.base.BaseAdapter
import com.example.quanpham.databinding.ItemRankBinding
import com.example.quanpham.db.model.Rank
import com.example.quanpham.utility.makeGone
import com.example.quanpham.utility.makeInvisible
import com.example.quanpham.utility.makeVisible
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RankAdapter(var context : Context) : BaseAdapter<Rank,ItemRankBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ItemRankBinding {
        return ItemRankBinding.inflate(inflater,parent,false)
    }

    override fun bind(binding: ItemRankBinding, item: Rank, position: Int) {
        binding.tvName.text = item.name
        binding.tvStep.text = item.steps.toString()
        if(item.id == Firebase.auth.currentUser!!.uid ){
            binding.tvName.setTextColor(ContextCompat.getColor(context,R.color.primary_color))
        }
        when (position){
             0 -> {
                 binding.ivRank.makeVisible()
                 binding.ivRank.setImageResource(R.drawable.rank_one)
             }
             1 -> {
                 binding.ivRank.setImageResource(R.drawable.rank_two)
                 binding.ivRank.makeVisible()
             }

             2 -> {
                 binding.ivRank.makeVisible()
                 binding.ivRank.setImageResource(R.drawable.rank_three)
             }
            else -> {
                binding.ivRank.makeGone()
                binding.tvRank.makeVisible()
                binding.tvRank.text = "${position +1}"
                binding.root.setBackgroundResource(R.drawable.bg_item_rank_black)
            }
        }
    }
}