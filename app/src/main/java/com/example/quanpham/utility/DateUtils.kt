
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


    fun Date.getMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.time = this
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

    fun generateWeeksInYear(year: Int): MutableList<MutableList<Int>> {
        var isHasCurrentDay = false
        val currentDayOfYear = getDayOfYear()
        val totalDay = if (isLeapYear(year)) 366 else 365
        val weeks = mutableListOf<MutableList<Int>>()
        val currentWeek = mutableListOf<Int>()

        for (day in 1..totalDay) {
            currentWeek.add(day)
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

    fun findMonthAndDayOfMonth(dayOfYear: Int, year: Int): Pair<Int, Int> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear)

        val month = calendar.get(Calendar.MONTH) + 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        return Pair(dayOfMonth,month)
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
    fun getTimeNow(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
    fun getDayOfMonth(timeInMillis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        return dayOfMonth
    }
    fun getMonthOfYear(timeInMillis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        val month = calendar.get(Calendar.MONTH) + 1
        return month
    }
    fun getDayOfYear(timeInMillis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        return dayOfYear
    }
    fun getHour(inputDate: String = getTimeNow()): Int {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date = inputDate.let { format.parse(it) }

        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.HOUR_OF_DAY)
    }
    fun getStartOfYear(year: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getEndOfYear(year: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        calendar.set(Calendar.DAY_OF_MONTH, 31)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 58)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    fun getStartOfDay(currentTime: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    fun getEndOfDay(currentTime: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 58)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    fun getStartDayOfYear(dayOfYear : Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR,dayOfYear)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    fun getDayOfYear(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_YEAR)
    }
    fun getEndDayOfYear(dayOfYear : Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR,dayOfYear)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 58)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    fun getStartOfDayMinus(currentTime: Long, dayMinus : Int): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        calendar.add(Calendar.DAY_OF_YEAR,-dayMinus)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 1)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    fun getEndOfDayMinus(currentTime: Long,dayMinus : Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH,-dayMinus)
        calendar.timeInMillis = currentTime
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 58)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    fun getStartOfHour(currentTime: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    fun convertSecondToTime(second:Long) : String{
        if(second < 60)
            return "${second}s"
        else if(second < 3600)
            return "${second/60}m ${second%60}s"
        return "${second / 3600}h ${(second%3600)/60}m"
    }
    fun getTimeFromDayOfYear(dayOfYear : Int) : Long{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear)
        return calendar.timeInMillis
    }
    fun getDateFromDayOfYear(dayOfYear : Int) : Date{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear)
        return calendar.time
    }
    fun getDayFromDate(date : Date) : Int{
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_YEAR)
    }

    fun getDateFromTimeMillis(timeInMillis : Long) : Date{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        return calendar.time
    }

    interface SelectDatetimeListener {
        fun onDateSelected(day: Int, month: Int, year: Int) {

        }

        fun onTimeSelected(minute: Int, hour: Int) {

        }
    }
}