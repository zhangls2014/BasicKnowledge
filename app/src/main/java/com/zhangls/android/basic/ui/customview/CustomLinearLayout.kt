package com.zhangls.android.basic.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlin.math.ceil

/**
 * @author zhangls
 */
class CustomLinearLayout : LinearLayout {

  private var isOnDraw = false
  private var isDispatchDraw = false
  private val pattern by lazy { Pattern() }

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    setWillNotDraw(false)
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    if (isOnDraw) {
      canvas?.let {
        pattern.draw(it, width.toFloat(), height.toFloat())
      }
    }
  }

  override fun dispatchDraw(canvas: Canvas?) {
    super.dispatchDraw(canvas)

    if (isDispatchDraw) {
      canvas?.let {
        pattern.draw(it, width.toFloat(), height.toFloat())
      }
    }
  }

  fun setOnDrawOrder(isOnDraw: Boolean) {
    this.isOnDraw = isOnDraw
    invalidate()
  }

  fun setDispatchDrawOrder(isDispatchDraw: Boolean) {
    this.isDispatchDraw = isDispatchDraw
    invalidate()
  }


  private class Pattern {
    /**
     * 斑点数据（坐标、半径）
     */
    data class Spot(val relativeX: Float, val relativeY: Float, val relativeSize: Float)

    companion object {
      private const val PATTERN_RATIO = 5F / 6
    }

    private val patternPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      color = Color.parseColor("#A0E91E63")
    }
    private val spots: Array<Spot> = arrayOf(
        Spot(0.24F, 0.3F, 0.026F),
        Spot(0.69F, 0.25F, 0.067F),
        Spot(0.32F, 0.6F, 0.067F),
        Spot(0.62F, 0.78F, 0.083F)
    )

    fun draw(canvas: Canvas, width: Float, height: Float) {
      val repitition = ceil((width / height).toDouble()).toInt()
      for (i in 0 until spots.size * repitition) {
        val spot = spots[i % spots.size]
        canvas.drawCircle((i / spots.size).toFloat() * height * PATTERN_RATIO + spot.relativeX * height, spot.relativeY * height, spot.relativeSize * height, patternPaint)
      }
    }
  }
}