package com.example.quanpham.fragment

import DateUtils.getStartOfDay
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.quanpham.activity.SplashActivity
import com.example.quanpham.adapter.RankAdapter
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentRankBinding
import com.example.quanpham.db.model.Rank
import com.example.quanpham.services.ResetStepForegroundService
import com.example.quanpham.utility.Constant
import com.example.quanpham.utility.logD
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
        fetchData()
        binding.layoutRefresh.setOnRefreshListener {
            fetchData()
        }

    }
    fun fetchData(){
        var intent = Intent(requireActivity(), ResetStepForegroundService::class.java)
        ContextCompat.startForegroundService(requireContext(), intent)
        var count = 0
        val listRank : ArrayList<Rank> = arrayListOf()
        var rankAdapter = RankAdapter(requireContext())
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
        binding.layoutRefresh.isRefreshing = false
    }
}