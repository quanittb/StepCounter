package com.mobiai.views.beforeafter.chart.utility

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF
import android.view.MotionEvent
import com.mobiai.base.chart.ColumnChart


import kotlin.math.roundToInt


fun getMaxValue(value: Int): Int {
    var customVal = value
    if (value < 100) {
        customVal = value / 10
        return (customVal + 2) * 10
    } else if (100 <= value && value <= 1000) {
        customVal = value / 100
        return (customVal + 2) * 100
    } else if (value > 1000 && value <= 10000) {
        customVal = value / 1000
        return (customVal + 2) * 1000
    } else if (value > 10000) {
        customVal = value / 1000
        return (customVal + 2) * 1000

    }
    return customVal
}

fun listValueDraw(maxvalf: Float): ArrayList<Int> {
    var maxval = maxvalf
    var value_Def = 0
    if (maxval < 100) {
        maxval = maxvalf / 10
        value_Def = 10
    } else if (100 <= maxval && maxval <= 1000) {
        maxval = maxvalf / 100
        value_Def = 100
    } else {
        maxval = maxvalf / 1000
        value_Def = 1000
    }
    var maxvalAdd = 0f
    val returnvalue = ArrayList<Int>()
    returnvalue.add(0)
    for (i in 4 downTo 1) {
        maxvalAdd += maxval / 4
        maxvalAdd = roundToFirstDecimal(maxvalAdd.toDouble()).toFloat()
        if (maxvalAdd * value_Def > maxvalf) {
            maxvalAdd = maxvalf / value_Def
        }
        returnvalue.add((maxvalAdd * value_Def).toInt())
    }
    return returnvalue
}

fun roundToFirstDecimal(number: Double): Double {
    return (number * 10).roundToInt() / 10.0
}

fun getFirstValueDecimal(number: Double): Double {
    return (number * 10.0).toInt() / 10.0
}


fun Canvas.drawTriangle(
    borderPaint: Paint,
    firstPoint: PointF,
    secondPoint: PointF,
    threePoint: PointF
) {
    val shapePath = Path()
    shapePath.moveTo(firstPoint.x, firstPoint.y)
    shapePath.lineTo(secondPoint.x, secondPoint.y)
    shapePath.lineTo(threePoint.x, threePoint.y)
    shapePath.close()
    drawPath(shapePath, borderPaint)
}


fun Canvas.drawRoundRectPath(
    rectF: RectF,
    radius: Float,
    roundTopLeft: Boolean,
    roundTopRight: Boolean,
    roundBottomLeft: Boolean,
    roundBottomRight: Boolean,
    paint: Paint
) {
    val path = Path()
    if (roundBottomLeft) {
        path.moveTo(rectF.left, rectF.bottom - radius)
    } else {
        path.moveTo(rectF.left, rectF.bottom)
    }
    if (roundTopLeft) {
        path.lineTo(rectF.left, rectF.top + radius)
        path.quadTo(rectF.left, rectF.top, rectF.left + radius, rectF.top)
    } else {
        path.lineTo(rectF.left, rectF.top)
    }
    if (roundTopRight) {
        path.lineTo(rectF.right - radius, rectF.top)
        path.quadTo(rectF.right, rectF.top, rectF.right, rectF.top + radius)
    } else {
        path.lineTo(rectF.right, rectF.top)
    }
    if (roundBottomRight) {
        path.lineTo(rectF.right, rectF.bottom - radius)
        path.quadTo(rectF.right, rectF.bottom, rectF.right - radius, rectF.bottom)
    } else {
        path.lineTo(rectF.right, rectF.bottom)
    }
    if (roundBottomLeft) {
        path.lineTo(rectF.left + radius, rectF.bottom)
        path.quadTo(rectF.left, rectF.bottom, rectF.left, rectF.bottom - radius)
    } else {
        path.lineTo(rectF.left, rectF.bottom)
    }
    path.close()

    drawPath(path, paint)
}


fun spaceValues(list: List<Int>): List<Int> {
    val spaceValues = mutableListOf<Int>()
    for (i in 1 until list.size) {
        val space = list[i] - list[i - 1]
        spaceValues.add(space)
    }
    return spaceValues
}

fun Canvas.drawTriangle(
    borderPaint: Paint,
    firstPoint: Point,
    secondPoint: Point,
    threePoint: Point
) {
    val shapePath = Path()
    shapePath.moveTo(firstPoint.x.toFloat(), firstPoint.y.toFloat())
    shapePath.lineTo(secondPoint.x.toFloat(), secondPoint.y.toFloat())
    shapePath.lineTo(threePoint.x.toFloat(), threePoint.y.toFloat())
    shapePath.close()
    drawPath(shapePath, borderPaint)
}

fun getIndexByPoint(
    listRectData: java.util.ArrayList<RectF>,
    event: MotionEvent,
    calback: (Int, PointF) -> Unit
) {
    listRectData.forEachIndexed { index, rectF ->
        if (rectF.left <= event.x && rectF.right > event.x) {
            val pointX = rectF.left + (rectF.right - rectF.left) / 2
            var pointF = PointF(pointX, event.y)
            calback(index, pointF)

        }
    }
}

fun getPointByIndex(listRectData: java.util.ArrayList<RectF>, currentIndex: Int, calback: (PointF) -> Unit) {
    listRectData.forEachIndexed { index, rectF ->
        if (index == currentIndex) {
            val pointX = rectF.left + (rectF.right - rectF.left) / 2
            var pointF = PointF(pointX, 0f)
            calback(pointF)
        }
    }
}
