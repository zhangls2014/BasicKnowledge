package com.zhangls.android.basic.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.zhangls.android.basic.R
import com.zhangls.android.basic.ui.customview.CustomViewActivity

/**
 * @author zhangls
 */
class HomeItemViewBinder : ItemViewBinder<HomeItem, HomeItemViewBinder.ViewHolder>() {

  override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
    return ViewHolder(inflater.inflate(R.layout.recycler_item_home, parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, item: HomeItem) {
    holder.title.text = item.title
    holder.title.setOnClickListener {
      when (item.type) {
        HomeItemType.CustomView -> CustomViewActivity.actionStart(it.context)
      }
    }
  }

  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title = itemView.findViewById<AppCompatTextView>(R.id.tvTitle)
  }
}

