package com.example.quanpham.fragment

import  DateUtils
import DateUtils.findDayOfWeek
import DateUtils.findMonthAndDayOfMonth
import DateUtils.getCurrentYear
import DateUtils.getMonthInStringIdSplit3
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.quanpham.R
import com.example.quanpham.base.BaseFragment
import com.example.quanpham.databinding.FragmentReportItemTabBinding
import com.example.quanpham.db.model.Steps
import com.example.quanpham.fragment.HomeFragment.Companion.currentStep
import com.example.quanpham.lib.SharedPreferenceUtils
import com.example.quanpham.model.PairModel
import com.example.quanpham.utility.Constant.cmToIn
import com.example.quanpham.utility.convertSecondToTime
import com.example.quanpham.utility.getDayOfYear
import com.example.quanpham.utility.getEndOfDay
import com.example.quanpham.utility.getEndOfDayMinus
import com.example.quanpham.utility.getStartDayOfYear
import com.example.quanpham.utility.getStartOfDay
import com.example.quanpham.utility.getStartOfDayMinus
import com.example.quanpham.utility.logD
import com.example.quanpham.utility.rxbus.ChangeUnit
import com.example.quanpham.utility.rxbus.listenEvent
import com.mobiai.base.chart.ColumnChart.Column
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class ReportItemTabFragment : BaseFragment<FragmentReportItemTabBinding>() {
    private var currentWeekIndex: Int = -1
    private val TAG: String = ReportItemTabFragment::javaClass.name
    private var reportType = STEP
    private var timeType = WEEK
    private var currentMonthIndex = -1
    private val currentWeek: MutableList<Int> = mutableListOf()
    private var totalDay: Int = 365
    private var currentDayIndex = 0
    private var currentMonth: PairModel = PairModel(-1, -1)
    private val listWeek: MutableList<MutableList<Int>> = mutableListOf()
    private val listMonth: MutableList<PairModel> = mutableListOf()
    companion object {
        const val CALORIE = 1
        const val DISTANCE = 2
        const val STEP = 3
        const val TIME = 4

        const val WEEK = "WEEK"
        const val MONTH = "MONTH"
        const val DAY = "DAY"
        const val TIME_TYPE = "TIME_TYPE"
        const val REPORT_TYPE = "REPORT_TYPE"
        fun instance(timeType: String = DAY, reportType: Int = STEP): ReportItemTabFragment {
            val bundle = Bundle().apply {
                putString(TIME_TYPE, timeType)
                putInt(REPORT_TYPE, reportType)
            }
            return newInstance(ReportItemTabFragment::class.java, bundle)
        }
    }

    override fun initView() {
        arguments?.let {
            if (it.getString(TIME_TYPE) != null) {
                timeType = it.getString(TIME_TYPE)!!
            }
            reportType = it.getInt(REPORT_TYPE)
        }
        initData(timeType)
        handlerRx()

        currentStep.observe(this){
            binding.run {
                initData(timeType)
            }
        }
    }

    private fun handlerRx() {
        addDispose(listenEvent({
            when(it){
                is ChangeUnit ->{
                    initData(timeType)
                }
            }
        }
        ))
    }

    private fun initData(timeType: String) {
        when (timeType) {
            WEEK -> {
                listWeek.addAll(DateUtils.generateWeeksInYear(getCurrentYear()))
                setTimePickerBehaviour(timeType)
            }

            MONTH -> {
                listMonth.addAll(DateUtils.getStartDayAndEndDayOfMonth(getCurrentYear()))
                setTimePickerBehaviour(timeType)

            }

            DAY -> {
                totalDay = if (DateUtils.isLeapYear(getCurrentYear())) 366 else 365
                setTimePickerBehaviour(timeType)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTimePickerBehaviour(timeType: String) {

        when (timeType) {
            DAY -> {
                val day = DateUtils.getCurrentDay()
                val month = DateUtils.getCurrentMonth()
                binding.tvTime.text =
                    "${DateUtils.getMonthInStringText(requireContext(), month)} $day"

                updateDataToDayChart(currentDayIndex)

                binding.run {
                    ivPrevious.setOnClickListener {
                        currentDayIndex++
                        val calendar = Calendar.getInstance()
                        calendar.add(Calendar.DAY_OF_YEAR,-currentDayIndex)
                        binding.tvTime.text = "${
                            DateUtils.getMonthInStringText(
                                requireContext(),
                                calendar.get(Calendar.MONTH) +1
                            )
                        } ${calendar.get(Calendar.DAY_OF_MONTH)}"
                        updateDataToDayChart(currentDayIndex)
                    }
                    ivNext.setOnClickListener {
                        if (currentDayIndex == 0) return@setOnClickListener
                        currentDayIndex--
                        val calendar = Calendar.getInstance()
                        calendar.add(Calendar.DAY_OF_YEAR,-currentDayIndex)
                        binding.tvTime.text = "${
                            DateUtils.getMonthInStringText(
                                requireContext(),
                                calendar.get(Calendar.MONTH)+1
                            )
                        } ${calendar.get(Calendar.DAY_OF_MONTH)}"
                        updateDataToDayChart(currentDayIndex)
                    }

                }
            }
            WEEK -> {
                val dayOfYear = DateUtils.getDayOfYear()
                currentWeek.clear()
                // thêm ds ngày của tuần hiện tại
                getCurrentWeekInList(dayOfYear)?.let { currentWeek.addAll(ArrayList(it)) }
                if (currentWeek.isNotEmpty()) {
                    Log.i(TAG, "setTimePickerBehaviour: $currentWeek")
                    logD("day of year : ${currentWeek[0]}")
                    val startDay = findMonthAndDayOfMonth(
                        currentWeek[0],
                        getCurrentYear()
                    )
                    logD("day of month : ${startDay.first} va ${startDay.second}")
                    val endDay = findMonthAndDayOfMonth(
                        currentWeek[currentWeek.size - 1],
                        getCurrentYear()
                    )
                    val startString = "${getString(getMonthInStringIdSplit3(startDay.second))} ${startDay.first}"
                    val endString = "${getString(getMonthInStringIdSplit3(endDay.second))} ${endDay.first}"
                    binding.tvTime.text = "${startString} -${endString} "
                    updateDataToWeekChart(currentWeek)
                }

                binding.run {
                    ivPrevious.setOnClickListener {
                        if (currentWeekIndex <= 0) return@setOnClickListener
                        currentWeekIndex--
                        currentWeek.clear()
                        currentWeek.addAll(listWeek[currentWeekIndex])
                        if (currentWeek.isNotEmpty()) {
                            val startDay = findMonthAndDayOfMonth(
                                currentWeek[0],
                                getCurrentYear()
                            )
                            val endDay = findMonthAndDayOfMonth(
                                currentWeek[currentWeek.size - 1],
                                getCurrentYear()
                            )
                            val startString = "${getString(getMonthInStringIdSplit3(startDay.second))} ${startDay.first}"
                            val endString = "${getString(getMonthInStringIdSplit3(endDay.second))} ${endDay.first}"
                            binding.tvTime.text = "${startString} -${endString} "
                            updateDataToWeekChart(currentWeek)
                        }
                    }

                    ivNext.setOnClickListener {
                        if (currentWeekIndex >= listWeek.size - 1) return@setOnClickListener
                        currentWeekIndex++
                        currentWeek.clear()
                        currentWeek.addAll(listWeek[currentWeekIndex])
                        if (currentWeek.isNotEmpty()) {
                            val startDay = findMonthAndDayOfMonth(
                                currentWeek[0],
                                getCurrentYear()
                            )
                            val endDay = findMonthAndDayOfMonth(
                                currentWeek[currentWeek.size - 1],
                                getCurrentYear()
                            )
                            val startString = "${getString(getMonthInStringIdSplit3(startDay.second))} ${startDay.first}"
                            val endString = "${getString(getMonthInStringIdSplit3(endDay.second))} ${endDay.first}"
                            binding.tvTime.text = "${startString} -${endString} "
                            updateDataToWeekChart(currentWeek)
                        }
                    }
                }
            }
        }


    }
    fun getHourFromDate(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.HOUR_OF_DAY) // Lấy giờ theo định dạng 24 giờ
    }
    @SuppressLint("NewApi")
    private fun updateDataToDayChart(minus: Int = 0){
        var listMonth = mutableListOf<Int>()
        val listRow: ArrayList<String> = arrayListOf()
        val listColum: ArrayList<Column> = arrayListOf()
        listColum.clear()
        listRow.clear()
        val listStepDay = database.stepDao().getRecordStepsDay(
            getStartOfDayMinus(System.currentTimeMillis(),minus),
            getEndOfDayMinus(System.currentTimeMillis(),minus)
        )
        if(listStepDay.isNotEmpty())
            listStepDay.sortedBy { it.startTime }.forEach {
                val hour = getHourFromDate(it.startTime)
                listRow.add("${hour}H")
                when (reportType) {
                    STEP -> listColum.add(
                        Column(
                            it.step.toDouble(),
                            "${it.step} ${requireContext().getString(R.string.steps)}",
                            "${hour}:00-${hour}:59"
                        )
                    )

                    CALORIE -> listColum.add(
                        Column(
                            it.calo.toDouble(),
                            "${it.calo} ${requireContext().getString(R.string.kcal)}",
                            "${hour}:00-${hour}:59"
                        )
                    )

                    DISTANCE -> {
                        if (SharedPreferenceUtils.unit)
                            listColum.add(
                                Column(
                                    it.distance.toDouble(),
                                    "${it.distance} ${requireContext().getString(R.string.km)}",
                                    "${hour}:00-${hour}:59"
                                )
                            )
                        else {
                            var distance = it.distance.toFloat() * 1000 * cmToIn
                            listColum.add(
                                Column(
                                    distance.toDouble(),
                                    "${distance} ${requireContext().getString(R.string.ft)}",
                                    "${hour}:00-${hour}:59"
                                )
                            )
                        }
                    }

                    TIME -> listColum.add(
                        Column(
                            it.activeTime.toDouble(),
                            convertSecondToTime(it.activeTime),
                            "${hour}:00-${hour}:59"
                        )
                    )
                }
            }
        val currentDate = LocalDate.now()
        var today = currentDate.dayOfYear
        binding.chartView.setColumnSelected(listMonth.indexOf(today))
        binding.chartView.setTargetValue(false, 80)
        binding.chartView.listHorizontal = listRow
        binding.chartView.setData(listColum)

    }

    @SuppressLint("NewApi")
    private fun updateDataToWeekChart(currentWeek: MutableList<Int>){
        // currentWeek chứa list ngày của tuần
        var listMonth = mutableListOf<Int>()
        val listRow: ArrayList<String> = arrayListOf()
        val listColum: ArrayList<Column> = arrayListOf()
        listColum.clear()
        listRow.clear()
        //list step cả tuần
        val listStepWeek = database.stepDao().getRecordStepsDay(
            getStartDayOfYear(currentWeek.first()),
            getStartDayOfYear(currentWeek.last())
        )
        val listFinalStepDayOfWeek : ArrayList<Steps> = arrayListOf()
        currentWeek.forEach{
            var step = Steps(startTime = Date())
            listStepWeek.forEach{item ->
                if(getDayOfYear(item.startTime) == it)
                {
                    step.step += item.step
                    step.calo += item.calo
                    step.distance += item.distance
                    step.activeTime += item.activeTime
                    step.startTime = item.startTime
                }
            }
            val day = findDayOfWeek(it, getCurrentYear())
            listRow.add(day)
            listFinalStepDayOfWeek.add(step)
        }
        listFinalStepDayOfWeek.sortedBy { it.startTime }.forEach {
            val date = findMonthAndDayOfMonth(getDayOfYear(it.startTime), getCurrentYear())
            when (reportType) {
                STEP -> listColum.add(
                    Column(
                        it.step.toDouble(),
                        "${it.step} ${requireContext().getString(R.string.steps)}",
                        "${
                            DateUtils.getMonthInStringText(
                                requireContext(),
                                date.second
                            )
                        } ${date.first}"
                    )
                )

                CALORIE -> listColum.add(
                    Column(
                        it.calo.toDouble(),
                        "${it.calo} ${requireContext().getString(R.string.kcal)}",
                        "${
                            DateUtils.getMonthInStringText(
                                requireContext(),
                                date.second
                            )
                        } ${date.first}"
                    )
                )

                DISTANCE -> {
                    if (SharedPreferenceUtils.unit)
                        listColum.add(
                            Column(
                                it.distance.toDouble(),
                                "${it.distance} ${requireContext().getString(R.string.km)}",
                                "${
                                    DateUtils.getMonthInStringText(
                                        requireContext(),
                                        date.second
                                    )
                                } ${date.first}"
                            )
                        )
                    else {
                        var distance = it.distance.toFloat() * 1000 * cmToIn
                        listColum.add(
                            Column(
                                distance.toDouble(),
                                "${distance} ${requireContext().getString(R.string.ft)}",
                                "${
                                    DateUtils.getMonthInStringText(
                                        requireContext(),
                                        date.second
                                    )
                                } ${date.first}"
                            )
                        )
                    }
                }

                TIME -> listColum.add(
                    Column(
                        it.activeTime.toDouble(),
                        convertSecondToTime(it.activeTime),
                        "${
                            DateUtils.getMonthInStringText(
                                requireContext(),
                                date.second
                            )
                        } ${date.first}"
                    )
                )
            }
        }
        val currentDate = LocalDate.now()
        var today = currentDate.dayOfYear
        binding.chartView.setColumnSelected(listMonth.indexOf(today))
        binding.chartView.setTargetValue(true, 250)
        binding.chartView.listHorizontal = listRow
        binding.chartView.setData(listColum)

    }
    private fun getCurrentWeekInList(dayOfYear: Int): MutableList<Int>? {
//        Log.i(TAG, "getCurrentWeekInList: ${listWeek[2].size}")
        for (index in 0 until listWeek.size) {
            val item = listWeek[index]
            if (item[0] <= dayOfYear && item[item.size - 1] >= dayOfYear) {
                currentWeekIndex = index
                return item
            }
        }
        return null
    }



    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReportItemTabBinding =
        FragmentReportItemTabBinding.inflate(inflater, container, false)

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        requireActivity().finish()
      /*  if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        } else {
            requireActivity().finish()
        }*/
    }

}
