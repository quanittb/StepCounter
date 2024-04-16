package com.mobiai.base.chart

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.example.quanpham.R
import com.example.quanpham.utility.logD
import com.mobiai.views.beforeafter.chart.utility.drawRoundRectPath
import com.mobiai.views.beforeafter.chart.utility.drawTriangle


class ColumnChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    data class Column(var value: Double, var valueString: String, var dateString: String)

    private var listColumn = ArrayList<Column>()
    var listHorizontal = ArrayList<String>()
    private var columRadius = 0f
    private var columnSelected = 0
    private var textVerticalColor = Color.parseColor("#434E58")
    private var textHorizontalColor = Color.parseColor("#434E58")
    private var textHorizontalSize = 30f
    private var textVerticalSize = 28f

    private var textTopBox = 24f
    private var textBottomBox = 20f
    private var columnDefaultColor = Color.parseColor("#E3E9ED")
    private var columnSelectColor = Color.parseColor("#0CB809")
    private var columnPaintDef: Paint
    private var columnPaintSel: Paint
    private val typedArray: TypedArray
    private var istouchColumn = false

    init {
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomColumChart)
        columnDefaultColor = typedArray.getColor(
            R.styleable.CustomColumChart_defaultColor,
            Color.parseColor("#E3E9ED")
        )
        columnSelectColor = typedArray.getColor(
            R.styleable.CustomColumChart_selectedColor,
            Color.parseColor("#0CB809")
        )
        columRadius =
            typedArray.getDimension(R.styleable.CustomColumChart_columnRadius, 0f)
        textVerticalColor = typedArray.getColor(
            R.styleable.CustomColumChart_textVerticalColor,
            Color.parseColor("#434E58")
        )
        textHorizontalColor = typedArray.getColor(
            R.styleable.CustomColumChart_textHorizontalColor,
            Color.parseColor("#434E58")
        )
        textHorizontalSize = typedArray.getInt(
            R.styleable.CustomColumChart_textHorizontalSize, 30
        ).toFloat()
        textVerticalSize = typedArray.getInt(
            R.styleable.CustomColumChart_textVerticalSize, 28
        ).toFloat()
        textTopBox =
            typedArray.getDimension(R.styleable.CustomColumChart_textTopBoxSize, 24f)
        textBottomBox =
            typedArray.getDimension(R.styleable.CustomColumChart_textTopBoxSize, 20f)
        columnPaintDef = Paint().apply {
            strokeWidth = 5f
            style = Paint.Style.FILL
            color = columnDefaultColor
        }
        columnPaintSel = Paint().apply {
            strokeWidth = 5f
            style = Paint.Style.FILL
            color = columnSelectColor
        }
        typedArray.recycle()
    }

    private var centerPointRect = PointF(0f, 0f)
    private lateinit var listShapeColumn: ArrayList<RectF>
    fun setColumnSelected(column: Int) {
        this.columnSelected = column
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                istouchColumn = true
                getIndexByPoint(event) { index, currentPoint ->
                    columnSelected = index
                    centerPointRect = currentPoint
                }
                invalidate()
                onTouchCallback?.onValue(
                    listColumn.get(columnSelected).value.toFloat(),
                    columnSelected
                )
            }
        }
        invalidate()
        return true
    }

    fun setData(arr: ArrayList<Column>) {
        this.listColumn.clear()
        this.listColumn.addAll(arr)
//        if(this.listColumn.isNotEmpty())
            initMaxMin(this.listColumn)
        listShapeColumn = ArrayList()
        getListShapeColumn(this.listColumn) {
            this.listShapeColumn.clear()
            this.listShapeColumn.addAll(it)
        }
        invalidate()
    }

    private var arrValue = arrayListOf(
        0,
        1000,
        10000,
        20000,
        50000,
        100000,
        200000,
        500000
    )
    private var listHorizonVal = ArrayList<Int>()
    private var maxValue = 0.0
    private var minValue = 0.0
    private var maxLeftValue = 0
    private fun initMaxMin(listColumn: ArrayList<Column>) {
        var currentIndex = 0
        if (listColumn.isNotEmpty()) {
            this.minValue = listColumn.minOf { it.value }
            this.maxValue = if(listColumn.maxOf { it.value } > 0) listColumn.maxOf { it.value } else 0.0
            this.listHorizonVal.clear()
        }
        for (index in 0 until arrValue.lastIndex) {
            val currentValue = arrValue[index]
            maxLeftValue = currentValue

            currentIndex = index
            if (currentValue > maxValue) {
                break
            }
            maxLeftValue = arrValue[index + 1]
        }
        if(listColumn.isEmpty()){
            maxLeftValue = 1000
        }
            listHorizonVal.add(0)
            listHorizonVal.add((maxLeftValue * 0.2).toInt())
            listHorizonVal.add((maxLeftValue * 0.4).toInt())
            listHorizonVal.add((maxLeftValue * 0.6).toInt())
            listHorizonVal.add((maxLeftValue * 0.8).toInt())
            listHorizonVal.add(maxLeftValue)
    }

    var textHorzWidth = 0f
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawTextPaint(canvas)
        drawLineHorizontal(canvas!!, columnSelected)
//        drawLeftValue(canvas)
        drawHorizontalValue(canvas)
        if (isShowTarget) {
            drawTargetValue(canvas)
        }
    }

    private fun drawTargetValue(canvas: Canvas) {
        val boxWidth = 0.5f * startPointDrawX
        val boxheight = 0.5f * boxWidth
        val scale = (targetValue.toFloat() / maxLeftValue.toFloat())
        var startY = startPointDrawY - (startPointDrawY - endPointDrawY) * scale - boxheight / 2
        var startX = 2f
        var rectF = RectF(startX, startY, startX + boxWidth, startY + boxheight)

//        canvas.drawRect(rectF, columnPaintSel)
        val textpaint2 = textPaint.apply {
            textSize = 16f
            color = Color.WHITE
        }
        val textX =
            rectF.left + (rectF.width() - textpaint2.measureText(targetValue.toString())) / 2
        val textY = rectF.top + (rectF.height() + textpaint2.textSize) * 0.45f
//        canvas.drawText(targetValue.toString(), textX, textY, textpaint2)
        canvas.drawTriangle(
            columnPaintSel, PointF(startPointDrawX, startY),
            PointF(startPointDrawX + boxWidth, (startY + boxheight / 2)), PointF(startPointDrawX, startY + boxheight)
        )
        canvas.drawLine(
            startPointDrawX + boxWidth,
            startY + boxheight / 2,
            endPointDrawX,
            startY + boxheight / 2,
            borderPaint
        )

    }

    private fun drawLeftValue(canvas: Canvas) {
        val spaces = (startPointDrawY - endPointDrawY) / (listHorizonVal.size - 1)
        var startY = startPointDrawY + 6f
        var textPaintValue = textPaint.apply {
            textSize = 30f
            color= Color.parseColor("#434E58")
        }
        listHorizonVal.forEach { value ->
            var endValue = "0"
            if (value == 0) {
                endValue = "0"
            } else if (value in 1..999) {
                endValue = value.toString()
            } else {
                endValue = (value / 1000).toString() + "k"
            }

            canvas.drawText(endValue, 12f, startY, textPaintValue)
            startY -= spaces - 6f
        }
    }
    private fun drawHorizontalValue(canvas: Canvas) {
        val spaces = (startPointDrawY - endPointDrawY) / 5
        var startY = startPointDrawY + 6f
        var textPaintValue = textPaint.apply {
            textSize = 30f
            color= Color.parseColor("#434E58")
        }
        listHorizonVal.forEachIndexed { index, value ->
            if(index > 5) return
            var endValue = "0"
            if (value in 0..999) {
                endValue = value.toString()
            } else {
                endValue = (value / 1000).toString() + "k"
            }
            canvas.drawText(endValue, 12f, startY, textPaintValue)
            startY -= spaces - 6f
        }
    }
    private var isShowTarget = false
    private var targetValue = 0.0

    fun setTargetValue(ishow: Boolean, value: Double) {
        this.isShowTarget = ishow
        this.targetValue = value
        invalidate()
    }

    private fun getListShapeColumn(
        listColumn: ArrayList<Column>,
        calback: (ArrayList<RectF>) -> Unit
    ) {
        var listRectF = ArrayList<RectF>()
        var width = (endPointDrawX - startPointDrawX)
        val height = (startPointDrawY - endPointDrawY)
        val maxValue = listHorizonVal.lastOrNull()?.toDouble() ?: 1.0
        var defaultSpace = width / (listColumn.size * 2)
        width -= defaultSpace
        defaultSpace = width / (listColumn.size * 2)
        var collunmHeight: Float
        var startX = startPointDrawX
        listColumn.forEachIndexed { index, column ->
            val score = column.value.toFloat() / maxValue
            collunmHeight = (height * score).toFloat()
            var left = startX + defaultSpace
            var right = left + defaultSpace
            var rectF: RectF
            if (listColumn.size == 7) {
                rectF = RectF(
                    left,
                    startPointDrawY - collunmHeight,
                    right - (right - left) * 0.2f,
                    startPointDrawY
                )
            } else {
                rectF = RectF(left, startPointDrawY - collunmHeight, right, startPointDrawY)
            }
            listRectF.add(rectF)
            startX = right
        }
        calback(listRectF)
        getPointByIndex(columnSelected) {
            centerPointRect = it
        }
    }

    private val borderPaint = Paint().apply {
        color = Color.parseColor("#9CA4AB")
        style = Paint.Style.STROKE
        strokeWidth = 2f
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
    }

    private fun drawColumn(canvas: Canvas, columnSelected: Int) {
        getListShapeColumn(listColumn) {
            listShapeColumn = it
        }
        listShapeColumn.forEachIndexed { index, rectF ->
            if (rectF.height() > 0) {
                if (rectF.height() < 5f) {
                    var rectF = RectF(rectF.left, rectF.top - 8f, rectF.right, rectF.bottom)
                    canvas.drawRoundRectPath(
                        rectF, if (columRadius == 0f) rectF.width() / 2 else columRadius,
                        true, true, true,
                        true, if (index == columnSelected) columnPaintSel else columnPaintDef
                    )
                } else {
                    canvas.drawRoundRectPath(
                        rectF, if (columRadius == 0f) rectF.width() / 2 else columRadius,
                        true, true, true,
                        true, if (index == columnSelected) columnPaintSel else columnPaintDef
                    )
                }

            }

        }
        if (columnSelected < listColumn.size && columnSelected >= 0) {
            drawBoxValue(listShapeColumn, canvas, columnSelected)
        }
        drawHorizontal(canvas)
    }

    private fun drawBoxValue(
        listShapeColumn: ArrayList<RectF>,
        canvas: Canvas,
        columnSelected: Int
    ) {
        val data = listColumn.get(columnSelected)
        val textLine1 = data.valueString
        val textLine2 = data.dateString
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
        val boxHeight = height * 0.13f

        val boxWidth = if(textPaint1.measureText(textLine1) < textPaint2.measureText(textLine2)) textPaint2.measureText(textLine2)*1.8f
                        else textPaint1.measureText(textLine1)*1.8f
        val top = height * 0.015f
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
        val rectF = RectF(startPoint, top, startPoint + boxWidth, boxHeight)
        canvas.drawRoundRectPath(
            rectF, rectF.height() / 5,
            true, true, true, true, shapePaint
        )
        val recDraw = listShapeColumn.get(columnSelected)
        val bottomPoint = recDraw.top - 10f
        canvas.drawLine(
            centerPointRect.x,
            boxHeight + 25f,
            centerPointRect.x,
            bottomPoint,
            borderPaint
        )


        val shapeHeight = boxHeight
        val textX = rectF.centerX()
        val textY1 = rectF.centerY() - shapeHeight / 12
        val textY2 = rectF.centerY() + shapeHeight / 4
        canvas.drawText(textLine1, textX, textY1 + 4f, textPaint1)
        canvas.drawText(textLine2, textX, textY2 + 5f, textPaint2)

        drawTriangle(canvas, startPoint, rectF, boxWidth, shapePaint)
    }

    private var onTouchCallback: OnChartTouchEvent? = null

    fun getData(onChartTouchEvent: OnChartTouchEvent) {
        onTouchCallback = onChartTouchEvent
    }

    private fun drawTriangle(
        canvas: Canvas,
        startPoint: Float,
        rectF: RectF,
        boxWidth: Float,
        shapePaint: Paint
    ) {
        var firstPoint = PointF(0f, rectF.bottom)
        var secondPoint = PointF(0f, rectF.bottom)
        var threePoint = PointF(0f, rectF.bottom + 16f)

        if (rectF.left <= startPointDrawX) {
            firstPoint = PointF(centerPointRect.x - 10f, rectF.bottom - 2f)
            secondPoint = PointF(centerPointRect.x + 10f, rectF.bottom - 2f)
            threePoint = PointF(centerPointRect.x, rectF.bottom + 18f)
        } else if (startPoint + boxWidth > endPointDrawX) {
            firstPoint = PointF(startPoint + boxWidth - 40f, rectF.bottom - 2f)
            secondPoint = PointF(startPoint + boxWidth - 20f, rectF.bottom - 2f)
            threePoint = PointF(centerPointRect.x, rectF.bottom + 18f)
        } else {
            getPointByIndex(columnSelected) { pointF ->
                firstPoint = PointF(pointF.x - 10f, rectF.bottom)
                secondPoint = PointF(pointF.x + 10f, rectF.bottom)
                threePoint = PointF(pointF.x, rectF.bottom + 12f)
            }
        }
        canvas.drawTriangle(shapePaint, firstPoint, secondPoint, threePoint)
    }

    private var startPointDrawX = 0f
    private var endPointDrawX = 0f
    private var startPointDrawY = 0f
    private var endPointDrawY = 0f

    var textPaint = Paint()
    private fun drawTextPaint(canvas: Canvas?) {
        textPaint = Paint().apply {
            textSize = textVerticalSize
            color = Color.parseColor("#434E58")
            style = Paint.Style.FILL
        }
        textHorzWidth = textPaint.measureText("50 k")
    }

    private fun drawHorizontal(canvas: Canvas) {
        var canvasWidth = canvas.width.toFloat()
        val textPaints = Paint().apply {
            color = textHorizontalColor
            textSize = 32f
            style = Paint.Style.FILL
        }
        var startX = startPointDrawX
        canvasWidth = canvasWidth - startX
        val totalItems = listHorizontal.size
        val sectionWidth = canvasWidth / (totalItems * 2)
        startX = startX - sectionWidth / 1.5f
        val yPosition = height - (height - startPointDrawY) * 0.25f
        for ((index, value) in listHorizontal.withIndex()) {
            if (listHorizontal.size == listShapeColumn.size) {
                getPointByIndex(index) {
                    canvas.drawText(
                        value,
                        it.x - textPaints.measureText(value) / 2,
                        yPosition,
                        if (columnSelected == index) textPaints.apply {
                            typeface = Typeface.DEFAULT_BOLD
                            color = columnSelectColor
                        } else textPaints.apply {
                            typeface = Typeface.DEFAULT
                            color = Color.parseColor("#9CA4AB")
                        }
                    )
                }
            } else {
                if(index==0){
                    startX= startX+ textPaint.measureText(listHorizontal.get(0))*0.8f
                }
                else{
                    startX=startX
                }
                val textDrawPaint= textPaint
                val left = startX + sectionWidth
                canvas.drawText(
                    value,
                    left - textDrawPaint.measureText(value) *1.25f,
                    yPosition,
                    textDrawPaint.apply {
                        color = Color.parseColor("#9CA4AB")
                    }
                )
                startX = left + sectionWidth
            }
        }
    }

    private fun drawLineHorizontal(canvas: Canvas, columnSelected: Int) {
        val paintLine = Paint().apply {
            strokeWidth = 2f
            style = Paint.Style.FILL
            color = Color.parseColor("#ECECEC")
        }
        var startX = textHorzWidth * 1.1f + 5f

        var width = canvas.width - startX / 3
        var startY = canvas.height - (canvas.height * 0.1f)
        canvas.drawLine(startX, startY, width, startY, paintLine)
        startPointDrawX = startX
        endPointDrawX = width
        startPointDrawY = startY
        endPointDrawY = startPointDrawY - height * 0.7f
        drawColumn(canvas, columnSelected)
    }

    fun getStringMax(listString: ArrayList<String>): String {
        var stringval = listString.get(0)
        listString.forEach { item ->
            if (item.length > stringval.length) {
                stringval = item
            }
        }
        return stringval
    }

    fun getIndexByPoint(
        event: MotionEvent,
        calback: (Int, PointF) -> Unit
    ) {
        listShapeColumn.forEachIndexed { index, rectF ->
            if (rectF.left <= event.x && rectF.right > event.x) {
                val pointX = rectF.left + (rectF.right - rectF.left) / 2
                var pointF = PointF(pointX, event.y)
                calback(index, pointF)
            }
        }
    }

    fun getPointByIndex(currentIndex: Int, calback: (PointF) -> Unit) {
        listShapeColumn.forEachIndexed { index, rectF ->
            if (index == currentIndex) {
                val pointX = rectF.left + (rectF.right - rectF.left) / 2
                var pointF = PointF(pointX, 0f)
                calback(pointF)
            }
        }
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
}

