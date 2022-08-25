package com.veit.app.weatherino.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class DeleteOnSwipeHelper: ItemTouchHelper(DeleteOnSwipeCallback())

internal class DeleteOnSwipeCallback: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // is not supported
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        (viewHolder as? Swipeable)?.onSwiped()
    }
}

interface Swipeable {
    fun onSwiped()
}

interface SwipeAction<T> {
    fun onItemSwiped(item: T)
}