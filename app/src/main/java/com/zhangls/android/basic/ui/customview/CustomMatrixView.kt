package com.zhangls.android.basic.ui.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.graphics.withMatrix
import androidx.core.graphics.withSave
import androidx.core.graphics.withSkew
import com.zhangls.android.basic.R
import com.zhangls.android.basic.util.dpToPx

/**
 * @author zhangls
 */
class CustomMatrixView : View {
  private var matrixType: MatrixType = MatrixType.ClipRect
  private val mapBitmap: Bitmap by lazy { BitmapFactory.decodeResource(resources, R.drawable.maps) }
  private val paint: Paint by lazy { Paint() }
  private var degree: Float = 0F
  private val animator: ValueAnimator by lazy {
    ValueAnimator.ofFloat(0F, 180F).apply {
      repeatCount = ValueAnimator.INFINITE
      repeatMode = ValueAnimator.REVERSE
      duration = 5000
      interpolator = LinearInterpolator()
      addUpdateListener {
        if (it.animatedValue is Float) {
          degree = it.animatedValue as Float
          invalidate()
        }
      }
    }
  }
  private val animateCamera: Camera by lazy { Camera() }


  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    canvas?.let {
      when (matrixType) {
        MatrixType.ClipRect -> clipRect(it)
        MatrixType.ClipPath -> clipPath(it)
        MatrixType.CanvasTranslate -> canvasTranslate(it)
        MatrixType.CanvasScale -> canvasScale(it)
        MatrixType.CanvasRotate -> canvasRotate(it)
        MatrixType.CanvasSkew -> canvasSkew(it)
        MatrixType.MatrixTranslate -> matrixTranslate(it)
        MatrixType.MatrixScale -> matrixScale(it)
        MatrixType.MatrixRotate -> matrixRotate(it)
        MatrixType.MatrixSkew -> matrixSkew(it)
        MatrixType.CameraRotateXY -> cameraRotateXY(it)
        MatrixType.CameraRotateXYAmend -> cameraRotateXYAmend(it)
        MatrixType.CameraRotateXYLocation -> cameraRotateLocation(it)
        MatrixType.PageFlip -> cameraAnimate(it)
      }
    }
  }

  fun setMatrixType(type: MatrixType) {
    matrixType = type

    pageFlipVisible(visibility == VISIBLE)
    if (matrixType != MatrixType.PageFlip) {
      invalidate()
    }
  }

  private fun clipRect(canvas: Canvas) {
    val rectWidth = mapBitmap.width * 0.8F
    val rectHeight = mapBitmap.height * 0.8F

    val left = (width - mapBitmap.width) / 2F
    val top = (height - mapBitmap.height) / 2F
    paint.alpha = 100
    canvas.drawBitmap(mapBitmap, left, top, paint)

    val rectLeft = (width - rectWidth) / 2F
    val rectRight = rectLeft + rectWidth
    val rectTop = (height - rectHeight) / 2F
    val rectBottom = rectTop + rectHeight

    canvas.save()
    canvas.clipRect(rectLeft, rectTop, rectRight, rectBottom)
    paint.alpha = 255
    canvas.drawBitmap(mapBitmap, left, top, paint)
    canvas.restore()
  }

  private fun clipPath(canvas: Canvas) {
    val interval = 50.dpToPx
    val mapBitmapWidth = mapBitmap.width
    val mapBitmapHeight = mapBitmap.height
    val centerLeftX = (width - mapBitmapWidth - interval) / 2F
    val centerY = height / 2F

    val pathLeft = Path()
    pathLeft.addCircle(centerLeftX, centerY, mapBitmapWidth / 2F - 16.dpToPx, Path.Direction.CW)
    canvas.save()
    canvas.clipPath(pathLeft)
    val leftX = centerLeftX - mapBitmapWidth / 2F
    val top = centerY - mapBitmapHeight / 2F
    canvas.drawBitmap(mapBitmap, leftX, top, paint)
    canvas.restore()

    val centerRightX = width - centerLeftX
    val pathRight = Path()
    pathRight.addCircle(centerRightX, centerY, mapBitmapWidth / 2F - 16.dpToPx, Path.Direction.CW)
    pathRight.fillType = Path.FillType.INVERSE_WINDING
    canvas.save()
    canvas.clipPath(pathRight)
    val rightX = centerRightX - mapBitmapWidth / 2F
    canvas.drawBitmap(mapBitmap, rightX, top, paint)
    canvas.restore()
  }

  private fun canvasTranslate(canvas: Canvas) {
    canvas.save()
    canvas.translate(50.dpToPx, 50.dpToPx)
    // 这里的左边是相对于移动后的位置。有点移动的是原点的感觉
    canvas.drawBitmap(mapBitmap, 100.dpToPx, 100.dpToPx, paint)
    canvas.restore()
  }

  private fun canvasScale(canvas: Canvas) {
    canvas.save()
    canvas.scale(0.5F, 0.5F)
    canvas.drawBitmap(mapBitmap, 16.dpToPx, 16.dpToPx, paint)
    canvas.scale(4F, 4F)
    canvas.drawBitmap(mapBitmap, 36.dpToPx, 36.dpToPx, paint)
    canvas.restore()
  }

  private fun canvasRotate(canvas: Canvas) {
    canvas.save()
    val center = 16.dpToPx + mapBitmap.width / 2
    canvas.rotate(30F, center, center)
    canvas.drawBitmap(mapBitmap, 16.dpToPx, 16.dpToPx, paint)
    canvas.rotate(-60F, center, center)
    // 此时的通过对比可以发现，X 轴的方向也发生了变化
    canvas.drawBitmap(mapBitmap, 32.dpToPx, 16.dpToPx, paint)
    canvas.restore()
  }

  private fun canvasSkew(canvas: Canvas) {
    // 哈哈，才发现 Kotlin 支持的语法糖，好！Kotlin Yes!
    canvas.withSkew {
      // 在此错切中，在 X 轴正方向倾斜了 45°，所以 tan 值为 1
      skew(1F, 0F)
      canvas.drawBitmap(mapBitmap, 0F, 0F, paint)
      skew(-1F, 0.5F)
      canvas.drawBitmap(mapBitmap, 0.dpToPx, 0F, paint)
    }
  }

  private fun matrixTranslate(canvas: Canvas) {
    val matrix = Matrix()
    matrix.postTranslate(100.dpToPx, 100.dpToPx)
    canvas.withMatrix(matrix) {
      drawBitmap(mapBitmap, 0.dpToPx, 0F, paint)
    }
  }

  private fun matrixScale(canvas: Canvas) {
    val matrix = Matrix().apply {
      postScale(2F, 2F)
    }
    canvas.withMatrix(matrix) {
      drawBitmap(mapBitmap, 0F, 0F, paint)
    }
  }

  private fun matrixRotate(canvas: Canvas) {
    val matrix = Matrix().apply {
      val radius = mapBitmap.width / 2F
      postRotate(90F, radius, radius)
    }
    canvas.withMatrix(matrix) {
      drawBitmap(mapBitmap, 0F, 0F, paint)
    }
  }

  private fun matrixSkew(canvas: Canvas) {
    val matrix = Matrix().apply {
      postSkew(-0.3F, 0F)
    }
    canvas.withMatrix(matrix) {
      drawBitmap(mapBitmap, 0F, 0F, paint)
    }
  }

  private fun cameraRotateXY(canvas: Canvas) {
    canvas.withSave {
      Camera().apply {
        rotateX(45F)
        rotateY(45F)
        rotateZ(45F)
        applyToCanvas(canvas)
      }
      canvas.drawBitmap(mapBitmap, 0F, 0F, paint)
    }
  }

  private fun cameraRotateXYAmend(canvas: Canvas) {
    canvas.withSave {
      val radius = mapBitmap.width / 2F
      // 将原点移动到图形中心点
      translate(radius, radius)

      Camera().apply {
        rotateX(45F)
        rotateY(45F)
        rotateZ(45F)
        applyToCanvas(canvas)
      }
      translate(-radius, -radius)
      canvas.drawBitmap(mapBitmap, 0F, 0F, paint)
    }
  }

  private fun cameraRotateLocation(canvas: Canvas) {
    canvas.withSave {
      val radius = mapBitmap.width / 2F
      // 将原点移动到图形中心点
      translate(radius, radius)

      Camera().apply {
        setLocation(0F, 0F, -16F)
        rotateX(45F)
        rotateY(45F)
        rotateZ(45F)
        applyToCanvas(canvas)
      }
      translate(-radius, -radius)
      canvas.drawBitmap(mapBitmap, 0F, 0F, paint)
    }
  }

  /**
   * 在这个方法中通过动画来生动展现 Camera 的旋转方式。
   */
  private fun cameraAnimate(canvas: Canvas) {
    val halfWidth = mapBitmap.width / 2F
    val left = (width - mapBitmap.width) / 2F
    val top = (height - mapBitmap.height) / 2F

    // 绘制上半部分图案
    canvas.withSave {
      clipRect(0, 0, width, height / 2)
      drawBitmap(mapBitmap, left, top, paint)
    }
    // 绘制下半部分图案
    canvas.withSave {
      if (degree < 90) {
        clipRect(0, height / 2, width, height)
      } else {
        clipRect(0, 0, width, height / 2)
      }

      animateCamera.save()
      translate(width / 2F, height / 2F)
      animateCamera.rotateX(degree)
      animateCamera.applyToCanvas(this)
      animateCamera.restore()
      drawBitmap(mapBitmap, -halfWidth, -halfWidth, paint)
    }
  }

  override fun onVisibilityChanged(changedView: View, visibility: Int) {
    super.onVisibilityChanged(changedView, visibility)
    pageFlipVisible(visibility == VISIBLE)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    pageFlipVisible(true)
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    pageFlipVisible(false)
  }

  private fun pageFlipVisible(isVisible: Boolean) {
    if (matrixType == MatrixType.PageFlip) {
      if (isVisible) {
        if (animator.isPaused) {
          animator.resume()
        } else if (animator.isStarted.not()) {
          animator.start()
        }
      } else {
        if (animator.isStarted) animator.pause()
      }
    } else {
      if (animator.isStarted) animator.pause()
    }
  }

  enum class MatrixType {
    ClipRect,
    ClipPath,
    CanvasTranslate,
    CanvasScale,
    CanvasRotate,
    CanvasSkew,
    MatrixTranslate,
    MatrixScale,
    MatrixRotate,
    MatrixSkew,
    CameraRotateXY,
    CameraRotateXYAmend,
    CameraRotateXYLocation,
    PageFlip
  }
}