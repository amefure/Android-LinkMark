package com.amefure.linkmark.View.Locator.RecycleViewSetting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amefure.linkmark.Model.Database.Locator
import com.amefure.linkmark.R
import com.amefure.linkmark.View.Utility.OneTouchHelperCallback
import com.amefure.linkmark.ViewModel.LocatorViewModel
import java.text.SimpleDateFormat
import java.util.Collections

class LocatorAdapter(
    private val viewModel: LocatorViewModel,
    private val locatorList: List<Locator>,
    private val context: Context
): RecyclerView.Adapter<LocatorAdapter.MainViewHolder>(), OneTouchHelperCallback.DragAdapter {

    private val _locatorList: MutableList<Locator> = locatorList.toMutableList()

    override fun getItemCount(): Int = _locatorList.size

    public fun getItemAtPosition(position: Int) : Locator? {
        if (position < 0 || position >= _locatorList.size) {
            return null
        }
        val item = _locatorList[position]
        return item
    }
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition >= 0 && toPosition >= 0) {
            Collections.swap(locatorList, fromPosition, toPosition)
            changeOrder(fromPosition, toPosition)
            notifyDataSetChanged()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_locator_item, parent, false)
        return MainViewHolder(view).also { viewHolder ->
            viewHolder.foregroundKnobLayout.setOnClickListener {
                val position = viewHolder.adapterPosition
            }
        }
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val locator = _locatorList[position]
        val df = SimpleDateFormat("yyyy/MM/dd")
        holder.title.text = locator.title
        holder.memo.text = locator.memo
        holder.createdAt.text = df.format(locator.createdAt).toString()
        holder.url.text = locator.url


        holder.editButton.setOnClickListener {
            listener.onEditTapped(locator.id)
        }

        holder.deleteButton.setOnClickListener {
            listener.onDeleteTapped(locator) {
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


        val title: TextView = itemView.findViewById(R.id.locator_title)
        val memo: TextView = itemView.findViewById(R.id.locator_memo)
        val createdAt: TextView = itemView.findViewById(R.id.locator_createdAt)
        val url: TextView = itemView.findViewById(R.id.locator_url)
    }

    private lateinit var listener: onTappedListner
    interface onTappedListner {
        fun onEditTapped(locatorId: Int)
        fun onDeleteTapped(locator: Locator, completion: (result: Boolean) -> Unit)
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
        if (fromPos < 0 || fromPos >= _locatorList.size) { return }
        var category = _locatorList[fromPos]
        category.order = toPos
        viewModel.changeOrder(source = fromPos, destination = toPos, _locatorList)
    }
}