package com.zhangls.android.basic.ui.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

import com.zhangls.android.basic.R
import com.zhangls.android.basic.util.dpToPxInt
import kotlinx.android.synthetic.main.fragment_custom_view.*


class CustomViewFragment : Fragment() {
  /**
   * tab tabTitle
   */
  private var tabTitle = R.string.tab_text_draw


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

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_custom_view, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    chipGroup.apply {
      isSingleSelection = true
      chipSpacingHorizontal = 16.dpToPxInt
      chipSpacingVertical = 12.dpToPxInt
    }
    val chipNormalBackgroundColor = ContextCompat.getColorStateList(view.context, R.color.colorChipDefault)
    val chipCheckedBackgroundColor = ContextCompat.getColorStateList(view.context, R.color.colorPrimary)
    val chips = resources.getStringArray(R.array.tab_draw_chip)
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
          chip.chipBackgroundColor =  if (b) chipCheckedBackgroundColor else chipNormalBackgroundColor
        }
      }

      chipGroup.addView(chip)
    }

    val chip = chipGroup[0]
    if (chip is Chip) {
      chip.isChecked = true
    }

    chipGroup.setOnCheckedChangeListener { chipGroup, checkedId ->
      chipGroup.forEachIndexed { index, view ->
        if (view.id == checkedId) {
          when (index) {
            0 -> customView.setDrawType(CustomView.DrawType.Color)
            1 -> customView.setDrawType(CustomView.DrawType.Circle)
            2 -> customView.setDrawType(CustomView.DrawType.Rect)
            3 -> customView.setDrawType(CustomView.DrawType.Point)
            4 -> customView.setDrawType(CustomView.DrawType.Oval)
            5 -> customView.setDrawType(CustomView.DrawType.Line)
            6 -> customView.setDrawType(CustomView.DrawType.RoundRect)
            7 -> customView.setDrawType(CustomView.DrawType.Arc)
            8 -> customView.setDrawType(CustomView.DrawType.Path)
          }
        }
      }
    }
  }
}