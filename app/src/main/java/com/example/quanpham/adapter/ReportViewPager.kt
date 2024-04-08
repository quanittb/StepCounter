package com.mobiai.app.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.quanpham.fragment.ReportItemTabFragment

class ReportViewPager(owner : Fragment, val numberPage : Int, var timeType  : String) : FragmentStateAdapter(owner) {
    override fun getItemCount(): Int {
        return numberPage
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {

            0 -> ReportItemTabFragment.instance(timeType,ReportItemTabFragment.STEP)
            1 -> ReportItemTabFragment.instance(timeType,ReportItemTabFragment.CALORIE)
            2 -> ReportItemTabFragment.instance(timeType,ReportItemTabFragment.TIME)
            3 -> ReportItemTabFragment.instance(timeType,ReportItemTabFragment.DISTANCE)

            else -> ReportItemTabFragment.instance(timeType,ReportItemTabFragment.STEP)
        }

    }


}