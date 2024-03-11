package com.example.quanpham.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.example.quanpham.R
import com.mobiai.views.beforeafter.chart.utility.drawTriangle

class BMIChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val listBMIValue = arrayListOf(
        15, 18, 25, 30, 35, 40
    )


    private val typedArray: TypedArray
    private var colorBox = Color.parseColor("#9DD030")
    private var textValueColor = Color.WHITE
    private var bmiHeight = 20f
    private val boxPaint = Paint()
    private var listBoxValue = ArrayList<String>()
    private val listStartPoint = ArrayList<Float>()
    private var clickAble = false

    fun setListBoxValue(listBoxValue: ArrayList<String>) {
        this.listBoxValue = listBoxValue
        invalidate()
    }


    init {
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.BMIChart)
        colorBox = typedArray.getColor(R.styleable.BMIChart_boxColor, Color.parseColor("#9DD030"))
        textValueColor = typedArray.getColor(R.styleable.BMIChart_textBoxColor, Color.WHITE)
        bmiHeight = typedArray.getInt(R.styleable.BMIChart_bmiHeight, 20).toFloat()
        clickAble = typedArray.getBoolean(R.styleable.BMIChart_clickable, false)

        typedArray.recycle()
    }

    private val listColor = arrayListOf(
        Color.parseColor("#FDC944"),
        Color.parseColor("#9DD030"),
        Color.parseColor("#FDC944"),
        Color.parseColor("#FF8C39"),
        Color.parseColor("#FF395D")
    )

    private fun getListRect(height: Float, width: Float): ArrayList<RectF> {
        val listShapeData = ArrayList<RectF>()
        var drawWidth = width - startX * 2 - spaceRect * 5
        val minvalue = listBMIValue.minOrNull() ?: 0
        val maxvalue = listBMIValue.maxOrNull() ?: 0
        val countValue = maxvalue - minvalue

        val spaceWidthValue = (drawWidth / countValue)
        for (i in 1 until listBMIValue.size) {
            val valueSpace = listBMIValue[i] - listBMIValue[i - 1]
            val rectWidth = valueSpace * spaceWidthValue
            val rectf = RectF(
                startX,
                height - (height * 0.4f),
                startX + rectWidth,
                height - (height * 0.4f) + bmiHeight
            )
            listShapeData.add(rectf)
            listStartPoint.add(startX)

            startX += rectWidth + spaceRect
        }
        return listShapeData
    }

    private fun getIndexByPoint(
        event: MotionEvent,
        calback: (Int, PointF) -> Unit
    ) {
        listShape.forEachIndexed { index, rectF ->
            if (rectF.left <= event.x && rectF.right > event.x) {
                val pointX = rectF.left + (rectF.right - rectF.left) / 2
                val pointF = PointF(pointX, event.y)
                calback(index, pointF)
            }
        }
    }

    private var columnSelected = 0

    fun setValue(number: Float) {
        if (number > 40f || number < 15f) {
            columnSelected = -1
        } else {
            for (i in 0 until listBMIValue.size - 1) {
                if (number >= listBMIValue[i].toFloat() && number < listBMIValue[i + 1].toFloat()) {
                    columnSelected = i
                }
            }
        }
        invalidate()
    }

    private var centerPointRect: PointF = PointF(0f, 0f)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                if (clickAble) {
                    getIndexByPoint(event) { index, currentPoint ->
                        columnSelected = index
                        centerPointRect = currentPoint
                        invalidate()
                    }
                }

            }
        }
        invalidate()
        return true
    }

    var listShape = ArrayList<RectF>()
    private fun drawBoxValue(canvas: Canvas?) {
        var colorStart = 0
        listShape.forEach { rectF ->
            canvas?.drawRoundRect(
                rectF, (bmiHeight / 2),
                bmiHeight / 2, boxPaint.apply {
                    color = listColor.get(colorStart)
                }
            )
            colorStart++
        }

        val textPaint = Paint().apply {
            color = Color.RED
            style = Paint.Style.FILL
            textSize = 24f
        }
        listStartPoint.forEachIndexed { index, fl ->
            val text = listBMIValue[index].toString()
            canvas?.drawText(
                text,
                fl,
                height - (height * 0.4f) + bmiHeight * 2 + bmiHeight / 4,
                textPaint
            )
            if (index == listStartPoint.count() - 1) {
                val nextText = listBMIValue[index + 1].toString()
                val textWidth = textPaint.measureText(nextText)
                canvas?.drawText(
                    nextText,
                    width.toFloat() - spaceRect - textWidth,
                    height - (height * 0.4f) + bmiHeight * 2 + bmiHeight / 4,
                    textPaint
                )
            }
        }
    }

    fun getPointByIndex(currentIndex: Int, calback: (PointF) -> Unit) {
        listShape.forEachIndexed { index, rectF ->
            if (index == currentIndex) {
                val pointX = rectF.left + (rectF.right - rectF.left) / 2
                var pointF = PointF(pointX, 0f)
                calback(pointF)
            }
        }
    }
    private fun drawShapeBox(canvas: Canvas, columnSelected: Int) {
        val width = canvasWidth
        val height = canvasHeight
        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = 22f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val text = listBoxValue.get(columnSelected)

        var boxWidth = textPaint.measureText(text) * 1.25f
        var boxHeight = height * 0.45f
        val top = height * 0.02f
        getPointByIndex(columnSelected) {
            centerPointRect = it
        }
        var left = centerPointRect.x - boxWidth / 2
        if (left < 2) {
            left = 2f
        }
        if (boxWidth + left > width - 2f) {
            left = width - boxWidth - 5f
        }
        val shapePaint = Paint().apply {
            style = Paint.Style.FILL
            color = listColor.get(columnSelected)
        }
        val rectF = RectF(left, top, left + boxWidth, boxHeight)
        canvas.drawRoundRect(
            rectF, boxHeight / 2, boxHeight / 2, shapePaint
        )
        if (centerPointRect.x == 0f) {
            centerPointRect.x = rectF.right - rectF.right / 2
        }
        val textX = rectF.centerX()
        val textY = rectF.centerY() + textPaint.textSize / 2
        canvas.drawText(text, textX, textY, textPaint)

        canvas.drawTriangle(
            shapePaint,
            Point((centerPointRect.x - 10f).toInt(), boxHeight.toInt()),
            Point((centerPointRect.x + 10f).toInt(), boxHeight.toInt()),
            Point(centerPointRect.x.toInt(), (boxHeight + 16f).toInt())
        )
    }

    private var canvasWidth = 0f
    private var canvasHeight = 0f
    private var startX = 6f
    private val spaceRect = 8f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvasWidth = width.toFloat()
        canvasHeight = height.toFloat()
        if (listShape.isEmpty()) {
            listShape = getListRect(canvasHeight, canvasWidth)
        }
        drawBoxValue(canvas)
        if (listBoxValue.size === 5 && columnSelected!=-1) {
            drawShapeBox(canvas!!, columnSelected)
        }
    }
}
