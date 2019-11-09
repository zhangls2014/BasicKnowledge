package com.zhangls.android.basic.ui.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.zhangls.android.basic.R
import com.zhangls.android.basic.util.dpToPxInt
import kotlinx.android.synthetic.main.fragment_custom_view.*


class CustomViewFragment : Fragment() {
  /**
   * tab tabTitle
   */
  private var tabTitle = R.string.tab_text_draw
  /**
   * Fragment 是都被加载的标识符
   */
  private var isLoaded: Boolean = false


  companion object {
    private const val KEY_TAB_TITLE = "key_tab_title"

    fun newInstance(@StringRes title: Int): CustomViewFragment {
      return CustomViewFragment().apply {
        arguments = Bundle().apply {
          putInt(KEY_TAB_TITLE, title)
        }
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      tabTitle = it.getInt(KEY_TAB_TITLE)
    }
  }

  override fun onResume() {
    super.onResume()
    // 如果没有初始化，则进行初始化（实现懒加载）
    if (isLoaded.not()) {
      isLoaded = true
      init()
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_custom_view, container, false)
  }

  private fun init() {
    val view = view!!

    context?.let {
      when (tabTitle) {
        R.string.tab_text_draw -> customView.addView(CustomDrawView(it))
        R.string.tab_text_paint -> customView.addView(CustomPaintView(it))
        R.string.tab_text_text -> customView.addView(CustomTextView(it))
        R.string.tab_text_matrix -> customView.addView(CustomMatrixView(it))
        else -> customView.addView(CustomDrawView(it))
      }
    }

    chipGroup.apply {
      isSingleSelection = true
      chipSpacingHorizontal = 16.dpToPxInt
      chipSpacingVertical = 12.dpToPxInt
    }
    val chipNormalBackgroundColor = ContextCompat.getColorStateList(view.context, R.color.colorChipDefault)
    val chipCheckedBackgroundColor = ContextCompat.getColorStateList(view.context, R.color.colorPrimary)
    val chips = when (tabTitle) {
      R.string.tab_text_draw -> resources.getStringArray(R.array.tab_draw_chip)
      R.string.tab_text_paint -> resources.getStringArray(R.array.tab_paint_chip)
      R.string.tab_text_text -> resources.getStringArray(R.array.tab_text_chip)
      R.string.tab_text_matrix -> resources.getStringArray(R.array.tab_text_matrix)
      else -> resources.getStringArray(R.array.tab_draw_chip)
    }
    chips.forEach {
      val chip = Chip(view.context)
      chip.text = it
      chip.apply {
        isCheckable = true
        check(true)
        setTextColor(ContextCompat.getColor(view.context, android.R.color.black))
        isCloseIconVisible = false
        isChipIconVisible = false
        isCheckedIconVisible = false
        rippleColor = null
        chipBackgroundColor = chipNormalBackgroundColor
        setOnCheckedChangeListener { _, b ->
          chip.chipBackgroundColor = if (b) chipCheckedBackgroundColor else chipNormalBackgroundColor
        }
      }

      chipGroup.addView(chip)
    }

    chipGroup.setOnCheckedChangeListener { chipGroup, checkedId ->
      chipGroup.forEachIndexed { index, view ->
        if (view.id == checkedId) {
          when (tabTitle) {
            R.string.tab_text_draw -> clickDrawChip(index)
            R.string.tab_text_paint -> clickPaintChip(index)
            R.string.tab_text_text -> clickTextChip(index)
            R.string.tab_text_matrix -> clickMatrixCHip(index)
          }
        }
      }
    }

    val chip = chipGroup[0]
    if (chip is Chip) {
      chip.isChecked = true
    }
  }

  private fun clickDrawChip(index: Int) {
    val customDrawView = customView.getChildAt(0)
    if (customDrawView is CustomDrawView) {
      when (index) {
        0 -> customDrawView.setDrawType(CustomDrawView.DrawType.Color)
        1 -> customDrawView.setDrawType(CustomDrawView.DrawType.Circle)
        2 -> customDrawView.setDrawType(CustomDrawView.DrawType.Rect)
        3 -> customDrawView.setDrawType(CustomDrawView.DrawType.Point)
        4 -> customDrawView.setDrawType(CustomDrawView.DrawType.Oval)
        5 -> customDrawView.setDrawType(CustomDrawView.DrawType.Line)
        6 -> customDrawView.setDrawType(CustomDrawView.DrawType.RoundRect)
        7 -> customDrawView.setDrawType(CustomDrawView.DrawType.Arc)
        8 -> customDrawView.setDrawType(CustomDrawView.DrawType.Path)
        else -> customDrawView.setDrawType(CustomDrawView.DrawType.Color)
      }
    }
  }

  private fun clickPaintChip(index: Int) {
    val customPaintView = customView.getChildAt(0)
    if (customPaintView is CustomPaintView) {
      when (index) {
        0 -> customPaintView.setPaintType(CustomPaintView.PaintType.LinearGradient)
        1 -> customPaintView.setPaintType(CustomPaintView.PaintType.RadialGradient)
        2 -> customPaintView.setPaintType(CustomPaintView.PaintType.SweepGradient)
        3 -> customPaintView.setPaintType(CustomPaintView.PaintType.BitmapShader)
        4 -> customPaintView.setPaintType(CustomPaintView.PaintType.ComposeShader)
        5 -> customPaintView.setPaintType(CustomPaintView.PaintType.LightingColorFilter)
        6 -> customPaintView.setPaintType(CustomPaintView.PaintType.ColorMatrixColorFilter)
        7 -> customPaintView.setPaintType(CustomPaintView.PaintType.Xfermode)
        8 -> customPaintView.setPaintType(CustomPaintView.PaintType.StrokeCap)
        9 -> customPaintView.setPaintType(CustomPaintView.PaintType.StrokeJoin)
        10 -> customPaintView.setPaintType(CustomPaintView.PaintType.StrokeMiter)
        11 -> customPaintView.setPaintType(CustomPaintView.PaintType.PathEffect)
        12 -> customPaintView.setPaintType(CustomPaintView.PaintType.ShadowLayer)
        13 -> customPaintView.setPaintType(CustomPaintView.PaintType.MaskFilter)
        14 -> customPaintView.setPaintType(CustomPaintView.PaintType.FillPath)
        15 -> customPaintView.setPaintType(CustomPaintView.PaintType.TextPath)
        else -> customPaintView.setPaintType(CustomPaintView.PaintType.LinearGradient)
      }
    }
  }

  private fun clickTextChip(index: Int) {
    val customTextView = customView.getChildAt(0)
    if (customTextView is CustomTextView) {
      when (index) {
        0 -> customTextView.setTextType(CustomTextView.TextType.DrawText)
        1 -> customTextView.setTextType(CustomTextView.TextType.StaticLayout)
        2 -> customTextView.setTextType(CustomTextView.TextType.SetTextSize)
        3 -> customTextView.setTextType(CustomTextView.TextType.SetTypeface)
        4 -> customTextView.setTextType(CustomTextView.TextType.SetFakeBoldText)
        5 -> customTextView.setTextType(CustomTextView.TextType.SetStrikeThruText)
        6 -> customTextView.setTextType(CustomTextView.TextType.SetUnderlineText)
        7 -> customTextView.setTextType(CustomTextView.TextType.SetTextSkewX)
        8 -> customTextView.setTextType(CustomTextView.TextType.SetTextScaleX)
        9 -> customTextView.setTextType(CustomTextView.TextType.SetTextAlign)
      }
    }
  }

  private fun clickMatrixCHip(index: Int) {
    val customMatrixView = customView.getChildAt(0)
    if (customMatrixView is CustomMatrixView) {
      when (index) {
        0 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.ClipRect)
        1 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.ClipPath)
        2 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.CanvasTranslate)
        3 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.CanvasScale)
        4 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.CanvasRotate)
        5 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.CanvasSkew)
        6 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.MatrixTranslate)
        7 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.MatrixScale)
        8 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.MatrixRotate)
        9 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.MatrixSkew)
        10 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.CameraRotateXY)
        11 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.CameraRotateXYAmend)
        12 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.CameraRotateXYLocation)
        13 -> customMatrixView.setMatrixType(CustomMatrixView.MatrixType.PageFlip)
      }
    }
  }
}