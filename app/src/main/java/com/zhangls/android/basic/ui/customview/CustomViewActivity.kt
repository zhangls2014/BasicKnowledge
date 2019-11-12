package com.zhangls.android.basic.ui.customview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.zhangls.android.basic.R
import kotlinx.android.synthetic.main.activity_custom_view.*

/**
 * @author zhangls
 */
class CustomViewActivity : AppCompatActivity() {

  companion object {
    @StringRes
    val TAB_TITLES = intArrayOf(R.string.tab_text_draw, R.string.tab_text_paint, R.string.tab_text_text, R.string.tab_text_matrix, R.string.tab_text_order)

    fun actionStart(context: Context) {
      val intent = Intent(context, CustomViewActivity::class.java)
      context.startActivity(intent)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_custom_view)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
      override fun getItem(position: Int): Fragment = CustomViewFragment.newInstance(TAB_TITLES[position])

      override fun getCount(): Int = TAB_TITLES.size

      override fun getPageTitle(position: Int): CharSequence? = getString(TAB_TITLES[position])
    }
    viewPager.offscreenPageLimit = TAB_TITLES.size
    tabs.setupWithViewPager(viewPager)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> {
        finish()
        true
      }
      else -> {
        super.onOptionsItemSelected(item)
      }
    }
  }
}