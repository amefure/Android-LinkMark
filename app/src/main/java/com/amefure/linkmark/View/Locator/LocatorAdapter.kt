package com.amefure.linkmark.View.Locator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amefure.linkmark.Model.Locator
import com.amefure.linkmark.R
import com.amefure.linkmark.View.Category.CategoryAdapter
import java.net.URL
import java.text.SimpleDateFormat

class LocatorAdapter(locatorList: List<Locator>): RecyclerView.Adapter<LocatorAdapter.MainViewHolder>() {

    private val _locatorList: MutableList<Locator> = locatorList.toMutableList()

    override fun getItemCount(): Int = _locatorList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            // XMLレイアウトファイルからViewオブジェクトを作成
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_locator_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val locator = _locatorList[position]
        val df = SimpleDateFormat("yyyy/MM/dd")
        holder.title.text = locator.title
        holder.memo.text = locator.memo
        holder.createdAt.text = df.format(locator.createdAt).toString()
        holder.title.text = locator.title
    }
    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.locator_title)
        val memo: TextView = itemView.findViewById(R.id.locator_memo)
        val createdAt: TextView = itemView.findViewById(R.id.locator_createdAt)
        val url: TextView = itemView.findViewById(R.id.locator_url)
    }

    private lateinit var listner: onTappedListner

    interface onTappedListner {
        fun onTaped(url: String)
    }

    /**
     * リスナーのセットは使用するFragmentから呼び出して行う
     * リスナーオブジェクトの中に処理が含まれて渡される
     */
    public fun setOnTapedListner(listener: onTappedListner) {
        // 定義した変数listenerに実行したい処理を引数で渡す（CategoryListFragmentで渡している）
        this.listner = listener
    }
}