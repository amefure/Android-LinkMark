package com.amefure.linkmark.View.Category.RecycleViewSetting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amefure.linkmark.Model.AppThemaColor
import com.amefure.linkmark.Model.Category
import com.amefure.linkmark.R
import com.amefure.linkmark.ViewModel.CategoryViewModel
import java.util.Collections

class CategoryAdapter(private val viewModel: CategoryViewModel,private val categoryList: List<Category>) :RecyclerView.Adapter<CategoryAdapter.MainViewHolder>() , OneTouchHelperCallback.DragAdapter {
    private val _categoryList: MutableList<Category> = categoryList.toMutableList()
    override fun getItemCount(): Int = _categoryList.size

    public fun getItemAtPosition(position: Int) : Category? {
        if (position < 0 || position >= _categoryList.size) {
            return null
        }
        val item = _categoryList[position]
        return item
    }
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(categoryList, fromPosition, toPosition)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_category_item, parent, false)
        return MainViewHolder(view).also { viewHolder ->
            viewHolder.foregroundKnobLayout.setOnClickListener {
                val position = viewHolder.adapterPosition
            }

            viewHolder.deleteButton.setOnClickListener {
                val position = viewHolder.adapterPosition
                notifyItemRemoved(position)
            }
        }
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
        holder.count.text = category.order.toString()

        //　タップイベントを追加
        holder.itemView.setOnClickListener {
            listener.onTapped(category.id)
        }
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , OneTouchHelperCallback.SwipeViewHolder {

        override val foregroundKnobLayout: ViewGroup = itemView.findViewById(R.id.foregroundKnobLayout)
        override val backgroundLeftButtonLayout: ViewGroup = itemView.findViewById(R.id.backgroundLeftButtonLayout)
        override val backgroundRightButtonLayout: ViewGroup = itemView.findViewById(R.id.backgroundRightButtonLayout)
        override val canRemoveOnSwipingFromRight: Boolean get() = true

        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

        val color: View = itemView.findViewById(R.id.category_color)
        val name: TextView = itemView.findViewById(R.id.category_name)
        val count: TextView = itemView.findViewById(R.id.link_count_label)
    }

    private lateinit var listener: onTappedListner
    interface onTappedListner {
        fun onTapped(categoryId: Int)
    }

    /**
     * リスナーのセットは使用するFragmentから呼び出して行う
     * リスナーオブジェクトの中に処理が含まれて渡される
     */
    public fun setOnTappedListner(listener: onTappedListner) {
        // 定義した変数listenerに実行したい処理を引数で渡す（CategoryListFragmentで渡している）
        this.listener = listener
    }

    public fun changeOrder(fromPos: Int, toPos: Int) {
        if (fromPos < 0 || fromPos >= _categoryList.size) { return }
        var category = _categoryList[fromPos]
        category.order = toPos
        viewModel.changeOrder(source = fromPos, destination = toPos)
    }
}

