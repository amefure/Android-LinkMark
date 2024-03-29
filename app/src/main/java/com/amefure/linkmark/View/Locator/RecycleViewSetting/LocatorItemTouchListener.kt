package com.amefure.linkmark.View.Locator.RecycleViewSetting

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.amefure.linkmark.Model.Database.Locator

class LocatorItemTouchListener: RecyclerView.SimpleOnItemTouchListener() {

    private lateinit var listener: onTappedListner

    interface onTappedListner {
        fun onTapped(url: String)
    }

    /**
     * リスナーのセットは使用するFragmentから呼び出して行う
     * リスナーオブジェクトの中に処理が含まれて渡される
     */
    public fun setOnTappedListner(listener: onTappedListner) {
        // 定義した変数listenerに実行したい処理を引数で渡す（CategoryListFragmentで渡している）
        this.listener = listener
    }
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        // タッチイベントの処理
        if (e.action == MotionEvent.ACTION_UP) {
            // タッチされた位置のViewを取得
            val childView: View? = rv.findChildViewUnder(e.x, e.y)
            if (childView != null) {
                // 要素番号を取得
                val position = rv.getChildAdapterPosition(childView)
                if (position != RecyclerView.NO_POSITION) {
                    val adapter = rv.adapter
                    if (adapter is LocatorAdapter) {
                        val tappedItem: Locator? = adapter.getItemAtPosition(position)
                        tappedItem?.let {
                            listener.onTapped(it.url)
                        }
                    }
                }
            }
        }
        return false // 通常のタッチイベント処理を維持
    }
}