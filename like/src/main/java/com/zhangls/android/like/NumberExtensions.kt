package com.zhangls.android.like

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.IntRange
import kotlin.math.roundToInt


/**
 * 整数扩展函数
 *
 * @author zhangls
 */

/**
 * 返回粗略的数值，添加 k, m, b 后缀
 */
fun Int.toRough(@IntRange(from = 1, to = 3) decimalLength: Int): String {
  val suffix = arrayOf("K", "M", "B")
  // 将长整数除以 1000.0，得到双精度浮点数
  var d = this / 1000.0

  return if (d < 1) {
    this.toString()
  } else if (d < 1000) {
    String.format("%.${decimalLength}f", d) + suffix[0]
  } else {
    d /= 1000.0
    if (d < 1000) {
      String.format("%.${decimalLength}f", d) + suffix[1]
    } else {
      String.format("%.${decimalLength}f", d / 1000.0) + suffix[2]
    }
  }
}

/**
 * dp 转 px
 */
val Number.dpToPx: Float
  get() = TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP,
      toFloat(),
      Resources.getSystem().displayMetrics
  )

/**
 * dp 转 px
 */
val Number.dpToPxInt: Int
  get() = TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP,
      toFloat(),
      Resources.getSystem().displayMetrics
  ).roundToInt()

/**
 * px 转 dp
 */
val Int.pxToDp: Float get() = this / Resources.getSystem().displayMetrics.density

/**
 * sp 转 px
 */
val Int.spToPx: Float
  get() = TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_SP,
      toFloat(),
      Resources.getSystem().displayMetrics
  )