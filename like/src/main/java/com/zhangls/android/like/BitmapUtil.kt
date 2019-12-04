package com.zhangls.android.like

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat


/**
 * @author zhangls
 */
object BitmapUtil {
  private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
    val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)
    return bitmap
  }

  private fun getBitmap(vectorDrawable: VectorDrawableCompat): Bitmap {
    val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)
    return bitmap
  }

  fun getBitmap(context: Context, @DrawableRes drawableResId: Int): Bitmap {
    return when (val drawable = ContextCompat.getDrawable(context, drawableResId)) {
      is BitmapDrawable -> drawable.bitmap
      is VectorDrawableCompat -> getBitmap(drawable)
      is VectorDrawable -> getBitmap(drawable)
      else -> throw IllegalArgumentException("Unsupported drawable type")
    }
  }
}