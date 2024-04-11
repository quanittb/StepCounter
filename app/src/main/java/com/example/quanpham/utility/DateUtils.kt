
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import com.example.quanpham.R
import com.example.quanpham.model.PairModel
import com.example.quanpham.utility.Constant.DEFAULT_DATE
import com.example.quanpham.utility.Constant.DEFAULT_TIME
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object DateUtils {
    private val TAG = DateUtils::javaClass.name
    fun getTime24(timestamp: Int, format: String): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp.times(1000L)
        val time12 = DateFormat.format("dd/MM hh:mm a", calendar).toString()
        val df = SimpleDateFormat("dd/MM hh:mm aa", Locale.getDefault())
        val outputFormat = SimpleDateFormat(format, Locale.getDefault())
        val date: Date?
        val output: String?
        return try {
            date = df.parse(time12)
            output = outputFormat.format(date!!)
            output
        } catch (pe: ParseException) {
            pe.printStackTrace()
            time12
        }
    }

    fun getCurrentMonth() : Int {
       return Calendar.getInstance()[Calendar.MONTH] + 1
    }
    fun getCurrentHour(): String {
        val format = SimpleDateFormat("HH", Locale.getDefault())
        return format.format(Date())
    }

    fun getDate(milliSeconds: Long, dateFormat: String): String? {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    /**
     * @author Phong-Apero
     * @since 19-04-2023
     * get current year in int
     * @return year int
     */
    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    fun getCurrentDate(pattern: String): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        val calendar = Calendar.getInstance()
        return formatter.format(calendar.time)
    }

    fun getCurrentMilliSecond(): Long {
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis
    }

    fun getCurrentDate(): Date {
        val c = Calendar.getInstance()
        return c.time
    }

    fun getCurrentDay () : Int {
        return  Calendar.getInstance()[Calendar.DAY_OF_MONTH]
    }
    fun getCurrentDayOfYear () : Int {
        return  Calendar.getInstance()[Calendar.DAY_OF_YEAR]
    }
    fun getCurrentTime(pattern: String): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())

        return formatter.format(calendar.time)
    }

    fun convertDateToString(pattern: String, date: Date): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertSelectDateTime(date: String, time: String): Date? {
        val formatter = SimpleDateFormat("${DEFAULT_TIME} $DEFAULT_DATE")
        val dateValue = "$time $date"
        return formatter.parse(dateValue)
    }

    fun convertDateToStringDayTime(pattern: String, date: Date): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(date)
    }

    fun Date.strDate(dateFormat: String) = try {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.format(this)
    } catch (ex: java.lang.Exception) {
        ""
    }


    fun format(dateStr: String, dateFormat: String): Date? {
        return try {
            val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
            formatter.parse(dateStr)
        } catch (ex: Exception) {
            null
        }
    }

    fun getDateStr(milliSeconds: Long, dateFormat: String): String? {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    fun Date.strDateTime(dateFormat: String) = try {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.format(this)
    } catch (ex: java.lang.Exception) {
        ""
    }

    fun getDate(timeMillis: Long): Date {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMillis
        return calendar.time
    }

    fun Date.getMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.timeInMillis
    }

    fun Date.getFromDateMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    fun Date.getToDateMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        return calendar.timeInMillis
    }

    fun openDatePicker(
        context: Context, date: Date? = null, listener: SelectDatetimeListener? = null
    ) {
        val c = Calendar.getInstance()
        if (date != null) {
            c.time = date
        }
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            context, { _, yearSelected, monthOfYear, dayOfMonth ->
                listener?.onDateSelected(dayOfMonth, monthOfYear + 1, yearSelected)
            }, year, month, day
        )
        dpd.show()
    }

/*    fun openTimePicker(context: Context, date: Date?, listener: SelectDatetimeListener? = null) {
        val c = Calendar.getInstance()
        if (date != null) {
            c.time = date
        }
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourSelected, minuteSelected ->
            listener?.onTimeSelected(minuteSelected, hourSelected)
        }
        val timePickerDialog = TimePickerDialog(context, R.style.DateTimePickerTheme , timeSetListener, hour, minute, true)
        timePickerDialog.show()
    }*/

    fun strTime(hour: Int, minute: Int) = try {
        String.format("%02d:%02d", hour, minute)
    } catch (ex: java.lang.Exception) {
        ""
    }

    fun getDateBefore(before: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -before)
        return calendar.time
    }

    /*
    * calculate different between 2 days
    * for example, departmentDay is 20/06/2023 & destinationDay is 15/05/2023. Different day is 5 days
    * */
    fun getDifferentBetweenTwoDays(departmentDay: Long, destinationDay: Long): Long {
        val dif = departmentDay - destinationDay
        val differentTimeInDay = TimeUnit.DAYS.convert(dif, TimeUnit.DAYS)
        val output = abs(differentTimeInDay / (1000 * 60 * 60 * 24))
        return output
    }


    //trả về tháng theo text
    fun getMonthInStringId (monthInNumber : Int) : Int{
      return  when (monthInNumber){
            1 -> R.string.month_january
            2 -> R.string.month_february
            3 -> R.string.month_march
            4 -> R.string.month_april
            5 -> R.string.month_may
            6 -> R.string.month_june
          7 -> R.string.month_july
          8 -> R.string.month_august
          9 -> R.string.month_september
          10 -> R.string.month_october
          11 -> R.string.month_november
          12 -> R.string.month_december
          else -> R.string.month_january
      }
    }
    fun getMonthInStringIdSplit3 (monthInNumber : Int) : Int{
        return  when (monthInNumber){
            1 -> R.string._month_jan
            2 -> R.string._month_feb
            3 -> R.string._month_mar
            4 -> R.string._month_apr
            5 -> R.string._month_may
            6 -> R.string._month_jun
            7 -> R.string._month_jul
            8 -> R.string._month_aug
            9 -> R.string._month_sep
            10 -> R.string._month_oct
            11 -> R.string._month_nov
            12 -> R.string._month_dec
            else -> R.string._month_jan
        }
    }
    fun getMonthInStringText(context: Context? , monthInNumber: Int) : String{
        if (context == null){
            return ""
        }
        val txt = context.getString(getMonthInStringIdSplit3(monthInNumber))
        return txt.substring(0, txt.length.coerceAtMost(3)) +" "
    }
    /*
    nhập vào năm , trả về một danh sách các tuần trong năm , mỗi tuần 7 ngày
    nếu năm nhuận có 51 tuần 7 ngày và 1 tuần 9 ngày
    nếu năm không nhuận có 51 tuần 7 ngày và 1 tuần 8 ngày
     */
    fun generateWeeksInYear(year: Int): MutableList<MutableList<Int>> {
        var isHasCurrentDay: Boolean = false
        val currentDayOfYear = getDayOfYear()
        val totalDay = if (isLeapYear(year)) 366 else 365
        val weeks = mutableListOf<MutableList<Int>>()
        val currentWeek = mutableListOf<Int>()

        for (day in 1..totalDay) {
            currentWeek.add(day)
            /*
              if (day == totalDay && currentWeek.size >= 1) {
                weeks.lastOrNull()?.addAll(currentWeek)
                currentWeek.clear()
            } else
             */
            if (currentWeek.contains(currentDayOfYear)) {
                isHasCurrentDay = true
            }

            if (currentWeek.size == 7 || day == totalDay) {
                weeks.add(ArrayList(currentWeek))
                if (isHasCurrentDay) {
                    return weeks
                }
                currentWeek.clear()
            }

        }
        Log.i(TAG, "generateWeeksInYear: total week = ${weeks.size}")
        for (item in weeks){
            Log.i(TAG, "generateWeeksInYear: size = ${item.size}, days = ${item}")
        }

        return weeks
    }


    /*
    nhập vào số ngày và năm, trả về kết quả ngày nhập vào thuộc tháng thứ mấy trong năm
     */
    fun findMonthFromDay(dayOfYear: Int, year: Int): Int {
        val daysInMonths =
            if (isLeapYear(year))
                intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            else
                intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        var currentDay = dayOfYear
        var currentMonth = 0

        for (i in 0 until 12) {
            if (currentDay <= daysInMonths[i]) {
                currentMonth = i + 1
                break
            }
            currentDay -= daysInMonths[i]
        }

        return currentMonth
    }



    /*
        nhập vào tháng và năm , trả về tổng số ngày trong tháng đó
     */

    fun getDaysInMonth(month: Int, year: Int): Int {
        val daysInMonth = when (month) {
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> 31
        }
        return daysInMonth
    }

    fun getListMonth(year: Int): MutableList<Int> {
        return if (isLeapYear(year))
            mutableListOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        else
            mutableListOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    }


    /*
    nhập vào tháng và năm , trả về ngày bắt đầu và ngày kết thúc của tháng đó trong năm
     */
    fun getStartDayAndEndDayOfMonth(month: Int, year: Int): PairModel {

        val daysInMonths =
            if (isLeapYear(year))
                intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            else
                intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        var countDay = 0
        var startDay =0
        var endDay = 0
        for (i  in daysInMonths.indices){
            if (i < month - 1) {
                countDay += daysInMonths[i]
            } else if (i == month - 1) {
                startDay = countDay + 1
                endDay = countDay + daysInMonths[i] - 1
                return PairModel(startDay, endDay)
            }
        }
        return PairModel(1, 31)
    }

    fun getStartAndEndDayOfMonth(year: Int) : MutableList<PairModel>{
        val currentMonth = getCurrentMonth()
        val isLeapYear = isLeapYear(year)
        val totalDaysInYear = if (isLeapYear) 366 else 365
        var startDayOfYear = 1
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        var monthRanges : MutableList<PairModel> = arrayListOf()

        for (month in Calendar.JANUARY until getCurrentMonth() ) {
            calendar.set(Calendar.MONTH, month)
            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val endDayOfYear = startDayOfYear + daysInMonth - 1

            monthRanges.add(PairModel(startDayOfYear, endDayOfYear))
            startDayOfYear = endDayOfYear + 1
        }
        return monthRanges
    }

     fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    fun getDayOfYear(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        return calendar.get(Calendar.DAY_OF_YEAR)
    }
    fun findDayOfYear(day: Int, month: Int, year: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        return calendar.get(Calendar.DAY_OF_YEAR)
    }

    fun findMonthAndDayOfMonth(dayOfYear: Int, year: Int): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear)

        val month = calendar.get(Calendar.MONTH) + 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        return Pair(dayOfMonth,month)
    }

    fun findDayOfMonth(dayOfYear: Int, year: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear)

        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun findDayOfWeek(dayOfYear: Int,  year: Int) : String{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear)
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "S"
            Calendar.MONDAY -> "M"
            Calendar.TUESDAY -> "T"
            Calendar.WEDNESDAY -> "W"
            Calendar.THURSDAY -> "T"
            Calendar.FRIDAY -> "F"
            Calendar.SATURDAY -> "SA"
            else -> ""
        }
    }
    fun secondsToMinutes(seconds: Long): Pair<Long, Long> {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return Pair(minutes, remainingSeconds)
    }


    interface SelectDatetimeListener {
        fun onDateSelected(day: Int, month: Int, year: Int) {

        }

        fun onTimeSelected(minute: Int, hour: Int) {

        }
    }
}