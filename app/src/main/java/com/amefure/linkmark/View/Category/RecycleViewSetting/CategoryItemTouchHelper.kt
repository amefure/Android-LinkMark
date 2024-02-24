package com.amefure.linkmark.View.Category.RecycleViewSetting

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class CategoryItemTouchHelper(private val adapter: CategoryAdapter): ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.ACTION_STATE_IDLE//ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        var fromPos = viewHolder.adapterPosition
        var toPos = target.adapterPosition
        // FIXME: DBの値を更新してしまうとうまく並び替えが動作しないため見送り
        // adapter.notifyItemMoved(fromPos, toPos)
        // adapter.changeOrder(fromPos, toPos)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }
}