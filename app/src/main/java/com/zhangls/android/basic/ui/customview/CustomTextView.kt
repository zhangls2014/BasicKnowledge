package com.zhangls.android.basic.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.zhangls.android.basic.R
import com.zhangls.android.basic.util.dpToPx
import com.zhangls.android.basic.util.spToPx

/**
 * @author zhangls
 */
class CustomTextView : View {
  private var textType: TextType = TextType.DrawText
  private val defaultColor: Int by lazy {
    ContextCompat.getColor(context, R.color.colorPrimary)
  }
  private val textPaint: TextPaint by lazy {
    TextPaint().apply {
      textSize = 100F
      color = defaultColor
    }
  }
  private val typeface: Typeface by lazy {
    Typeface.createFromAsset(context.assets, "Satisfy-Regular.ttf")
  }


  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    canvas?.let {
      when (textType) {
        TextType.DrawText -> drawText(it)
        TextType.StaticLayout -> drawStaticLayout(it)
        TextType.SetTextSize -> drawTextSize(it)
        TextType.SetTypeface -> drawTypeface(it)
        TextType.SetFakeBoldText -> drawFakeBoldText(it)
        TextType.SetStrikeThruText -> drawStrikeThruText(it)
        TextType.SetUnderlineText -> drawUnderlineText(it)
        TextType.SetTextSkewX -> drawTextSkewX(it)
        TextType.SetTextScaleX -> drawTextScaleX(it)
        TextType.SetTextAlign -> drawTextAlign(it)
      }
    }
  }

  fun setTextType(type: TextType) {
    textType = type
    invalidate()
  }

  private fun drawText(canvas: Canvas) {
    textPaint.textSize = 120F
    textPaint.color = defaultColor
    textPaint.typeface = Typeface.DEFAULT
    canvas.drawText("Hello zhangls", 0F, 100F, textPaint)
  }

  /**
   * drawText 方法不能自动换行，采用 StaticLayout 方法即可实现智能换行，识别换行符
   */
  private fun drawStaticLayout(canvas: Canvas) {
    val text = "Hello\nzhangls"

    canvas.drawText(text, 0F, 100F, textPaint)
    canvas.save()
    canvas.translate(0F, 200F)

    @Suppress("DEPRECATION") val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      StaticLayout.Builder.obtain(text, 0, text.length, textPaint, 600).build()
    } else {
      StaticLayout(text, textPaint, 600, Layout.Alignment.ALIGN_NORMAL, 1F, 0F, true)
    }
    staticLayout.draw(canvas)
  }

  private fun drawTextSize(canvas: Canvas) {
    val text = "Hello zhangls"

    val x = 0F
    var y = 50F
    textPaint.textSize = 10.spToPx
    canvas.drawText(text, x, y, textPaint)

    y += 70
    textPaint.textSize = 20.spToPx
    canvas.drawText(text, x, y, textPaint)

    y += 100
    textPaint.textSize = 30.dpToPx
    canvas.drawText(text, x, y, textPaint)

    textPaint.textSize = 100F
  }

  private fun drawTypeface(canvas: Canvas) {
    val text = "Hello zhangls"

    val x = 0F
    var y = 100F
    textPaint.typeface = Typeface.DEFAULT
    canvas.drawText(text, x, y, textPaint)

    y += 100
    textPaint.typeface = Typeface.DEFAULT_BOLD
    canvas.drawText(text, x, y, textPaint)

    y += 100
    textPaint.typeface = typeface
    canvas.drawText(text, x, y, textPaint)

    y += 200
    textPaint.typeface = Typeface.SERIF
    canvas.drawText(text, x, y, textPaint)

    textPaint.typeface = Typeface.DEFAULT
  }

  /**
   * 伪粗体，即不会占用更多的绘制空间，它并不是通过选用更高 weight 的字体让文字变粗，而是通过程序在运行时把文字给「描粗」了。
   */
  private fun drawFakeBoldText(canvas: Canvas) {
    textPaint.textSize = 120F
    textPaint.color = defaultColor
    textPaint.typeface = Typeface.DEFAULT
    val text = "Hello zhangls"

    val x = 0F
    var y = 100F
    textPaint.isFakeBoldText = false
    canvas.drawText(text, x, y, textPaint)

    y += 100
    textPaint.isFakeBoldText = true
    canvas.drawText(text, x, y, textPaint)

    textPaint.isFakeBoldText = false
  }

  private fun drawStrikeThruText(canvas: Canvas) {
    val text = "Hello zhangls"

    val x = 0F
    var y = 100F
    textPaint.isStrikeThruText = false
    canvas.drawText(text, x, y, textPaint)

    y += 100
    textPaint.isStrikeThruText = true
    canvas.drawText(text, x, y, textPaint)

    textPaint.isStrikeThruText = false
  }

  private fun drawUnderlineText(canvas: Canvas) {
    val text = "Hello zhangls"

    val x = 0F
    var y = 100F
    textPaint.isUnderlineText = false
    canvas.drawText(text, x, y, textPaint)

    y += 100
    textPaint.isUnderlineText = true
    canvas.drawText(text, x, y, textPaint)

    textPaint.isUnderlineText = false
  }

  private fun drawTextSkewX(canvas: Canvas) {
    val text = "Hello zhangls"

    val x = 100F
    var y = 100F
    textPaint.textSkewX = 0.5F
    canvas.drawText(text, x, y, textPaint)

    y += 100
    textPaint.textSkewX = -0.7F
    canvas.drawText(text, x, y, textPaint)

    textPaint.textSkewX = 0F
  }

  private fun drawTextScaleX(canvas: Canvas) {
    val text = "Hello zhangls"

    val x = 0F
    var y = 100F
    textPaint.textScaleX = 1F
    canvas.drawText(text, x, y, textPaint)

    y += 100
    textPaint.textScaleX = 0.7F
    canvas.drawText(text, x, y, textPaint)

    y += 100
    textPaint.textScaleX = 1.5F
    canvas.drawText(text, x, y, textPaint)

    textPaint.textScaleX = 1F
  }

  private fun drawTextAlign(canvas: Canvas) {
    val text = "Hello zhangls"

    val x = width / 2F
    var y = 100F
    textPaint.textAlign = Paint.Align.LEFT
    canvas.drawText(text, x, y, textPaint)

    y += 100
    textPaint.textAlign = Paint.Align.RIGHT
    canvas.drawText(text, x, y, textPaint)

    y += 100
    textPaint.textAlign = Paint.Align.CENTER
    canvas.drawText(text, x, y, textPaint)
  }

  enum class TextType {
    DrawText,
    StaticLayout,
    SetTextSize,
    SetTypeface,
    SetFakeBoldText,
    SetStrikeThruText,
    SetUnderlineText,
    SetTextSkewX,
    SetTextScaleX,
    SetTextAlign
  }
}