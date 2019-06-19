package com.rainview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable

data class RainDrop(private val centerX: Float, private val centerY: Float, private val maxRadius: Float, private val manual: Int) : Parcelable {

    var currentRadius = 1f
    var intersection : Boolean = false
    var distance: Double = 0.0
    var rainDropListManual: MutableList<RainDrop> = mutableListOf()

    constructor(parcel: Parcel) : this(
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readInt()
    ) {
        currentRadius = parcel.readFloat()
    }

    fun draw(canvas: Canvas, paint: Paint, widthRect: Int, heightRect: Int, rainDropListManual: MutableList<RainDrop>) {
        this.rainDropListManual = rainDropListManual
        intersection = false

        paint.apply { alpha = ((maxRadius - currentRadius) / maxRadius * MAX_ALPHA).toInt() }
        if (manual == 1) {
            paint.color = Color.RED
            paint.setStyle(Paint.Style.STROKE)
            paint.setStrokeWidth(4f)
        } else {
            paint.color = Color.DKGRAY
            paint.setStrokeWidth(1f)
        }

        //if manual ring found
        if (manual == 0) {
            for (item in rainDropListManual) {
                distance = calculateDistanceBetweenPointsWithHypot(item.centerX.toDouble(), item.centerY.toDouble(), centerX.toDouble(), centerY.toDouble()) + currentRadius
                if (distance <= 100) {
                    intersection = true
                    rainDropListManual.remove(item)
                    break;
                }
            }
        }
        if (!intersection) {
            canvas.drawCircle(centerX, centerY, currentRadius++, paint)
        }
    }

    fun isValid() = currentRadius < maxRadius

    fun getDropListManual() : MutableList<RainDrop> {
        return rainDropListManual;
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(centerX)
        parcel.writeFloat(centerY)
        parcel.writeFloat(maxRadius)
        parcel.writeFloat(currentRadius)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RainDrop> {
        private const val MAX_ALPHA = 255

        override fun createFromParcel(parcel: Parcel): RainDrop {
            return RainDrop(parcel)
        }

        override fun newArray(size: Int): Array<RainDrop?> {
            return arrayOfNulls(size)
        }
    }

    fun calculateDistanceBetweenPointsWithHypot(
            x1: Double,
            y1: Double,
            x2: Double,
            y2: Double): Double {

        val ac = Math.abs(y2 - y1)
        val cb = Math.abs(x2 - x1)

        return Math.hypot(ac, cb)
    }
}
