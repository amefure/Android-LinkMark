package com.amefure.linkmark.View.Category.RecycleViewSetting

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amefure.linkmark.Model.Config.AppThemaColor
import com.amefure.linkmark.Model.Database.Category
import com.amefure.linkmark.Model.Database.CategoryWithLocators
import com.amefure.linkmark.R
import com.amefure.linkmark.View.Utility.OneTouchHelperCallback
import com.amefure.linkmark.ViewModel.CategoryViewModel
import java.util.Collections

class CategoryAdapter(
    private val viewModel: CategoryViewModel,
    private val categoryWithLocators: List<CategoryWithLocators>,
    private val context: Context
    ) :RecyclerView.Adapter<CategoryAdapter.MainViewHolder>(), OneTouchHelperCallback.DragAdapter {
    private val _categoryWithLocators: MutableList<CategoryWithLocators> = categoryWithLocators.toMutableList()
    override fun getItemCount(): Int = _categoryWithLocators.size

    public fun getItemAtPosition(position: Int) : CategoryWithLocators? {
        if (position < 0 || position >= _categoryWithLocators.size) {
            return null
        }
        val item = _categoryWithLocators[position]
        return item
    }
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition >= 0 && toPosition >= 0) {
            Collections.swap(categoryWithLocators, fromPosition, toPosition)
            changeOrder(fromPosition, toPosition)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_category_item, parent, false)
        return MainViewHolder(view).also { viewHolder ->
            viewHolder.foregroundKnobLayout.setOnClickListener {
                val position = viewHolder.adapterPosition
            }
        }
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val categoryWithLocators = _categoryWithLocators[position]

        var color: ColorStateList? = null
        try {
            color = AppThemaColor.valueOf(categoryWithLocators.category.color).color(context)
        } catch (e: IllegalArgumentException) {
            color = AppThemaColor.RED.color(context)
        }

        holder.color.backgroundTintList = color
        holder.name.text = categoryWithLocators.category.name.take(13)
        holder.count.text = categoryWithLocators.locators.size.toString()

        holder.editButton.setOnClickListener {
            listener.onEditTapped(categoryWithLocators.category.id)
        }

        holder.deleteButton.setOnClickListener {
            listener.onDeleteTapped(categoryWithLocators.category) {
                if (it) {
                    val position = holder.adapterPosition
                    notifyItemRemoved(position)
                }
            }
        }
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OneTouchHelperCallback.SwipeViewHolder {

        override val foregroundKnobLayout: ViewGroup = itemView.findViewById(R.id.foregroundKnobLayout)
        override val backgroundLeftButtonLayout: ViewGroup = itemView.findViewById(R.id.backgroundLeftButtonLayout)
        override val backgroundRightButtonLayout: ViewGroup = itemView.findViewById(R.id.backgroundRightButtonLayout)
        override val canRemoveOnSwipingFromRight: Boolean get() = false

        val editButton: ImageButton = itemView.findViewById(R.id.editButton)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

        val color: View = itemView.findViewById(R.id.category_color)
        val name: TextView = itemView.findViewById(R.id.category_name)
        val count: TextView = itemView.findViewById(R.id.link_count_label)
    }

    private lateinit var listener: onTappedListner
    interface onTappedListner {
        fun onEditTapped(categoryId: Int)
        fun onDeleteTapped(category: Category, completion: (result: Boolean) -> Unit)
    }

    /**
     * リスナーのセットは使用するFragmentから呼び出して行う
     * リスナーオブジェクトの中に処理が含まれて渡される
     */
    public fun setOnTappedListner(listener: onTappedListner) {
        // 定義した変数listenerに実行したい処理を引数で渡す（CategoryListFragmentで渡している）
        this.listener = listener
    }

    /**
     * ローカルデータorderプロパティ更新処理
     */
    public fun changeOrder(fromPos: Int, toPos: Int) {
        if (fromPos < 0 || fromPos >= _categoryWithLocators.size) { return }
        var categoryWithLocators = _categoryWithLocators[fromPos]
        categoryWithLocators.category.order = toPos
        viewModel.changeOrder(source = fromPos, destination = toPos, _categoryWithLocators)
    }
}

