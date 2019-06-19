package com.rainview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class RainDropView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0) :
        View(context, attrs, defStyleAttr, defStyleRes) {

    private var xValue: Float = 0f
    private var yValue: Float = 0f
    private var rainDrops: Int = 0
    private var rainDrops2: Int = 0
    private var rainDrops3: Int = 0

    private val paint = Paint()
            .apply { color = Color.DKGRAY }
            .apply { style = Paint.Style.STROKE }
            .apply { isAntiAlias = true }
            .apply { strokeWidth = 1f }

    var rainDropList: List<RainDrop> = listOf()
    var rainDropListManual: MutableList<RainDrop> = mutableListOf()
    var widthScreen: Int = 0
    var heightScreen: Int = 0
    private val maxRadius by lazy {
        context.resources.getDimension(R.dimen.max_radius)
    }
    private val autoMaxRadius by lazy {
        context.resources.getDimension(R.dimen.auto_radius)
    }

    fun setWidth(value: Int) {
        widthScreen = value
    }

    fun setHeight(value: Int) {
        heightScreen = value
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom
        setMeasuredDimension(resolveSize(desiredWidth, widthMeasureSpec),
                resolveSize(desiredHeight, heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rainDropList.forEach {
            it.draw(canvas, paint, 0, 0, rainDropListManual)
            rainDropListManual = it.getDropListManual()
        }
        //reduce number of rain drops
        if (rainDrops % 10 == 0 && rainDrops2 % 5 == 0 && rainDrops3 % 2 == 0) {
            xValue = (0..widthScreen).shuffled().first().toFloat()
            yValue = (0..heightScreen).shuffled().first().toFloat()
            rainDropList += RainDrop(xValue, yValue, autoMaxRadius, 0)
            rainDrops = 0
            rainDrops2 = 0
            rainDrops3 = 0
        }
        rainDrops++
        rainDrops2++
        rainDrops3++

        if (rainDropListManual.isNotEmpty() && isAttachedToWindow) invalidate()

        rainDropList = rainDropList.filter { it.isValid() }
        if (rainDropList.isNotEmpty() && isAttachedToWindow) invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointerIndex = event.actionIndex
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> return true
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP -> {
                rainDropList += RainDrop(event.getX(pointerIndex), event.getY(pointerIndex), maxRadius, 1)
                rainDropListManual.add(RainDrop(event.getX(pointerIndex), event.getY(pointerIndex), maxRadius, 1))
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

}
