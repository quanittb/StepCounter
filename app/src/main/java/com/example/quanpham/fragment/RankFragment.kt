package com.example.quanpham.fragment

import DateUtils.getStartOfDay
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quanpham.adapter.RankAdapter
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentRankBinding
import com.example.quanpham.db.model.Rank
import com.example.quanpham.utility.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RankFragment: BaseFragment<FragmentRankBinding>() {
    companion object{
        fun instance() : RankFragment{
            return newInstance(RankFragment::class.java)
        }
    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRankBinding {
        return FragmentRankBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        var count = 0
        val listRank : ArrayList<Rank> = arrayListOf()
        var rankAdapter = RankAdapter()
        var isChecked = true
        fbDatabase.getReference(Constant.KEY_RANK).child(getStartOfDay(System.currentTimeMillis()).toString()).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val data = snap.getValue(Rank::class.java)
                        data.let {
                            it?.let { it1 -> if (it1.steps > 0) listRank.add(it1) }
                        }
                        count++
                        if (count == snapshot.childrenCount.toInt() && isChecked) {
                            listRank.sortByDescending { it.steps }
                            rankAdapter.setItems(listRank)
                            binding.rcvRank.adapter = rankAdapter
                            isChecked = false
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}