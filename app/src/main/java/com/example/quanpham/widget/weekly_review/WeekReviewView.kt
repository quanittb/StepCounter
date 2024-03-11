package com.mobiai.base.chart.weekly_review

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.quanpham.widget.weekly_review.WeekReview
import com.example.quanpham.R

class WeekReviewView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    data class WeekData(var value: Float, var day: String, var isToDay: Boolean = false)

    private var listValue: ArrayList<WeekData> = ArrayList()
    private var arrTexView :ArrayList<TextView> = ArrayList()
    private var arrWeekView :ArrayList<WeekReview> = ArrayList()

    private lateinit var llView1: LinearLayout
    private lateinit var value1: WeekReview
    private lateinit var txt1: TextView

    private lateinit var llView2: LinearLayout
    private lateinit var value2: WeekReview
    private lateinit var txt2: TextView

    private lateinit var llView3: LinearLayout
    private lateinit var value3: WeekReview
    private lateinit var txt3: TextView

    private lateinit var llView4: LinearLayout
    private lateinit var value4: WeekReview
    private lateinit var txt4: TextView

    private lateinit var llView5: LinearLayout
    private lateinit var value5: WeekReview
    private lateinit var txt5: TextView

    private lateinit var llView6: LinearLayout
    private lateinit var value6: WeekReview
    private lateinit var txt6: TextView

    private lateinit var llView7: LinearLayout
    private lateinit var value7: WeekReview
    private lateinit var txt7: TextView


    init {
        val view = inflate(context, R.layout.list_week_review, this)
        initView(view)


        setTextSize(14f)
        setStrokeWidth(12f)
    }

    fun setStrokeWidth(fl: Float) {
        value1.setStrokeWidth(fl)
        value2.setStrokeWidth(fl)
        value3.setStrokeWidth(fl)
        value4.setStrokeWidth(fl)
        value5.setStrokeWidth(fl)
        value6.setStrokeWidth(fl)
        value7.setStrokeWidth(fl)
        invalidate()
    }

    fun setTextSize(textSize: Float) {
        txt1.textSize = textSize
        txt2.textSize = textSize
        txt3.textSize = textSize
        txt4.textSize = textSize
        txt5.textSize = textSize
        txt6.textSize = textSize
        txt7.textSize = textSize
        invalidate()
    }

    fun setData(value: ArrayList<WeekData>) {
        this.listValue = value
        if (listValue.count() == 7) {
            setDataToView(arrWeekView,arrTexView,listValue)
        }
        invalidate()
    }

    private fun setDataToView(
        arrWeekView: ArrayList<WeekReview>,
        arrTexView: ArrayList<TextView>,
        listValue: ArrayList<WeekData>
    ) {
        arrWeekView.forEachIndexed { index, it ->
            it.setArcValue(listValue[index].value)
        }
        arrTexView.forEachIndexed { index, textView ->
            val data= listValue[index]
            if(data.isToDay){
                textView.setTypeface(null,Typeface.BOLD)
            }
            else{
                textView.setTypeface(null, Typeface.NORMAL)
            }
            textView.text= data.day

        }

        invalidate()
    }

    private fun initView(view: View?) {
        llView1 = view!!.findViewById(R.id.ll_1)
        value1 = view.findViewById(R.id.val_1)
        txt1 = view.findViewById(R.id.txt_1)
        arrTexView.add(txt1)
        arrWeekView.add(value1)

        llView2 = view.findViewById(R.id.ll_2)
        value2 = view.findViewById(R.id.val_2)
        txt2 = view.findViewById(R.id.txt_2)

        arrTexView.add(txt2)
        arrWeekView.add(value2)

        llView3 = view.findViewById(R.id.ll_3)
        value3 = view.findViewById(R.id.val_3)
        txt3 = view.findViewById(R.id.txt_3)
        arrTexView.add(txt3)
        arrWeekView.add(value3)


        llView4 = view.findViewById(R.id.ll_4)
        value4 = view.findViewById(R.id.val_4)
        txt4 = view.findViewById(R.id.txt_4)
        arrTexView.add(txt4)
        arrWeekView.add(value4)
        llView5 = view.findViewById(R.id.ll_5)
        value5 = view.findViewById(R.id.val_5)
        txt5 = view.findViewById(R.id.txt_5)
        arrTexView.add(txt5)
        arrWeekView.add(value5)
        llView6 = view.findViewById(R.id.ll_6)
        value6 = view.findViewById(R.id.val_6)
        txt6 = view.findViewById(R.id.txt_6)
        arrTexView.add(txt6)
        arrWeekView.add(value6)
        llView7 = view.findViewById(R.id.ll_7)
        value7 = view.findViewById(R.id.val_7)
        txt7 = view.findViewById(R.id.txt_7)
        arrTexView.add(txt7)
        arrWeekView.add(value7)
    }
}