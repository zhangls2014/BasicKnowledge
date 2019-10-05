package com.zhangls.android.basic.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.zhangls.android.basic.R
import kotlinx.android.synthetic.main.activity_home.*

/**
 * @author zhangls
 */
class HomeActivity : AppCompatActivity() {

  private val adapter = MultiTypeAdapter()
  private val items = arrayListOf<Any>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    setSupportActionBar(toolbar)

    adapter.register(HomeItemViewBinder())
    adapter.items = items
    recycler.adapter = adapter
    recycler.layoutManager = LinearLayoutManager(this)

    items.add(HomeItem(HomeItemType.CustomView, "自定义 View"))
    adapter.notifyDataSetChanged()
  }
}
