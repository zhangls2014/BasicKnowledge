package com.zhangls.android.basic.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 输入法相关扩展方法
 *
 * @author zhangls
 */

/**
 * 切换输入法状态
 */
fun Context.toggleKeyboard() {
  val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS)
}

/**
 * 打开软键盘
 */
fun EditText.openKeyboard() {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * 关闭软键盘
 */
fun EditText.closeKeyboard() {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}