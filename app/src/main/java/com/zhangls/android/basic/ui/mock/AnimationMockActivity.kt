package com.zhangls.android.basic.ui.mock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zhangls.android.basic.R
import kotlinx.android.synthetic.main.activity_animation_mock.*


/**
 * 采用自定义 View 的方法模仿大厂的动画，练习自定义动画
 *
 * @author zhangls
 */
class AnimationMockActivity : AppCompatActivity() {

  companion object {
    /**
     * Fragment 数量
     */
    private const val NUM_FRAGMENTS = 4

    fun actionStart(context: Context) {
      val intent = Intent(context, AnimationMockActivity::class.java)
      context.startActivity(intent)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_animation_mock)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    viewPager.adapter = SlidePagerAdapter(this)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> {
        finish()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }


  private inner class SlidePagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int = Companion.NUM_FRAGMENTS

    override fun createFragment(position: Int): Fragment {
      return when (position) {
        0 -> LikeFragment.newInstance()
        else -> LikeFragment.newInstance()
      }
    }

  }
}
