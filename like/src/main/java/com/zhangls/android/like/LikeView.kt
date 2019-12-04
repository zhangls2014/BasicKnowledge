package com.zhangls.android.like

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import kotlin.math.max
import kotlin.math.roundToInt


/**
 * 点赞 View
 *
 * 拇指和闪光构成了整个 View，拇指和闪光不能通过属性自行设置（因为不同的图片会导致宽高和重叠区域的不同）
 * 自带的拇指和闪光是宽高均为 24dp 的矢量图，重叠部分为 1/3 闪光高度
 *
 * 属性：LikeWidth 是指拇指视图的长宽
 *
 * @author zhangls
 */
class LikeView : View {
  // 未点赞 Drawable
  private lateinit var dislikeOriginalBitmap: Bitmap
  // 已点赞 Drawable
  private lateinit var likeOriginalBitmap: Bitmap
  // 闪光 Drawable
  private lateinit var shiningOriginalBitmap: Bitmap
  // paint
  private val bitmapPaint: Paint by lazy { Paint() }
  // paint
  private val haloPaint: Paint by lazy {
    Paint().apply {
      color = ContextCompat.getColor(context, R.color.click_ripple)
      style = Paint.Style.STROKE
      strokeWidth = 2.dpToPx
    }
  }
  // 选中状态：false 为未选中
  private var isLike: Boolean = false
  // 显示的大拇指
  private lateinit var thumbBitmap: Bitmap
  // 显示的闪光
  private lateinit var shiningBitmap: Bitmap
  // 设定的宽高，用于随意限制视图的大小
  private var likeWidth: Int = 24.dpToPxInt

  // 点赞缩小比例
  private var likeBeforeScale: Float = 0F
  // 点赞缩小动画：减速
  private val likeBeforeAnimator: ObjectAnimator by lazy {
    ObjectAnimator.ofFloat(this, "likeBeforeScale", 1F, 0.8F).apply {
      duration = 200
      interpolator = DecelerateInterpolator()
    }
  }
  // 点赞放大比例
  private var likeAfterScale: Float = 0F
  // 点赞放大动画：超出回弹
  private val likeAfterAnimator: ObjectAnimator by lazy {
    ObjectAnimator.ofFloat(this, "likeAfterScale", 0.8F, 1F).apply {
      duration = 300
      interpolator = OvershootInterpolator()
    }
  }
  // 闪光放大比例
  private var shiningScale: Float = 0F
  // 点赞放大动画：超出回弹
  private val shiningAnimator: ObjectAnimator by lazy {
    ObjectAnimator.ofFloat(this, "shiningScale", 0.1F, 1F).apply {
      duration = 300
      interpolator = OvershootInterpolator()
    }
  }
  // 光圈半径
  private var haloRadius: Float = 0F
  // 点赞放大动画：超出回弹
  private val haloRadiusAnimator: ObjectAnimator by lazy {
    ObjectAnimator.ofFloat(this, "haloRadius", 0F, haloMaxRadius).apply {
      duration = 300
      interpolator = AccelerateDecelerateInterpolator()
      doOnEnd {
        haloRadius = 0F
      }
    }
  }
  private val haloMaxRadius: Float
    get() {
      val likeBmpMaxSide = max(likeOriginalBitmap.width, likeOriginalBitmap.height)
      val dislikeBmpMaxSide = max(dislikeOriginalBitmap.width, dislikeOriginalBitmap.height)
      return max(likeBmpMaxSide, dislikeBmpMaxSide) / 2 + 2.dpToPx
    }

  private val animatorLikeSet: AnimatorSet by lazy { AnimatorSet() }
  private val animatorDislikeSet: AnimatorSet by lazy { AnimatorSet() }


  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LikeView)

    val originalBitmap = BitmapUtil.getBitmap(context, R.drawable.ic_like_unselected)
    val sideLength = typedArray.getDimensionPixelSize(R.styleable.LikeView_lv_likeWidth, originalBitmap.width)
    likeWidth = if (sideLength >= originalBitmap.width) originalBitmap.width else sideLength
    typedArray.recycle()

    dislikeOriginalBitmap = originalBitmap.scale(likeWidth, likeWidth)
    likeOriginalBitmap = BitmapUtil.getBitmap(context, R.drawable.ic_like_selected).scale(likeWidth, likeWidth)
    shiningOriginalBitmap = BitmapUtil.getBitmap(context, R.drawable.ic_like_shining).scale(likeWidth, likeWidth)

    thumbBitmap = dislikeOriginalBitmap
    shiningBitmap = shiningOriginalBitmap
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    // 为光圈流出空间（手指的左侧、右侧、下方 分别留出 2dp 间隔）
    val needWidth = paddingStart + likeWidth + paddingEnd + 4.dpToPxInt
    val needHeight = paddingTop + 5 * likeWidth / 3 + paddingBottom + 2.dpToPxInt

    val contentWidth = resolveSize(needWidth, widthMeasureSpec)
    val contentHeight = resolveSize(needHeight, heightMeasureSpec)

    setMeasuredDimension(contentWidth, contentHeight)
  }

  override fun onDraw(canvas: Canvas?) {
    canvas?.let {
      if (isLike && shiningScale > 0) {
        val shiningLeft = paddingStart + (likeWidth - shiningBitmap.width) / 2F + 2.dpToPx
        val shiningTop = paddingTop + likeWidth - shiningBitmap.height
        it.drawBitmap(shiningBitmap, shiningLeft, shiningTop.toFloat(), bitmapPaint)
      }

      val likeLeft = (likeWidth - thumbBitmap.width) / 2F + paddingStart + 2.dpToPx
      val likeTop = height - paddingBottom - 2.dpToPx - likeWidth / 2 - thumbBitmap.height / 2F
      it.drawBitmap(thumbBitmap, likeLeft, likeTop, bitmapPaint)

      if (haloRadius > 0) {
        // 触发了点击效果
        val cx = (width - paddingStart - paddingEnd) / 2F + paddingStart
        val cy = height - paddingBottom - 2.dpToPx - likeWidth / 2
        // 如果光圈半径的最大半径的 80% ~ 100% 之间，则光圈透明度随半径变化
        if (haloRadius >= haloMaxRadius * 0.8 && haloRadius <= haloMaxRadius) {
          haloPaint.alpha = ((haloMaxRadius - haloRadius) / haloMaxRadius * 255).roundToInt()
        } else {
          haloPaint.alpha = 255
        }
        it.drawCircle(cx, cy, haloRadius, haloPaint)
      }
    }
  }

  /**
   * 属性动画必须的 set 方法：likeBeforeScale
   */
  @SuppressWarnings("unused")
  private fun setLikeBeforeScale(scale: Float) {
    val matrix = Matrix()
    matrix.postScale(scale, scale)

    thumbBitmap = if (isLike) {
      Bitmap.createBitmap(dislikeOriginalBitmap, 0, 0, dislikeOriginalBitmap.width, dislikeOriginalBitmap.height, matrix, false)
    } else {
      Bitmap.createBitmap(likeOriginalBitmap, 0, 0, likeOriginalBitmap.width, likeOriginalBitmap.height, matrix, false)
    }


    postInvalidate()
  }

  /**
   * 属性动画必须的 set 方法：likeAfterScale
   */
  @SuppressWarnings("unused")
  private fun setLikeAfterScale(scale: Float) {
    val matrix = Matrix()
    matrix.postScale(scale, scale)

    thumbBitmap = if (isLike) {
      Bitmap.createBitmap(likeOriginalBitmap, 0, 0, likeOriginalBitmap.width, likeOriginalBitmap.height, matrix, false)
    } else {
      Bitmap.createBitmap(dislikeOriginalBitmap, 0, 0, dislikeOriginalBitmap.width, dislikeOriginalBitmap.height, matrix, false)
    }

    postInvalidate()
  }

  /**
   * 属性动画必须的 set 方法：shiningScale
   */
  @SuppressWarnings("unused")
  private fun setShiningScale(scale: Float) {
    if (scale > 0) {
      // Bitmap 的宽高必须大于零
      val matrix = Matrix()
      matrix.postScale(scale, scale)

      shiningBitmap = Bitmap.createBitmap(shiningOriginalBitmap, 0, 0, shiningOriginalBitmap.width, shiningOriginalBitmap.height, matrix, false)

      shiningScale = scale
      postInvalidate()
    }
  }

  /**
   * 属性动画必须的 set 方法：haloRadius
   */
  @SuppressWarnings("unused")
  private fun setHaloRadius(radius: Float) {
    haloRadius = radius
  }

  fun setLike(like: Boolean) {
    if (isLike != like) {
      isLike = like

      if (isLike) likeAnimator() else dislikeAnimator()
    }
  }

  fun getLike(): Boolean = isLike

  private fun likeAnimator() {
    shiningScale = 0F

    animatorLikeSet.play(likeBeforeAnimator).with(haloRadiusAnimator).before(likeAfterAnimator)
    animatorLikeSet.play(likeAfterAnimator).with(shiningAnimator)
    animatorLikeSet.start()
  }

  private fun dislikeAnimator() {
    shiningAnimator.reverse()
    animatorDislikeSet.playSequentially(likeBeforeAnimator, likeAfterAnimator)
    animatorDislikeSet.start()
    postInvalidate()
  }
}