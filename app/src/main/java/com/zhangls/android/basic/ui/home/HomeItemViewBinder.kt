package com.zhangls.android.basic.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.zhangls.android.basic.R
import com.zhangls.android.basic.ui.customview.CustomViewActivity
import com.zhangls.android.basic.ui.mock.AnimationMockActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_item_home.*

/**
 * @author zhangls
 */
class HomeItemViewBinder : ItemViewBinder<HomeItem, HomeItemViewBinder.ViewHolder>() {

  override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
    return ViewHolder(inflater.inflate(R.layout.recycler_item_home, parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, item: HomeItem) {
    holder.tvTitle.text = item.title
    holder.tvTitle.setOnClickListener {
      when (item.type) {
        HomeItemType.CustomView -> CustomViewActivity.actionStart(it.context)
        HomeItemType.AnimationMock -> AnimationMockActivity.actionStart(it.context)
      }
    }
  }

  class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}