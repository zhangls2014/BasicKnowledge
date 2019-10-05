package com.zhangls.android.basic.ui.customview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.zhangls.android.basic.R
import kotlinx.android.synthetic.main.activity_custom_view.*

/**
 * @author zhangls
 */
open class CustomViewActivity : AppCompatActivity() {

  companion object {
    @StringRes
    val TAB_TITLES = intArrayOf(R.string.tab_text_draw, R.string.tab_text_2)

    fun actionStart(context: Context) {
      val intent = Intent(context, CustomViewActivity::class.java)
      context.startActivity(intent)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_custom_view)

    viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
      override fun getItem(position: Int): Fragment = CustomViewFragment.newInstance(TAB_TITLES[position])

      override fun getCount(): Int = TAB_TITLES.size

      override fun getPageTitle(position: Int): CharSequence? = getString(TAB_TITLES[position])
    }
    tabs.setupWithViewPager(viewPager)
  }
}