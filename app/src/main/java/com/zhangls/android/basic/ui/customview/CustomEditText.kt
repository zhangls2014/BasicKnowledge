package com.zhangls.android.basic.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

/**
 * @author zhangls
 */
class CustomEditText : AppCompatEditText {

  private var isOnDrawBefore: Boolean = false
  private var isDrawBefore: Boolean = false
  private val bounds: RectF by lazy { RectF() }
  private val paint: Paint by lazy {
    Paint().apply {
      color = ContextCompat.getColor(context, android.R.color.holo_orange_light)
    }
  }


  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


  override fun onDraw(canvas: Canvas?) {
    if (isOnDrawBefore) {
      bounds.left = layout.getLineLeft(0)
      bounds.top = layout.getLineTop(0).toFloat()
      bounds.right = layout.getLineRight(0)
      bounds.bottom = layout.getLineBottom(0).toFloat()
      canvas?.drawRect(bounds, paint)
    }

    super.onDraw(canvas)
  }

  override fun draw(canvas: Canvas?) {
    if (isDrawBefore) {
      canvas?.drawColor(paint.color)
    }
    super.draw(canvas)
  }

  fun setOnDrawOrder(isBefore: Boolean) {
    isOnDrawBefore = isBefore
    isDrawBefore = false

    invalidate()
  }

  fun setDrawOrder(isBefore: Boolean) {
    isDrawBefore = isBefore
    isOnDrawBefore = false

    invalidate()
  }
}