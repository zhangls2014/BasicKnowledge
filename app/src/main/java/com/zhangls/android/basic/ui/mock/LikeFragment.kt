package com.zhangls.android.basic.ui.mock


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.zhangls.android.basic.R
import com.zhangls.android.basic.util.closeKeyboard
import kotlinx.android.synthetic.main.fragment_like.*


/**
 * @author zhangls
 */
class LikeFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_like, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    etNum.setOnEditorActionListener { v, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_GO) {
        etNum.closeKeyboard()
        // TODO LikeView 执行数值设置
        return@setOnEditorActionListener true
      } else {
        return@setOnEditorActionListener false
      }
    }
    likeView.setTextSize(24F)
    likeView.setTextColor(Color.parseColor("#BBBBBB"))
    likeView.setOnClickListener {
      likeView.setCount(likeView.getCount() + 1)
    }
  }


  companion object {
    fun newInstance() = LikeFragment()
  }
}
