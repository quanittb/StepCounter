package com.example.quanpham.fragment

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.quanpham.R
import com.example.quanpham.activity.MainActivity
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentReportBinding
import com.example.quanpham.dialog.ReportDropDownMenu
import com.example.quanpham.utility.formatNumbers
import com.example.quanpham.utility.rxbus.UpdateAvgValue
import com.example.quanpham.utility.rxbus.listenEvent
import com.google.android.material.tabs.TabLayoutMediator
import com.mobiai.app.adapter.ReportAdapter
import com.mobiai.app.adapter.ReportViewPager

class ReportFragment : BaseFragment<FragmentReportBinding>() {
    private var viewPagerAdapter: ReportViewPager? = null
    private var currentSelectedTimeType: String = ReportDropDownMenu.WEEK
    private var reportAdapter: ReportAdapter? = null

    companion object {
        fun instance(): ReportFragment {
            return newInstance(ReportFragment::class.java)
        }
    }


    override fun initView() {
        initViewPager()
        binding.tvDropdownMenu.setOnClickListener {
            ReportDropDownMenu.showDropDown(
                requireActivity() as MainActivity, binding.tvDropdownMenu,
                currentSelectedTimeType,
                ) {
                handleDropDownMenuClick(it)
            }
        }
        handleEvent()

    }

    private fun handleEvent() {
        addDispose(listenEvent({
            when (it) {

                is UpdateAvgValue -> {
                  if (isAdded){
                      binding.tvAverage.text = if (it.typeValue == ReportItemTabFragment.WEEK || it.typeValue == ReportItemTabFragment.MONTH) getString(R.string.the_average_value)
                      else getString(R.string.total_value)
                      val avgSteps = if (it.avgSteps > 0) it.avgSteps else 0f
                      val avgCalories = if (it.avgCalories > 0) it.avgCalories else 0f
                      val avgDistance = if (it.avgDistance > 0) it.avgDistance else 0f
                      val avgHour = if (it.avgHour > 0) it.avgHour else 0f
                      updateIndex(avgSteps,avgDistance,avgCalories,avgHour)
                  }
                }
            }
        }, {}))
    }

    private fun updateIndex(totalStep: Float, totalDistance: Float, totalCalories: Float, totalHour: Float) {
        binding.step.text = formatNumbers(totalStep)
        binding.distance.text = formatNumbers(totalDistance)
        binding.calorie.text = formatNumbers(totalCalories)
        binding.time.text = formatNumbers(totalHour)
    }

    //xu ly khi click vao tung tab thoi gian
    private fun handleDropDownMenuClick(type: String) {
        currentSelectedTimeType = type
        initViewPager()
        when (type) {
            ReportDropDownMenu.WEEK -> {
                binding.tvDropdownMenu.text = requireContext().getString(R.string.week)
            }

            ReportDropDownMenu.MONTH -> {
                binding.tvDropdownMenu.text = requireContext().getString(R.string.month)
            }

            ReportDropDownMenu.DAY -> {
                binding.tvDropdownMenu.text = requireContext().getString(R.string.day)

            }
        }
    }

//    private fun setPage(){
//        when (MainActivity.nameReport) {
//            DailyActivity.KEY_CLICK_NOTY_DAILY_DISTANCE -> {
//                binding.run {
//                    viewPager.setCurrentItem(3, false)
//                }
//            }
//            DailyActivity.KEY_CLICK_NOTY_DAILY_FIRE -> {
//                binding.run {
//                    viewPager.setCurrentItem(1, false)
//                }
//            }
//            DailyActivity.KEY_CLICK_NOTY_DAILY_TIME -> {
//                binding.run {
//                    viewPager.setCurrentItem(2, false)
//                }
//            }
//            else -> {
//                binding.run {
//                    viewPager.setCurrentItem(0, false)
//                }
//            }
//        }
//    }
    private fun initViewPager() {
        val listTitle: ArrayList<String> = arrayListOf(
            getString(R.string.steps),
            getString(R.string.calories),
            getString(R.string.time),
            getString(R.string.distance)
        )
        viewPagerAdapter = ReportViewPager(this, listTitle.size, timeType = currentSelectedTimeType)
        binding.run {
            viewPager.adapter = viewPagerAdapter
            viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            //viewPager.setCurrentItem(0, false)
//            setPage()
            TabLayoutMediator(tabLayout, viewPager) { tab, postition ->
                tab.text = listTitle[postition]
                }.attach()
                viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                    }
                })
                viewPager.isUserInputEnabled = false
            }
    }
    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentReportBinding {
        return FragmentReportBinding.inflate(inflater, container,false)
    }

}