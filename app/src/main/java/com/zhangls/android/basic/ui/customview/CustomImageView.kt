package com.zhangls.android.basic.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.withMatrix
import com.zhangls.android.basic.R
import com.zhangls.android.basic.util.spToPx

/**
 * @author zhangls
 */
class CustomImageView : AppCompatImageView {
  private var isOnDrawAfter: Boolean = false
  private var isOnDrawForegroundBefore: Boolean = false
  private var isOnDrawForegroundAfter: Boolean = false
  private var isDrawAfter: Boolean = false
  private val foregroundPaint: TextPaint by lazy {
    TextPaint().apply {
      textSize = 24.spToPx
    }
  }
  private val textBounds: Rect by lazy { Rect() }

  private val textPaint: TextPaint by lazy {
    TextPaint().apply {
      color = ContextCompat.getColor(context, R.color.colorAccent)
      textSize = 14F
    }
  }

  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)


  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    if (isOnDrawAfter) {
      drawable?.let {
        val bounds = it.bounds
        canvas?.withMatrix(imageMatrix) {
          val text = "尺寸：${bounds.width()}x${bounds.height()}"
          textPaint.getTextBounds(text, 0, text.length, textBounds)
          drawText(text, 20F + bounds.left, 20F + bounds.top, textPaint)
        }
      }
    }
  }

  override fun onDrawForeground(canvas: Canvas?) {
    if (isOnDrawForegroundBefore) {
      canvas?.let { drawNewTag(it) }
    }
    super.onDrawForeground(canvas)
    if (isOnDrawForegroundAfter) {
      canvas?.let { drawNewTag(it) }
    }
  }

  private fun drawNewTag(canvas: Canvas) {
    foregroundPaint.color = Color.RED
    canvas.drawRect(0F, 40F, 200F, 120F, foregroundPaint)
    foregroundPaint.color = Color.WHITE
    canvas.drawText("New", 20F, 100F, foregroundPaint)
  }

  override fun draw(canvas: Canvas?) {
    super.draw(canvas)
    if (isDrawAfter) {
      canvas?.let { drawNewTag(it) }
    }
  }

  fun setOnDrawOrder(isAfter: Boolean) {
    isOnDrawAfter = isAfter

    isOnDrawForegroundAfter = false
    isOnDrawForegroundBefore = false
    isDrawAfter = false

    invalidate()
  }

  fun setOnDrawForegroundOrder(isAfter: Boolean, isBefore: Boolean) {
    isOnDrawForegroundBefore = isBefore
    isOnDrawForegroundAfter = isAfter

    isOnDrawAfter = false
    isDrawAfter = false

    if (isAfter || isBefore) {
      foreground = ColorDrawable().apply {
        color = Color.parseColor("#88000000")
      }
    } else {
      foreground = ColorDrawable().apply {
        color = Color.TRANSPARENT
      }
    }

    invalidate()
  }

  fun setDrawOrder(isAfter: Boolean, isBefore: Boolean) {
    isDrawAfter = isAfter

    isOnDrawAfter = false
    isOnDrawForegroundAfter = false
    isOnDrawForegroundBefore = false

    if (isAfter || isBefore) {
      foreground = ColorDrawable().apply {
        color = Color.parseColor("#88000000")
      }
    } else {
      foreground = ColorDrawable().apply {
        color = Color.TRANSPARENT
      }
    }

    invalidate()
  }
}