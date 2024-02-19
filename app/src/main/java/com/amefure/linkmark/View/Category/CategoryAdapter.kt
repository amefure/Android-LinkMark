package com.amefure.linkmark.View.Category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amefure.linkmark.Model.AppThemaColor
import com.amefure.linkmark.Model.Category
import com.amefure.linkmark.R

class CategoryAdapter(categoryList: List<Category>) :RecyclerView.Adapter<CategoryAdapter.MainViewHolder>() {
    private val _categoryList: MutableList<Category> = categoryList.toMutableList()
    override fun getItemCount(): Int = _categoryList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_category_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val category = _categoryList[position]

        val drawable = when(category.color) {
            AppThemaColor.RED.name -> R.drawable.circle_red
            AppThemaColor.YELLOW.name -> R.drawable.circle_yellow
            AppThemaColor.BLUE.name -> R.drawable.circle_blue
            AppThemaColor.GREEN.name -> R.drawable.circle_green
            AppThemaColor.PURPLE.name -> R.drawable.circle_purple
            else -> R.drawable.circle_red
        }
        holder.color.setBackgroundResource(drawable)
        holder.name.text = category.name
        holder.count.text = "3"

        //　タップイベントを追加Z
        holder.itemView.setOnClickListener {
            listener.onTaped(category.id)
        }
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val color: View = itemView.findViewById(R.id.category_color)
        val name: TextView = itemView.findViewById(R.id.category_name)
        val count: TextView = itemView.findViewById(R.id.link_count_label)
    }

    private lateinit var listener: onTapedListner
    interface onTapedListner {
        fun onTaped(categoryId: Int)
    }

    /**
     * リスナーのセットは使用するFragmentから呼び出して行う
     * リスナーオブジェクトの中に処理が含まれて渡される
     */
    public fun setOnTapedListner(listener: onTapedListner) {
        // 定義した変数listenerに実行したい処理を引数で渡す（CategoryListFragmentで渡している）
        this.listener = listener
    }
}

