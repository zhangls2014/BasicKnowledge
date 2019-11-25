package com.zhangls.android.basic.ui.home

/**
 * @author zhangls
 */
data class HomeItem(val type: HomeItemType, val title: String)

enum class HomeItemType {
  CustomView,
  AnimationMock
}