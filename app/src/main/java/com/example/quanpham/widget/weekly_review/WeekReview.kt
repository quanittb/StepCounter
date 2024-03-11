package com.example.quanpham.widget.weekly_review
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.min
import com.example.quanpham.R

class WeekReview
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var backgroundPaint: Paint = Paint()
    private var rectangle: RectF? = null
    private var margin: Float
    private var arcProportionValue: Float = 0f
    private var strokeColor = Color.parseColor("#E3E9ED")
    private var strokewidth = 24f
    private var bitmapStar:Bitmap

    init {
        margin = 6f
        bitmapStar= BitmapFactory.decodeResource(context.resources, R.drawable.img_star_full)
        setArcValue(75f)
    }

    fun setStrokeWidth(value:Float){
        this.strokewidth=value
        invalidate()
    }

    val arcProportionBg=1.0f
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth = widthSize // Use the full available width
        val desiredHeight = (desiredWidth * 1f).toInt() // Set the height to 3/4 of the width

        val finalWidth: Int
        val finalHeight: Int

        when (widthMode) {
            MeasureSpec.EXACTLY -> finalWidth = widthSize
            MeasureSpec.AT_MOST -> finalWidth = Integer.min(desiredWidth, widthSize)
            else -> finalWidth = desiredWidth
        }

        when (heightMode) {
            MeasureSpec.EXACTLY -> finalHeight = heightSize
            MeasureSpec.AT_MOST -> finalHeight = Integer.min(desiredHeight, heightSize)
            else -> finalHeight = desiredHeight
        }

        setMeasuredDimension(finalWidth, finalHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        backgroundPaint = Paint().apply {
            isAntiAlias = true
            color = strokeColor
            strokeCap = Paint.Cap.ROUND
            strokeWidth = strokewidth
            style = Paint.Style.STROKE
        }
        if (rectangle == null) {
            val centerX = canvas.width / 2f
            val centerY = canvas.height / 2f // Use canvas.height to ensure the circle is centered vertically
            val rectSize = min(canvas.width, canvas.height) - strokewidth
            val rectLeft = centerX - rectSize / 2
            val rectTop = centerY - rectSize / 2
            val rectRight = centerX + rectSize / 2
            val rectBottom = centerY + rectSize / 2
            rectangle = RectF(rectLeft, rectTop, rectRight, rectBottom)
        }
        val totalSweepAngle = 360f * arcProportionBg
        val startAngle = 0f
        drawBackground(canvas, totalSweepAngle, startAngle)
        val paintColor = Paint().apply {
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            strokeWidth = strokewidth
            style = Paint.Style.STROKE
            color = Color.parseColor("#98CD27")
        }
        val totalSweepAnglePaint = 360f * arcProportionValue
        val startAnglePaint = 270f
        drawPaintValue(canvas, totalSweepAnglePaint, startAnglePaint, paintColor)
        if(isDrawStar){
           drawStar(canvas)
        }
    }

    private fun drawStar(canvas: Canvas) {

        val bitmapSize = min(rectangle!!.width(), rectangle!!.height())+strokewidth
        val left = (rectangle!!.centerX() - bitmapSize / 2)
        val top = (rectangle!!.centerY() - bitmapSize / 2)
        val right = (rectangle!!.centerX() + bitmapSize / 2)
        val bottom = (rectangle!!.centerY() + bitmapSize / 2)
        val destRect = RectF(left, top, right, bottom)
        canvas.drawBitmap(bitmapStar, null, destRect, null)
    }

    private var isDrawStar= false
    private fun drawPaintValue(
        canvas: Canvas,
        totalSweepAngle: Float,
        startAngle: Float,
        paintColor: Paint
    ) {

        canvas.drawArc(rectangle!!, startAngle, totalSweepAngle, false, paintColor)
    }

    private fun drawBackground(canvas: Canvas, totalSweepAngle: Float, startAngle: Float) {
        canvas.drawArc(rectangle!!, startAngle, totalSweepAngle, false, backgroundPaint)
    }
    fun setArcValue(value: Float) {
        val spaceDraw = value/100f
        if(spaceDraw>= 1f){
            this.arcProportionValue =0f
            isDrawStar=true
        }
        else{
            isDrawStar=false
            this.arcProportionValue =spaceDraw
        }

        invalidate()
    }

}