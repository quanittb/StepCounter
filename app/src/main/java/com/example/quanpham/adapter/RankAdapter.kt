package com.example.quanpham.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.quanpham.R
import com.example.quanpham.base.BaseAdapter
import com.example.quanpham.databinding.ItemRankBinding
import com.example.quanpham.db.model.Rank
import com.example.quanpham.utility.makeInvisible
import com.example.quanpham.utility.makeVisible

class RankAdapter : BaseAdapter<Rank,ItemRankBinding>() {
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
            else -> binding.ivRank.makeInvisible()
        }
    }
}