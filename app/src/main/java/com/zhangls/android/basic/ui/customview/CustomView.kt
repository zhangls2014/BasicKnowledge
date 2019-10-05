package com.zhangls.android.basic.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.zhangls.android.basic.R
import com.zhangls.android.basic.util.dpToPx

/**
 * @author zhangls
 */
class CustomView : View {
  /**
   * 绘制内容的类型
   */
  private var drawType: DrawType = DrawType.Color
  private val defaultColor: Int by lazy {
    ContextCompat.getColor(context, R.color.colorPrimary)
  }
  private val circlePaint: Paint by lazy {
    Paint().apply {
      color = defaultColor
      strokeWidth = 4.dpToPx
    }
  }
  private val rectPaint: Paint by lazy {
    Paint().apply {
      color = defaultColor
      strokeWidth = 4.dpToPx
    }
  }
  private val pointPaint: Paint by lazy {
    Paint().apply {
      color = defaultColor
      strokeWidth = 16.dpToPx
    }
  }
  private val ovalPaint: Paint by lazy {
    Paint().apply {
      color = defaultColor
      strokeWidth = 4.dpToPx
    }
  }
  private val linePaint: Paint by lazy {
    Paint().apply {
      color = defaultColor
      strokeWidth = 24.dpToPx
      strokeCap = Paint.Cap.ROUND
    }
  }
  private val roundRectPaint: Paint by lazy {
    Paint().apply {
      color = defaultColor
      strokeWidth = 4.dpToPx
    }
  }
  private val arcPaint: Paint by lazy {
    Paint().apply {
      color = defaultColor
      strokeWidth = 4.dpToPx
    }
  }
  private val pathPaint: Paint by lazy {
    Paint().apply {
      color = defaultColor
      strokeWidth = 4.dpToPx
      strokeCap = Paint.Cap.ROUND
    }
  }


  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    canvas?.let {
      when (drawType) {
        DrawType.Color -> drawColor(it)
        DrawType.Circle -> drawCircle(it)
        DrawType.Rect -> drawRect(it)
        DrawType.Point -> drawPoint(it)
        DrawType.Oval -> drawOval(it)
        DrawType.Line -> drawLine(it)
        DrawType.RoundRect -> drawRoundRect(it)
        DrawType.Arc -> drawArc(it)
        DrawType.Path -> drawPath(it)
      }
    }
  }

  fun setDrawType(type: DrawType) {
    drawType = type
    invalidate()
  }

  private fun drawColor(canvas: Canvas) {
    canvas.drawColor(defaultColor)
  }

  private fun drawCircle(canvas: Canvas) {
    circlePaint.style = Paint.Style.STROKE
    canvas.drawCircle(200F, 200F, 100F, circlePaint)
    circlePaint.style = Paint.Style.FILL
    canvas.drawCircle(500F, 200F, 100F, circlePaint)
  }

  private fun drawRect(canvas: Canvas) {
    rectPaint.style = Paint.Style.STROKE
    canvas.drawRect(200F, 100F, 500F, 300F, rectPaint)
    rectPaint.style = Paint.Style.FILL
    canvas.drawRect(600F, 100F, 900F, 300F, rectPaint)
  }

  private fun drawPoint(canvas: Canvas) {
    pointPaint.strokeCap = Paint.Cap.BUTT
    canvas.drawPoint(100F, 100F, pointPaint)
    pointPaint.strokeCap = Paint.Cap.SQUARE
    canvas.drawPoint(200F, 100F, pointPaint)
    pointPaint.strokeCap = Paint.Cap.ROUND
    canvas.drawPoint(300F, 100F, pointPaint)
  }

  private fun drawOval(canvas: Canvas) {
    ovalPaint.style = Paint.Style.STROKE
    canvas.drawOval(200F, 100F, 500F, 300F, ovalPaint)
    ovalPaint.style = Paint.Style.FILL
    canvas.drawOval(600F, 100F, 900F, 300F, ovalPaint)
  }

  private fun drawLine(canvas: Canvas) {
    canvas.drawLine(100F, 100F, 500F, 400F, linePaint)
  }

  private fun drawRoundRect(canvas: Canvas) {
    roundRectPaint.style = Paint.Style.STROKE
    canvas.drawRoundRect(200F, 100F, 500F, 300F, 72F, 72F, roundRectPaint)
    roundRectPaint.style = Paint.Style.FILL
    canvas.drawRoundRect(600F, 100F, 900F, 300F, 36F, 72F, roundRectPaint)
  }

  private fun drawArc(canvas: Canvas) {
    arcPaint.style = Paint.Style.FILL
    canvas.drawArc(200F, 100F, 900F, 500F, 15F, 150F, true, arcPaint)
    canvas.drawArc(200F, 100F, 900F, 500F, 195F, 60F, false, arcPaint)
    arcPaint.style = Paint.Style.STROKE
    canvas.drawArc(200F, 100F, 900F, 500F, 285F, 60F, false, arcPaint)
  }

  private fun drawPath(canvas: Canvas) {
    val path = Path()
    path.addArc(100F, 100F, 400F, 400F, 150F, 210F)
    path.arcTo(400F, 100F, 700F, 400F, 180F, 210F, false)
    path.lineTo(400F, 700F)
    canvas.drawPath(path, pathPaint)
  }

  enum class DrawType {
    Color,
    Circle,
    Rect,
    Point,
    Oval,
    Line,
    RoundRect,
    Arc,
    Path
  }
}