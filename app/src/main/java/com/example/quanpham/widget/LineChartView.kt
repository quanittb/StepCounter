package com.mobiai.base.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.mobiai.views.beforeafter.chart.utility.drawRoundRectPath
import com.mobiai.views.beforeafter.chart.utility.drawTriangle


class LineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var dataList: MutableList<Data> = mutableListOf()
    private var listPoint = mutableListOf<PointF>()
    private var minValue = 0f
    private var maxValue = 0f
    private var textHorzWidth = 0f
    private var listTextPoint = mutableListOf<PointF>()

    private var textColumnPaint = Paint()

    init {

        textColumnPaint = Paint().apply {
            textSize = 28f
            color = Color.parseColor("#66707A")
            style = Paint.Style.FILL
        }


    }

    private var position: Int = -1
    private var istouch = false

    private var centerPointRect = PointF(0f, 0f)
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                istouch = true
                getIndexByPoint(event) { i, pointF ->
                    this.position = i
                    centerPointRect = pointF
                }
                invalidate()
                onTouchCallback?.onValue(dataList.get(position).value, position)
            }
        }
        invalidate()
        return true
    }

    private var onTouchCallback: OnChartTouchEvent? = null

    // vẽ ngày tháng ở tọa độ x
    private fun drawVerticalValue(canvas: Canvas?) {
        if (listString.isEmpty()) return
        val textPaint = Paint().apply {
            textSize = 24f
            color = Color.parseColor("#9CA4AB")
            style = Paint.Style.FILL
        }

        // lấy độ rộng text dài nhất : Mar30
        val maxString = getStringMax(listString)
        val texWidth = textPaint.measureText(maxString)

        // TH số điểm nhập và size ngày băng nhau
        if (listString.size == listPoint.size) {
            listString.forEachIndexed { index, s ->
                var startX = 0f
                if (index == 0) {
                    // set ngày nhập là ngày đầu tiên trên chart
                    startX = listPoint.get(0).x
                } else if (index == listPoint.size - 1) {
                    // set ngày cuối cùng nhập là cuối cùng trên chart
                    startX = listPoint.get(index).x - texWidth
                } else {
                    // set ngày ở giữa trên chart
                    startX =
                        listPoint.get(index).x - textPaint.measureText(listString.get(index)) / 2f
                }
                val startY = startPointDrawY + (height - startPointDrawY) / 2
                canvas?.drawText(s, startX, startY, textPaint)
            }
        } else {
            // TH điểm nhập và size ngày khác nhau
            // tính độ rộng có thể vẽ
            val width = listPoint[listPoint.size - 1].x - listPoint[0].x
            // độ rộng của 1 ngày
            val spaceWidth = width / listString.size

            var startX = listPoint[0].x
            listString.forEachIndexed { index, s ->
                // vị trí trung tâm
                val centerX = startX + spaceWidth / 2
                val startY = startPointDrawY + (height - startPointDrawY) / 2
                val textWidth = textPaint.measureText(s)
                val textX = centerX - textWidth / 2
                canvas?.drawText(s, textX, startY, textPaint)
                startX += spaceWidth
            }
        }
    }
    // lấy giá trị ngày tháng có độ dài lớn nhất : Aug30
    fun getStringMax(listString: MutableList<String>): String? {
        if (listString.isEmpty()) {
            return null
        }

        var stringVal = listString[0]
        listString.forEach { item ->
            if (item.length > stringVal.length) {
                stringVal = item
            }
        }
        return stringVal
    }
    private fun getIndexByPoint(
        event: MotionEvent,
        calback: (Int, PointF) -> Unit
    ) {
        var firstPointLineX: Float
        var secondPointLineX: Float
        listPoint.forEachIndexed { index, pointF ->
            var indexVal = index
            indexVal.coerceIn(0,listPoint.size)
            if (indexVal >= listPoint.size - 1) {
                firstPointLineX = listPoint.get((indexVal - 1).coerceIn(0,listPoint.size-1)).x + spaceWidth / 2
                secondPointLineX = listPoint.get(indexVal.coerceIn(0,listPoint.size-1)).x + spaceWidth / 2
            } else if (indexVal == 0) {
                firstPointLineX = listPoint.get(0).x - spaceWidth / 2
                secondPointLineX = listPoint[(indexVal + 1).coerceIn(0,listPoint.size-1)].x - spaceWidth / 2
            } else {
                firstPointLineX = listPoint.get(indexVal - 1).x + spaceWidth / 2
                secondPointLineX = listPoint.get((indexVal + 1).coerceIn(0,listPoint.size-1)).x - spaceWidth / 2
            }
            if (event.x in firstPointLineX..secondPointLineX) {
                calback(indexVal, pointF)
            }
        }
        invalidate()
    }

    var isColorSeclect = false

    // insert list ngày của tháng
    fun setVerticalValue(listString: MutableList<String>) {
        this.listString.clear()
        this.listString.addAll(listString)
        invalidate()
    }

    private fun getPointByIndex(currentIndex: Int, calback: (PointF) -> Unit) {
        listPoint.forEachIndexed { index, rectF ->
            if (index == currentIndex) {
                calback(PointF(rectF.x,rectF.y))
            }
        }
    }


    private var listString: MutableList<String> = mutableListOf()




    fun getData(onChartTouchEvent: OnChartTouchEvent) {
        onTouchCallback = onChartTouchEvent
    }

    var spaceWidth = 0f
    private fun initPoint(data: MutableList<Data>, dataList: (MutableList<PointF>) -> Unit) {
        this.listPoint = mutableListOf()
        val dataSize = data.size - 1
        val spaceWidth = (endPointDrawX - startPointDrawX) / dataSize
        this.spaceWidth = spaceWidth
        var X = startPointDrawX
        listPoint.clear()
        val heightDraw = startPointDrawY - endPointDrawY
        this.dataList.forEach { data ->
            var Y = startPointDrawY - ((data.value - min) / (max - min)) * heightDraw
            this.listPoint.add(PointF(X, Y))
            X += spaceWidth
        }
        dataList(listPoint)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        getPointByIndex(position){
            this.centerPointRect= it
        }
        isColorSeclect = true
        drawTextPaint(canvas)
        drawLineHorizontal(canvas)
        if (listPoint != null && dataList .size>0) {
            drawPointAndLine(canvas!!)
            if(position>=0 && position< dataList.size+1){
                invalidate()
                drawBoxValue(canvas, position,centerPointRect)
            }

        }

        if (listString.size > 0) {
            drawVerticalValue(canvas)
        }

    }

    private var listStringMax = mutableListOf<String>()
    private var columnSelectColor = Color.parseColor("#0CB809")
    private fun drawBoxValue(
        canvas: Canvas,
        columnSelected: Int,
        centerPointRect: PointF
    ) {
        val textPaintTop = Paint().apply {
            color = Color.WHITE
            textSize = textTopBox
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val textWidth = listStringMax.get(getIndexOfLongestString(listStringMax)) +10

        val maxvalue = dataList.maxOf { it.value }
        val valueSel = dataList.get(columnSelected).value

        val boxWidth = textPaintTop.measureText(textWidth) * 2f
        val boxHeight = boxWidth * 0.8f
        var top = 0f
        var ismax=false
        if (maxvalue == valueSel) {
            top =centerPointRect.y  + 14f
            ismax=true
        } else {
            top =centerPointRect.y - boxHeight - 14f
            ismax=false
        }

        val shapePaint = Paint().apply {
            color = columnSelectColor
            style = Paint.Style.FILL
        }
        var startPoint = centerPointRect.x - boxWidth / 2
        if (startPoint <= startPointDrawX) {
            startPoint = startPointDrawX
        }
        if (startPoint + boxWidth > endPointDrawX) {
            startPoint = endPointDrawX - boxWidth
        }
        val rectF = RectF(startPoint, top, startPoint + boxWidth, top + boxHeight)
        canvas.drawRoundRectPath(
            rectF, rectF.height() / 4,
            true, true, true, true, shapePaint
        )
        drawTriangle(canvas, rectF, boxWidth, shapePaint,ismax)
    }

    private var textTopBox = 24f
    private fun drawTriangle(
        canvas: Canvas,
        rectF: RectF,
        boxWidth: Float,
        shapePaint: Paint,
        ismax: Boolean,
    ) {
        var firstPoint: PointF
        var secondPoint: PointF
        var threePoint: PointF
        if(ismax){
            if (centerPointRect.x <= startPointDrawX) {
                firstPoint = PointF(rectF.left + 15f, centerPointRect.y + 18f)
                secondPoint = PointF(firstPoint.x + 20f, centerPointRect.y + 18f)
                threePoint = PointF(centerPointRect.x, centerPointRect.y)
            } else if (centerPointRect.x + boxWidth/2f > endPointDrawX) {
                secondPoint = PointF(rectF.right - 15f, centerPointRect.y + 18f)
                firstPoint = PointF(secondPoint.x - 20f, centerPointRect.y + 18f)
                threePoint = PointF(centerPointRect.x, centerPointRect.y)
            } else {
                firstPoint = PointF(centerPointRect.x - 10f, centerPointRect.y + 18f)
                secondPoint = PointF(centerPointRect.x + 10f, centerPointRect.y + 18f)
                threePoint = PointF(centerPointRect.x, centerPointRect.y)
            }
        }
        else
        {
            if (centerPointRect.x <= startPointDrawX) {
                firstPoint = PointF(rectF.left + 15f, centerPointRect.y - 18f)
                secondPoint = PointF(firstPoint.x + 20f, centerPointRect.y - 18f)
                threePoint = PointF(centerPointRect.x, centerPointRect.y)
            } else if (centerPointRect.x + boxWidth/2f > endPointDrawX) {
                secondPoint = PointF(rectF.right - 15f, centerPointRect.y - 18f)
                firstPoint = PointF(secondPoint.x - 20f, centerPointRect.y - 18f)
                threePoint = PointF(centerPointRect.x, centerPointRect.y)
            } else {
                firstPoint = PointF(centerPointRect.x - 10f, centerPointRect.y - 18f)
                secondPoint = PointF(centerPointRect.x + 10f, centerPointRect.y - 18f)
                threePoint = PointF(centerPointRect.x, centerPointRect.y)

            }
        }

        canvas.drawTriangle(shapePaint, firstPoint, secondPoint, threePoint)
        val textPaint1 = Paint().apply {
            color = Color.WHITE
            textSize = 32f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val textPaint2 = Paint().apply {
            color = Color.WHITE
            textSize = 24f
            textAlign = Paint.Align.CENTER
        }
        val data = dataList.get(position)
        val textLine1 = data.valueUnit
        val textLine2 = data.valueDate
        val shapeHeight = boxWidth / 1.75f
        val textX = rectF.centerX()
        val textY1 = rectF.centerY() - shapeHeight / 12
        val textY2 = rectF.centerY() + shapeHeight / 4
        canvas.drawText(textLine1, textX, textY1 + 4f, textPaint1)
        canvas.drawText(textLine2, textX, textY2+5f, textPaint2)
    }

    fun getIndexOfLongestString(strings: MutableList<String>): Int {
        if (strings.isEmpty()) {
            return -1
        }
        var longestIndex = 0
        var maxLength = strings[0].length
        for (i in 1 until strings.size) {
            val currentLength = strings[i].length
            if (currentLength > maxLength) {
                longestIndex = i
                maxLength = currentLength
            }
        }
        return longestIndex
    }


    var isShowValue = false

    private fun drawPointAndLine(canvas: Canvas) {
        val graphPaint = Paint().apply {
            color = Color.parseColor("#A155B9")
            style = Paint.Style.FILL
            strokeWidth = 3f
        }

        var firstPointLineX = 0f
        var secondPointLineX = 0f
        var firstPointLineY = 0f
        var secondPointLineY = 0f

        initPoint(dataList) { list ->
            list.forEachIndexed { index, pointF ->
                firstPointLineX = pointF.x
                firstPointLineY = pointF.y
                if (index >= list.size - 1) {
                    secondPointLineX = list.get(index).x
                    secondPointLineY = list.get(index).y
                } else {
                    secondPointLineX = list.get(index + 1).x
                    secondPointLineY = list.get(index + 1).y
                }
                if (isShowValue) {
                    canvas.drawText(
                        dataList.get(index).value.toString(),
                        firstPointLineX + 6f,
                        firstPointLineY,
                        Paint().apply {
                            color = Color.GRAY
                            textSize = 18f
                            style = Paint.Style.FILL
                        })
                }

                canvas.drawLine(
                    firstPointLineX,
                    firstPointLineY,
                    secondPointLineX,
                    secondPointLineY,
                    graphPaint.apply {
                        color = Color.parseColor("#A155B9")
                    })

                canvas.drawCircle(pointF.x, pointF.y, 6f, graphPaint)
            }
        }
        if(listString.isNotEmpty()){
            drawHorizontalValue(canvas)
        }

    }

    private fun drawHorizontalValue(canvas: Canvas?) {
        listTextPoint.forEachIndexed { index, pointF ->
            var text = listValueHorizontal.get(index).toString()
            canvas!!.drawText(
                text,
                pointF.x - textHorzWidth / 2,
                pointF.y + textHorzWidth / 8,
                textColumnPaint
            )
        }
    }

    private fun drawTextPaint(canvas: Canvas?) {
        val textPaint = Paint().apply {
            textSize = 28f
            color = Color.parseColor("#66707A")
            style = Paint.Style.FILL
        }
        textHorzWidth = textPaint.measureText(maxValue.toString())
    }

    private var startPointDrawX = 0f
    private var endPointDrawX = 0f
    private var startPointDrawY = 0f
    private var endPointDrawY = 0f
    private fun drawLineHorizontal(canvas: Canvas?) {
        val paintLine = Paint().apply {
            strokeWidth = 2f
            style = Paint.Style.FILL
            color = Color.parseColor("#ECECEC")
        }
        var startX = textHorzWidth * 1.2f + 5f
        var height = canvas!!.height - (canvas.height * 0.15f)
        var width = canvas.width - startX / 2
        var startY = canvas.height - (canvas.height * 0.1f)
        listTextPoint.clear()
        for (i in 0..4) {
            listTextPoint.add(PointF(textHorzWidth, startY))
            canvas.drawLine(startX, startY, width, startY, paintLine)
            startY -= height / 4
        }
        startPointDrawX = startX
        endPointDrawX = width
        startPointDrawY = listTextPoint.get(0).y
        endPointDrawY = listTextPoint.get(4).y
    }

    fun setSelectToday(position: Int){
        this.position= position
        getPointByIndex(position){
            this.centerPointRect=it
        }
        invalidate()
    }

    private var listValueHorizontal = mutableListOf<Int>()

    // insert giá trị data
    fun setData(dataList: MutableList<Data>) {
        if (dataList.isEmpty()) return
        this.dataList.clear()
        this.dataList.addAll(dataList)

        if (dataList.isNotEmpty()) {
            maxValue = dataList.maxOf { it.value }
            minValue = dataList.minOf { it.value }
        }
        this.listValueHorizontal.clear()
        initMinMax(minValue, maxValue) {
            this.listValueHorizontal = it
        }
        listStringMax.clear()
        dataList.forEach { value ->
            listStringMax.add(value.valueUnit)
        }
        if(dataList.isEmpty()){
            position=-1
        }
        getPointByIndex(position){
            this.centerPointRect= it
        }

        invalidate()

    }

    fun getMaxLarget(maxValue: Float): Float {
        val remainder = maxValue % 5f
        return if (remainder == 0f) {
            maxValue + 5f
        } else {
            maxValue + (5f - remainder)
        }
    }


    private fun getMinLarget(minValue: Float): Float {
        var largestDivisibleBy5 = minValue - (minValue % 5f)
        if (largestDivisibleBy5 >= minValue) {
            largestDivisibleBy5 -= 5f
        }


        return largestDivisibleBy5
    }

    var min = 0f
    var max = 0f

    private fun initMinMax(minValue: Float, maxValue: Float, calback: (MutableList<Int>) -> Unit) {
        val listData = mutableListOf<Int>()
        this.min = getMinLarget(minValue)
        this.max = getMaxLarget(maxValue)
        var minvalloop = this.min
        val space = (this.max - minvalloop)
        for (i in 0..4) {
            listData.add(minvalloop.toInt())
            minvalloop += (space / 4)
        }
        calback(listData)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth = widthSize

        val finalWidth: Int
        val finalHeight: Int

        when {
            widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY -> {
                // Both width and height are specified, use them
                finalWidth = widthSize
                finalHeight = heightSize
            }
            widthMode == MeasureSpec.EXACTLY -> {
                // Only width is specified, use it and set height equal to width
                finalWidth = widthSize
                finalHeight = finalWidth
            }
            heightMode == MeasureSpec.EXACTLY -> {
                // Only height is specified, use it
                finalWidth = heightSize
                finalHeight = heightSize
            }
            else -> {
                // Neither width nor height is specified, use desired width as a fallback
                finalWidth = desiredWidth
                finalHeight = finalWidth
            }
        }
        setMeasuredDimension(finalWidth, finalHeight)
    }

    // data bao gồm : Value , value box (value + unit) , date
    data class Data(val value: Float, var valueUnit: String, var valueDate: String)
}

interface OnChartTouchEvent {
    fun onValue(value: Float, position: Int)
}
