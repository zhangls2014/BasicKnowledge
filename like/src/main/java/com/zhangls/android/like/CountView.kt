package com.zhangls.android.like

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import kotlin.math.abs
import kotlin.math.roundToInt


/**
 * 计数 View
 *
 * @author zhangls
 */
class CountView : View {
  // 默认计数
  private val DEFAULT_COUNT: Int = 0
  // 默认文字字号
  private val DEFAULT_TEXT_SIZE: Float = 14.spToPx
  // 默认文字颜色
  private val DEFAULT_TEXT_COLOR: Int = ContextCompat.getColor(context, R.color.text_color_gray)
  // 默认数字偏移量
  private val DEFAULT_COUNT_OFFSET: Int = 8.dpToPxInt
  // 计数
  private var count = DEFAULT_COUNT
  // 字号
  private var textSize: Float = DEFAULT_TEXT_SIZE
  // 颜色
  private var textColor: Int = DEFAULT_TEXT_COLOR
  // 数字偏移量
  private var countOffset: Int = DEFAULT_COUNT_OFFSET
  // 文字 TextPaint
  private val countPaint: TextPaint by lazy { TextPaint() }
  // 文字变化数组，index 0 表示未变化的字符，1 表示变化前的字符，2 表示变化后的字符
  private val texts = arrayOf(count.toString(), "", "")


  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    init(context, attrs)
  }

  private fun init(context: Context, attrs: AttributeSet?) {
    // 初始化属性
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountView)
    count = typedArray.getInt(R.styleable.CountView_cv_count, DEFAULT_COUNT)
    textSize = typedArray.getDimension(R.styleable.CountView_cv_textSize, DEFAULT_TEXT_SIZE)
    textColor = typedArray.getColor(R.styleable.CountView_cv_textColor, DEFAULT_TEXT_COLOR)
    typedArray.recycle()

    // 应用属性
    countPaint.let {
      it.color = textColor
      it.textSize = textSize
    }

    calculateNumChange(0)
  }

  /**
   * 计算数值变化
   *
   * 计算方法：从左往右开始对比，相同数字代表不变，从第一次开始不相同的数字表示之后的数字都不同
   *
   * @param change 数值变化量
   */
  private fun calculateNumChange(change: Int) {
    val oldCount = count.toString()
    val newCount = (count + change).toString()

    for (index in oldCount.indices) {
      if (index < newCount.length) {
        // 新的数字包含 index 所指的位
        if (oldCount[index] != newCount[index]) {
          texts[0] = if (index == 0) "" else oldCount.substring(0, index)
          texts[1] = oldCount.substring(index)
          texts[2] = newCount.substring(index)
          break
        }
      } else {
        texts[0] = if (index == 0) "" else oldCount.substring(0, index)
        texts[1] = oldCount.substring(index)
        texts[2] = newCount.substring(index)
        break
      }
    }

    if (change != 0) {
      // 数字发生了变化，则执行数字更迭动画
      startAnimation(newCount.toInt() > oldCount.toInt())
    }
  }

  /**
   * @param toBig 新的数字更大
   */
  private fun startAnimation(toBig: Boolean) {
    val countHeight = countPaint.fontMetrics.bottom - countPaint.fontMetrics.top
    val endValue = if (toBig) {
      -countHeight.toInt()
    } else {
      countHeight.toInt()
    }
    val animator = ObjectAnimator.ofInt(this, "countOffset", 0, endValue)
    animator.duration = 300
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.start()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    val needWidth = paddingStart + countPaint.measureText(count.toString()).toInt() + paddingEnd
    val countHeight = countPaint.fontMetrics.bottom - countPaint.fontMetrics.top
    val needHeight = paddingTop + countHeight.toInt() * 3 + paddingBottom

    val measureWidth = resolveSize(needWidth, widthMeasureSpec)
    val measureHeight = resolveSize(needHeight, heightMeasureSpec)

    setMeasuredDimension(measureWidth, measureHeight)
  }

  override fun onDraw(canvas: Canvas?) {
    canvas?.let {
      // 未变化数字
      val x = paddingStart
      val countHeight = countPaint.fontMetrics.bottom - countPaint.fontMetrics.top
      val y = paddingTop + countHeight + -countPaint.fontMetrics.top
      countPaint.color = textColor
      it.drawText(texts[0], x.toFloat(), y, countPaint)
      // 变化数字
      val startX = paddingStart + countPaint.measureText(texts[0])
      if (countOffset > 0) {
        // 数字变小
        countPaint.color = getTextColor(countOffset.toFloat())
        it.drawText(texts[1], startX, y + countOffset, countPaint)
        countPaint.color = getTextColor(countOffset - countHeight)
        it.drawText(texts[2], startX, y - countHeight + countOffset, countPaint)
      } else {
        // 数字变大
        countPaint.color = getTextColor(countHeight + countOffset)
        it.drawText(texts[1], startX, y + countOffset, countPaint)
        countPaint.color = getTextColor(countOffset.toFloat())
        it.drawText(texts[2], startX, y + countHeight + countOffset, countPaint)
      }
    }
  }

  private fun getTextColor(offset: Float): Int {
    val countHeight = countPaint.fontMetrics.bottom - countPaint.fontMetrics.top
    val alpha = abs(offset) / countHeight * 255
    return Color.argb(alpha.roundToInt(), Color.red(textColor), Color.green(textColor), Color.blue(textColor))
  }

  /**
   * 属性动画必须的 set 方法：countOffset
   */
  @SuppressWarnings("unused")
  private fun setCountOffset(offset: Int) {
    this.countOffset = offset

    postInvalidate()
  }

  /**
   * 属性动画必须的 get 方法：countOffset
   */
  @SuppressWarnings("unused")
  private fun getCountOffset(): Int {
    return countOffset
  }

  fun setCount(count: Int) {
    calculateNumChange(count - this.count)
    this.count = count

    requestLayout()
  }

  fun getCount(): Int = count

  fun setTextSize(textSizeDp: Float) {
    this.textSize = textSizeDp.dpToPx
    countPaint.textSize = textSize

    requestLayout()
  }

  fun setTextColor(textColor: Int) {
    this.textColor = textColor
    countPaint.color = textColor

    postInvalidate()
  }
}