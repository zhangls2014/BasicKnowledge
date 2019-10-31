package com.zhangls.android.basic.ui.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.zhangls.android.basic.R
import com.zhangls.android.basic.util.dpToPx

/**
 * @author zhangls
 */
class CustomPaintView : View {
  private var paintType: PaintType = PaintType.LinearGradient
  private val defaultColor: Int by lazy {
    ContextCompat.getColor(context, R.color.colorPrimary)
  }
  private val shaderPaint: Paint by lazy {
    Paint().apply {
      style = Paint.Style.FILL
    }
  }
  private val colorFilterPaint: Paint by lazy {
    Paint()
  }
  private val xfermodePaint: Paint by lazy {
    Paint()
  }
  private val strokePaint: Paint by lazy {
    Paint().apply {
      strokeWidth = 16.dpToPx
      color = defaultColor
      style = Paint.Style.STROKE
    }
  }
  private val pathEffectPaint: Paint by lazy {
    Paint().apply {
      style = Paint.Style.STROKE
      strokeWidth = 1.dpToPx
      strokeCap = Paint.Cap.ROUND
    }
  }
  private val shadowLayerPaint: Paint by lazy {
    Paint().apply {
      textSize = 120F
    }
  }
  private val maskFilterPaint: Paint by lazy {
    Paint()
  }
  private val fillPathPaint: Paint by lazy {
    Paint().apply {
      style = Paint.Style.STROKE
      textSize = 120F
    }
  }


  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    canvas?.let { paintTypeSelect(it) }
  }

  private fun paintTypeSelect(canvas: Canvas) {
    when (paintType) {
      PaintType.LinearGradient -> paintLinearGradient(canvas)
      PaintType.RadialGradient -> paintRadialGradient(canvas)
      PaintType.SweepGradient -> paintSweepGradient(canvas)
      PaintType.BitmapShader -> paintBitmapShader(canvas)
      PaintType.ComposeShader -> paintComposeShader(canvas)
      PaintType.LightingColorFilter -> paintLightingColorFilter(canvas)
      PaintType.ColorMatrixColorFilter -> paintColorMatrixColorFilter(canvas)
      PaintType.Xfermode -> paintXfermode(canvas)
      PaintType.StrokeCap -> paintStrokeCap(canvas)
      PaintType.StrokeJoin -> paintStrokeJoin(canvas)
      PaintType.StrokeMiter -> paintStrokeMiter(canvas)
      PaintType.PathEffect -> paintPathEffect(canvas)
      PaintType.ShadowLayer -> paintShadowLayer(canvas)
      PaintType.MaskFilter -> paintMaskFilter(canvas)
      PaintType.FillPath -> paintFillPath(canvas)
      PaintType.TextPath -> paintTextPath(canvas)
    }
  }

  fun setPaintType(type: PaintType) {
    paintType = type
    invalidate()
  }

  private fun paintLinearGradient(canvas: Canvas) {
    // LinearGradient 中 [x0,y0], [x1,y1] 点坐标代表整个 view 中的坐标
    shaderPaint.shader = LinearGradient(400F, 400F, 800F, 800F, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"), Shader.TileMode.CLAMP)
    canvas.drawCircle(600F, 600F, 200F, shaderPaint)
  }

  private fun paintRadialGradient(canvas: Canvas) {
    shaderPaint.shader = RadialGradient(600F, 600F, 200F, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"), Shader.TileMode.CLAMP)
    canvas.drawCircle(600F, 600F, 200F, shaderPaint)
  }

  private fun paintSweepGradient(canvas: Canvas) {
    // 颜色的始末位置是根据绘制起始停止位置决定的
    shaderPaint.shader = SweepGradient(600F, 600F, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"))
    canvas.drawCircle(600F, 600F, 200F, shaderPaint)
  }

  private fun paintBitmapShader(canvas: Canvas) {
    ContextCompat.getDrawable(context, R.drawable.ic_android_black_24dp)?.toBitmap(400, 400)?.let {
      shaderPaint.shader = BitmapShader(it, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
      canvas.drawCircle(600F, 600F, 200F, shaderPaint)
    }
  }

  private fun paintComposeShader(canvas: Canvas) {
    ContextCompat.getDrawable(context, R.drawable.ic_android_black_24dp)?.toBitmap(400, 400)?.let {
      val bitmapShader = BitmapShader(it, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
      val linearGradient = LinearGradient(400F, 400F, 800F, 800F, Color.parseColor("#E91E63"), Color.parseColor("#2196F3"), Shader.TileMode.CLAMP)
      shaderPaint.shader = ComposeShader(bitmapShader, linearGradient, PorterDuff.Mode.SRC_IN)
      canvas.drawCircle(600F, 600F, 200F, shaderPaint)
    }
  }

  private fun paintLightingColorFilter(canvas: Canvas) {
    ContextCompat.getDrawable(context, R.drawable.batman)?.toBitmap()?.let {
      colorFilterPaint.colorFilter = LightingColorFilter(0x00ffff, 0x000000)
      canvas.drawBitmap(it, 100F, 100F, colorFilterPaint)
    }

    ContextCompat.getDrawable(context, R.drawable.ic_android_black_24dp)?.toBitmap(387, 376)?.let {
      colorFilterPaint.colorFilter = PorterDuffColorFilter(Color.parseColor("#ff00ff"), PorterDuff.Mode.SRC_IN)
      canvas.drawBitmap(it, 600F, 100F, colorFilterPaint)
    }
  }

  private fun paintColorMatrixColorFilter(canvas: Canvas) {
    ContextCompat.getDrawable(context, R.drawable.batman)?.toBitmap()?.let {
      colorFilterPaint.colorFilter = null
      canvas.drawBitmap(it, 100F, 100F, colorFilterPaint)

      val matrix = ColorMatrix()
      matrix.setSaturation(0F)
      colorFilterPaint.colorFilter = ColorMatrixColorFilter(matrix)
      canvas.drawBitmap(it, 600F, 100F, colorFilterPaint)
    }
  }

  private fun paintXfermode(canvas: Canvas) {
    // 离屏缓冲
    val saved = canvas.saveLayer(100F, 100F, 500F, 500F, xfermodePaint)

    // Xfermode 就是决定绘制内容重叠的显示方式
    ContextCompat.getDrawable(context, R.drawable.batman)?.toBitmap(400, 400)?.let {
      canvas.drawBitmap(it, 100F, 100F, xfermodePaint)
      val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
      xfermodePaint.xfermode = xfermode
    }

    ContextCompat.getDrawable(context, R.drawable.ic_android_black_24dp)?.toBitmap(200, 200)?.let {
      canvas.drawBitmap(it, 200F, 300F, xfermodePaint)
      xfermodePaint.xfermode = null
    }

    // 显示缓冲
    canvas.restoreToCount(saved)
  }

  private fun paintStrokeCap(canvas: Canvas) {
    strokePaint.strokeCap = Paint.Cap.BUTT
    canvas.drawLine(100F, 100F, 600F, 100F, strokePaint)
    strokePaint.strokeCap = Paint.Cap.ROUND
    canvas.drawLine(100F, 200F, 600F, 200F, strokePaint)
    strokePaint.strokeCap = Paint.Cap.SQUARE
    canvas.drawLine(100F, 300F, 600F, 300F, strokePaint)
  }

  private fun paintStrokeJoin(canvas: Canvas) {
    strokePaint.strokeCap = Paint.Cap.ROUND
    val path = Path()
    path.moveTo(0F, 0F)
    path.rLineTo(200F, 0F)
    path.rLineTo(-200F, 200F)

    strokePaint.strokeJoin = Paint.Join.BEVEL
    canvas.translate(100F, 100F)
    canvas.drawPath(path, strokePaint)
    strokePaint.strokeJoin = Paint.Join.MITER
    canvas.translate(300F, 0F)
    canvas.drawPath(path, strokePaint)
    strokePaint.strokeJoin = Paint.Join.ROUND
    canvas.translate(300F, 0F)
    canvas.drawPath(path, strokePaint)
  }

  private fun paintStrokeMiter(canvas: Canvas) {
    strokePaint.strokeCap = Paint.Cap.ROUND
    strokePaint.strokeJoin = Paint.Join.MITER
    val path = Path()
    path.moveTo(0F, 0F)
    path.rLineTo(200F, 0F)
    path.rLineTo(-200F, 200F)

    strokePaint.strokeMiter = 2F
    canvas.translate(100F, 100F)
    canvas.drawPath(path, strokePaint)
    strokePaint.strokeMiter = 4F
    canvas.translate(300F, 0F)
    canvas.drawPath(path, strokePaint)
    strokePaint.strokeMiter = 6F
    canvas.translate(300F, 0F)
    canvas.drawPath(path, strokePaint)
  }

  private fun paintPathEffect(canvas: Canvas) {
    val dashPathEffect = DashPathEffect(floatArrayOf(20F, 10F, 5F, 10F), 0F)
    val cornerPathEffect = CornerPathEffect(20F)
    val discretePathEffect = DiscretePathEffect(30F, 5F)
    val dashPath = Path()
    dashPath.lineTo(20f, -30f)
    dashPath.lineTo(40f, 0f)
    dashPath.close()
    val pathDashPathEffect = PathDashPathEffect(dashPath, 50F, 0F, PathDashPathEffect.Style.MORPH)
    val sumPathEffect = SumPathEffect(dashPathEffect, discretePathEffect)
    val composePathEffect = ComposePathEffect(dashPathEffect, discretePathEffect)

    val path = Path()
    path.moveTo(50f, 100f)
    path.rLineTo(50f, 100f)
    path.rLineTo(80f, -150f)
    path.rLineTo(100f, 100f)
    path.rLineTo(70f, -120f)
    path.rLineTo(150f, 80f)
    pathEffectPaint.pathEffect = null
    canvas.drawPath(path, pathEffectPaint)

    pathEffectPaint.pathEffect = dashPathEffect
    canvas.translate(500F, 0F)
    canvas.drawPath(path, pathEffectPaint)

    pathEffectPaint.pathEffect = cornerPathEffect
    canvas.translate(-500F, 200F)
    canvas.drawPath(path, pathEffectPaint)

    pathEffectPaint.pathEffect = discretePathEffect
    canvas.translate(500F, 0F)
    canvas.drawPath(path, pathEffectPaint)

    pathEffectPaint.pathEffect = pathDashPathEffect
    canvas.translate(-500F, 200F)
    canvas.drawPath(path, pathEffectPaint)

    pathEffectPaint.pathEffect = sumPathEffect
    canvas.translate(500F, 0F)
    canvas.drawPath(path, pathEffectPaint)

    pathEffectPaint.pathEffect = composePathEffect
    canvas.translate(-500F, 200F)
    canvas.drawPath(path, pathEffectPaint)
  }

  private fun paintShadowLayer(canvas: Canvas) {
    shadowLayerPaint.setShadowLayer(10F, 0F, 0F, defaultColor)
    canvas.drawText("zhangls", 100F, 200F, shadowLayerPaint)
  }

  private fun paintMaskFilter(canvas: Canvas) {
    ContextCompat.getDrawable(context, R.drawable.batman)?.toBitmap(250, 250)?.let {
      val blurMaskFilter1 = BlurMaskFilter(50F, BlurMaskFilter.Blur.NORMAL)
      maskFilterPaint.maskFilter = blurMaskFilter1
      canvas.drawBitmap(it, 50F, 50F, maskFilterPaint)

      val blurMaskFilter2 = BlurMaskFilter(50F, BlurMaskFilter.Blur.INNER)
      maskFilterPaint.maskFilter = blurMaskFilter2
      canvas.drawBitmap(it, 500F, 50F, maskFilterPaint)

      val blurMaskFilter3 = BlurMaskFilter(30F, BlurMaskFilter.Blur.OUTER)
      maskFilterPaint.maskFilter = blurMaskFilter3
      canvas.drawBitmap(it, 50F, 400F, maskFilterPaint)

      val blurMaskFilter4 = BlurMaskFilter(30F, BlurMaskFilter.Blur.SOLID)
      maskFilterPaint.maskFilter = blurMaskFilter4
      canvas.drawBitmap(it, 500F, 400F, maskFilterPaint)
    }
  }

  private fun paintFillPath(canvas: Canvas) {
    val dashPath = Path()
    dashPath.lineTo(20f, -30f)
    dashPath.lineTo(40f, 0f)
    dashPath.close()
    val pathDashPathEffect = PathDashPathEffect(dashPath, 50F, 0F, PathDashPathEffect.Style.MORPH)

    val path = Path()
    path.moveTo(50f, 100f)
    path.rLineTo(50f, 100f)
    path.rLineTo(80f, -150f)
    path.rLineTo(100f, 100f)
    path.rLineTo(70f, -120f)
    path.rLineTo(150f, 80f)
    pathEffectPaint.pathEffect = pathDashPathEffect
    canvas.drawPath(path, pathEffectPaint)

    val dstPath = Path()
    pathEffectPaint.getFillPath(path, dstPath)
    canvas.translate(500F, 0F)
    canvas.drawPath(dstPath, fillPathPaint)
  }

  private fun paintTextPath(canvas: Canvas) {
    // text path 不包含 shadowLayer 绘制内容
    val text = "zhangls"
    canvas.drawText(text, 100F, 100F, shadowLayerPaint)
    val dstTextPath = Path()
    shadowLayerPaint.getTextPath(text, 0, text.length, 100F, 200F, dstTextPath)
    canvas.drawPath(dstTextPath, fillPathPaint)
  }


  enum class PaintType {
    LinearGradient,
    RadialGradient,
    SweepGradient,
    BitmapShader,
    ComposeShader,
    LightingColorFilter,
    ColorMatrixColorFilter,
    Xfermode,
    StrokeCap,
    StrokeJoin,
    StrokeMiter,
    PathEffect,
    ShadowLayer,
    MaskFilter,
    FillPath,
    TextPath
  }

}