package com.example.quanpham.fragment

import  DateUtils
import DateUtils.convertSecondToTime
import DateUtils.findDayOfWeek
import DateUtils.findMonthAndDayOfMonth
import DateUtils.getCurrentDayOfYear
import DateUtils.getCurrentYear
import DateUtils.getDateFromDayOfYear
import DateUtils.getDayOfMonth
import DateUtils.getDayOfYear
import DateUtils.getEndDayOfYear
import DateUtils.getEndOfDayMinus
import DateUtils.getMonthInStringIdSplit3
import DateUtils.getMonthOfYear
import DateUtils.getStartDayOfYear
import DateUtils.getStartOfDayMinus
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
import com.example.quanpham.utility.formatNumbers
import com.example.quanpham.utility.rxbus.ChangeUnit
import com.example.quanpham.utility.rxbus.RxBus
import com.example.quanpham.utility.rxbus.UpdateAvgValue
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

        currentStep.observe(this) {
            binding.run {
                initData(timeType)
            }
        }
    }

    private fun handlerRx() {
        addDispose(listenEvent({
            when (it) {
                is ChangeUnit -> {
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
//                listMonth.addAll(DateUtils.getStartDayAndEndDayOfMonth(getCurrentYear()))
                listMonth.addAll(DateUtils.getStartAndEndDayOfMonth(getCurrentYear()))
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
                        calendar.add(Calendar.DAY_OF_YEAR, -currentDayIndex)
                        binding.tvTime.text = "${
                            DateUtils.getMonthInStringText(
                                requireContext(),
                                calendar.get(Calendar.MONTH) + 1
                            )
                        } ${calendar.get(Calendar.DAY_OF_MONTH)}"
                        updateDataToDayChart(currentDayIndex)
                    }
                    ivNext.setOnClickListener {
                        if (currentDayIndex == 0) return@setOnClickListener
                        currentDayIndex--
                        val calendar = Calendar.getInstance()
                        calendar.add(Calendar.DAY_OF_YEAR, -currentDayIndex)
                        binding.tvTime.text = "${
                            DateUtils.getMonthInStringText(
                                requireContext(),
                                calendar.get(Calendar.MONTH) + 1
                            )
                        } ${calendar.get(Calendar.DAY_OF_MONTH)}"
                        updateDataToDayChart(currentDayIndex)
                    }

                }
            }

            WEEK -> {
                val dayOfYear = getDayOfYear()
                currentWeek.clear()
                // thêm ds ngày của tuần hiện tại
                getCurrentWeekInList(dayOfYear)?.let { currentWeek.addAll(ArrayList(it)) }
                if (currentWeek.isNotEmpty()) {
                    Log.i(TAG, "setTimePickerBehaviour: $currentWeek")
                    val startDay = findMonthAndDayOfMonth(
                        currentWeek[0],
                        getCurrentYear()
                    )
                    val endDay = findMonthAndDayOfMonth(
                        currentWeek[currentWeek.size - 1],
                        getCurrentYear()
                    )
                    val startString =
                        "${getString(getMonthInStringIdSplit3(startDay.second))} ${startDay.first}"
                    val endString =
                        "${getString(getMonthInStringIdSplit3(endDay.second))} ${endDay.first}"
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
                            val startString =
                                "${getString(getMonthInStringIdSplit3(startDay.second))} ${startDay.first}"
                            val endString =
                                "${getString(getMonthInStringIdSplit3(endDay.second))} ${endDay.first}"
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
                            val startString =
                                "${getString(getMonthInStringIdSplit3(startDay.second))} ${startDay.first}"
                            val endString =
                                "${getString(getMonthInStringIdSplit3(endDay.second))} ${endDay.first}"
                            binding.tvTime.text = "${startString} -${endString} "
                            updateDataToWeekChart(currentWeek)
                        }
                    }
                }
            }

            MONTH -> {
                currentMonthIndex = DateUtils.getCurrentMonth() // month đã +1
                val monthInString =
                    requireContext().getString(DateUtils.getMonthInStringId(currentMonthIndex))
                binding.tvTime.text =
                    monthInString.substring(0, monthInString.length.coerceAtMost(3))
                if (currentMonthIndex - 1 >= 0) {
                    currentMonth = listMonth[currentMonthIndex - 1]
                    updateDataToMonthChart(currentMonth)

                    binding.run {
                        ivPrevious.setOnClickListener {
                            if (currentMonthIndex - 1 <= 0) return@setOnClickListener
                            currentMonthIndex--
                            currentMonth = listMonth[currentMonthIndex - 1]
                            val monthInString = requireContext().getString(
                                DateUtils.getMonthInStringId(currentMonthIndex)
                            )
                            binding.tvTime.text =
                                monthInString.substring(0, monthInString.length.coerceAtMost(3))
                            if (currentMonth.startDay != -1 && currentMonth.endDay != -1) {
                                updateDataToMonthChart(currentMonth)
                            }

                        }

                        ivNext.setOnClickListener {

                            if (currentMonthIndex >= listMonth.size) return@setOnClickListener
                            currentMonthIndex++
                            currentMonth = listMonth[currentMonthIndex - 1]
                            val monthInString = requireContext().getString(
                                DateUtils.getMonthInStringId(currentMonthIndex)
                            )
                            binding.tvTime.text =
                                monthInString.substring(0, monthInString.length.coerceAtMost(3))
                            if (currentMonth.startDay != -1 && currentMonth.endDay != -1) {
                                updateDataToMonthChart(currentMonth)
                            }
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
    private fun updateDataToDayChart(minus: Int = 0) {
        val day = getCurrentDayOfYear() - minus
        val listStepDay = database.stepDao().getRecordStepsDay(
            getStartOfDayMinus(System.currentTimeMillis(), minus),
            getEndOfDayMinus(System.currentTimeMillis(), minus)
        )
        val totalDay = listStepDay.size
        val avgSteps = if (listStepDay.isNotEmpty())
            database.stepDao().getStepsDay(
                getStartOfDayMinus(System.currentTimeMillis(), minus),
                getEndOfDayMinus(System.currentTimeMillis(), minus)
            ) / totalDay
        else 0
        val avgCalories = if (listStepDay.isNotEmpty())
            database.stepDao().getCaloDay(
                getStartOfDayMinus(System.currentTimeMillis(), minus),
                getEndOfDayMinus(System.currentTimeMillis(), minus)
            ) / totalDay
        else 0
        val avgDistance = if (listStepDay.isNotEmpty())
            database.stepDao().getDistanceDay(
                getStartOfDayMinus(System.currentTimeMillis(), minus),
                getEndOfDayMinus(System.currentTimeMillis(), minus)
            ) / totalDay
        else 0
        val avgHour = if (listStepDay.isNotEmpty())
            database.stepDao().getTimeDay(
                getStartOfDayMinus(System.currentTimeMillis(), minus),
                getEndOfDayMinus(System.currentTimeMillis(), minus)
            ) / totalDay
        else 0
        RxBus.publish(
            UpdateAvgValue(
                MONTH,
                avgSteps.toFloat(),
                avgCalories.toFloat(),
                avgDistance.toFloat(),
                avgHour.toFloat()
            )
        )
        var listMonth = mutableListOf<Int>()
        val listRow: ArrayList<String> = arrayListOf()
        val listColum: ArrayList<Column> = arrayListOf()
        if (listStepDay.isNotEmpty())
            listStepDay.sortedBy { it.startTime }.forEach {
                if (getDayOfYear(it.startTime) == day) {
                    val hour = getHourFromDate(it.startTime)
                    listRow.add("${hour}H")
                    when (reportType) {
                        STEP -> {
                            listColum.add(
                                Column(
                                    it.step.toDouble(),
                                    "${it.step} ${requireContext().getString(R.string.steps)}",
                                    "${hour}:00-${hour}:59"
                                )
                            )
                            binding.chartView.setTargetValue(true, avgSteps.toInt())

                        }


                        CALORIE -> {
                            listColum.add(
                                Column(
                                    it.calo,
                                    "${it.calo} ${requireContext().getString(R.string.kcal)}",
                                    "${hour}:00-${hour}:59"
                                )
                            )
                            binding.chartView.setTargetValue(true, avgCalories.toInt())
                        }

                        DISTANCE -> {
                            if (SharedPreferenceUtils.unit)
                                listColum.add(
                                    Column(
                                        it.distance,
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
                            binding.chartView.setTargetValue(true, avgDistance.toInt())
                        }

                        TIME -> {
                            listColum.add(
                                Column(
                                    it.activeTime.toDouble(),
                                    convertSecondToTime(it.activeTime),
                                    "${hour}:00-${hour}:59"
                                )
                            )
                            binding.chartView.setTargetValue(true, avgHour.toInt())
                        }
                    }
                }
            }
        val currentDate = LocalDate.now()
        var today = currentDate.dayOfYear
        binding.chartView.setColumnSelected(listMonth.indexOf(today))
        binding.chartView.listHorizontal = listRow
        binding.chartView.setData(listColum)
    }

    @SuppressLint("NewApi")
    private fun updateDataToMonthChart(currentMonth: PairModel) {
        val totalDayInMonth = currentMonth.endDay - currentMonth.startDay + 1
        val avgSteps =
            database.stepDao().getStepsDay(
                getStartDayOfYear(currentMonth.startDay),
                getEndDayOfYear(currentMonth.endDay)
            ) / totalDayInMonth
        val avgCalories =
            database.stepDao().getCaloDay(
                getStartDayOfYear(currentMonth.startDay),
                getEndDayOfYear(currentMonth.endDay)
            ) / totalDayInMonth
        val avgDistance =
            database.stepDao().getDistanceDay(
                getStartDayOfYear(currentMonth.startDay),
                getEndDayOfYear(currentMonth.endDay)
            ) / totalDayInMonth
        val avgHour =
            database.stepDao().getTimeDay(
                getStartDayOfYear(currentMonth.startDay),
                getEndDayOfYear(currentMonth.endDay)
            ) / totalDayInMonth
        RxBus.publish(
            UpdateAvgValue(
                MONTH,
                avgSteps.toFloat(),
                avgCalories.toFloat(),
                avgDistance.toFloat(),
                avgHour.toFloat()
            )
        )

        val listCollum: ArrayList<Column> = arrayListOf()
        val listRow: ArrayList<String> = arrayListOf()
        val listStepOfMonth = database.stepDao().getRecordStepsDay(
            getStartDayOfYear(currentMonth.startDay),
            getEndDayOfYear(currentMonth.endDay)
        )
        val listFinalStepDayOfMonth: ArrayList<Steps> = arrayListOf()
        for (day in currentMonth.startDay..currentMonth.endDay) {
            val step = Steps(startTime = getDateFromDayOfYear(day))
            listStepOfMonth.forEach { item ->
                if (getDayOfYear(item.startTime) == day) {
                    step.step += item.step
                    step.calo += item.calo
                    step.distance += item.distance
                    step.activeTime += item.activeTime
                    step.startTime = item.startTime
                }
            }
            listFinalStepDayOfMonth.add(step)
        }
        for (i in 1..listFinalStepDayOfMonth.lastIndex step 5) {
            val date = findMonthAndDayOfMonth(currentMonth.startDay + i, getCurrentYear())
            var monthString = getString(getMonthInStringIdSplit3(date.second))
            listRow.add("$monthString ${date.first}")
        }

        when (reportType) {
            STEP -> {
                listCollum.clear()
                binding.chartView.setTargetValue(true, avgSteps.toInt())
                listFinalStepDayOfMonth.sortedBy { it.startTime }.forEach {
                    var monthString =
                        getString(getMonthInStringIdSplit3(getMonthOfYear(it.startTime.time)))
                    var day = getDayOfMonth(it.startTime.time)
                    val column = Column(
                        it.step.toDouble(),
                        "${formatNumbers(it.step.toFloat())} ${requireContext().getString(R.string.steps)}",
                        "$monthString $day"
                    )
                    listCollum.add(column)
                }
            }

            CALORIE -> {
                listCollum.clear()
                binding.chartView.setTargetValue(true, avgCalories.toInt())

                listFinalStepDayOfMonth.sortedBy { it.startTime }.forEach {
                    var monthString =
                        getString(getMonthInStringIdSplit3(getMonthOfYear(it.startTime.time)))
                    var day = getDayOfMonth(it.startTime.time)
                    val column = Column(
                        it.calo.toDouble(),
                        "${formatNumbers(it.calo.toFloat())} ${requireContext().getString(R.string.kcal)}",
                        "$monthString $day"
                    )
                    listCollum.add(column)
                }
            }

            TIME -> {
                listCollum.clear()
                binding.chartView.setTargetValue(true, avgHour.toInt())
                listFinalStepDayOfMonth.sortedBy { it.startTime }.forEach {
                    var monthString =
                        getString(getMonthInStringIdSplit3(getMonthOfYear(it.startTime.time)))
                    var day = getDayOfMonth(it.startTime.time)
                    val column = Column(
                        it.activeTime.toDouble(),
                        convertSecondToTime(it.activeTime),
                        "$monthString $day"
                    )
                    listCollum.add(column)
                }

            }

            DISTANCE -> {
                listCollum.clear()
                binding.chartView.setTargetValue(true, avgDistance.toInt())
                listFinalStepDayOfMonth.sortedBy { it.startTime }.forEach {
                    var monthString =
                        getString(getMonthInStringIdSplit3(getMonthOfYear(it.startTime.time)))
                    var day = getDayOfMonth(it.startTime.time)
                    val column = Column(
                        it.distance.toDouble(),
                        "${it.distance} ${requireContext().getString(R.string.km)}",
                        "$monthString $day"
                    )
                    listCollum.add(column)
                }

            }
        }

        var listMonth = mutableListOf<Int>()
        for (curren in currentMonth.startDay..currentMonth.endDay) {
            listMonth.add(curren)
        }

        val currentDate = LocalDate.now()
        var today = currentDate.dayOfYear
        binding.chartView.setColumnSelected(listMonth.indexOf(today))
        binding.chartView.listHorizontal = listRow
        binding.chartView.setData(listCollum)
    }

    private fun updateDataToWeekChart(currentWeek: MutableList<Int>) {
        val totalDayInWeek = 7
        val avgSteps =
            database.stepDao().getStepsDay(
                getStartDayOfYear(currentWeek.first()),
                getEndDayOfYear(currentWeek.last())
            ) / totalDayInWeek
        val avgCalories =
            database.stepDao().getCaloDay(
                getStartDayOfYear(currentWeek.first()),
                getEndDayOfYear(currentWeek.last())
            ) / totalDayInWeek
        val avgDistance =
            database.stepDao().getDistanceDay(
                getStartDayOfYear(currentWeek.first()),
                getEndDayOfYear(currentWeek.last())
            ) / totalDayInWeek
        val avgHour =
            database.stepDao().getTimeDay(
                getStartDayOfYear(currentWeek.first()),
                getEndDayOfYear(currentWeek.last())
            ) / totalDayInWeek
        RxBus.publish(
            UpdateAvgValue(
                MONTH,
                avgSteps.toFloat(),
                avgCalories.toFloat(),
                avgDistance.toFloat(),
                avgHour.toFloat()
            )
        )
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
        val listFinalStepDayOfWeek: ArrayList<Steps> = arrayListOf()
        currentWeek.forEach {
            var step = Steps(startTime = Date())
            listStepWeek.forEach { item ->
                if (getDayOfYear(item.startTime) == it) {
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
                STEP -> {
                    listColum.add(
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
                    binding.chartView.setTargetValue(true, avgSteps.toInt())
                }

                CALORIE -> {
                    listColum.add(
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
                    binding.chartView.setTargetValue(true, avgCalories.toInt())
                }

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
                    binding.chartView.setTargetValue(true, avgDistance.toInt())
                }

                TIME -> {
                    listColum.add(
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
                    binding.chartView.setTargetValue(true, avgHour.toInt())
                }
            }
        }

        binding.chartView.setColumnSelected(listMonth.indexOf(getDayOfYear()))
        binding.chartView.listHorizontal = listRow
        binding.chartView.setData(listColum)

    }

    private fun getCurrentWeekInList(dayOfYear: Int): MutableList<Int>? {
        for (index in 0 until listWeek.size) {
            val item = listWeek[index]
            if (item[0] <= dayOfYear && item[item.size - 1] >= dayOfYear) {
                currentWeekIndex = index
                return item
            }
        }
        return null
    }

    private fun getCurrentMonthInList(dayOfYear: Int): MutableList<Int>? {
        for (index in 0 until listMonth.size) {
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
