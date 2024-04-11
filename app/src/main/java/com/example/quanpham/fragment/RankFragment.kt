package com.example.quanpham.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quanpham.adapter.RankAdapter
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentRankBinding
import com.example.quanpham.db.model.Rank
import com.example.quanpham.db.model.StepsFirebase
import com.example.quanpham.model.Users
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.getStartOfDay
import com.example.quanpham.utility.logD
import com.example.quanpham.utility.showToast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class RankFragment: BaseFragment<FragmentRankBinding>() {
    companion object{
        fun instance() : RankFragment{
            return newInstance(RankFragment::class.java)
        }
    }
    val listRank : MutableList<Rank> = arrayListOf()
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRankBinding {
        return FragmentRankBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        var count = 0
        var rankAdapter = RankAdapter()
        fbDatabase.getReference(Constant.KEY_RANK).child(getStartOfDay(System.currentTimeMillis()).toString()).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (snap in snapshot.children){
                        val data = snap.getValue(Rank::class.java)
                        data.let {
                            it?.let { it1 -> if(it1.steps > 0) listRank.add(it1) }
                        }
                        count++
                        if(count == snapshot.childrenCount.toInt()){
                            listRank.sortByDescending { it.steps }
                            rankAdapter.setItems(listRank)
                            binding.rcvRank.adapter = rankAdapter
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}