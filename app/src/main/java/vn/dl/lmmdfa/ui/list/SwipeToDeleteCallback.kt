package vn.dl.lmmdfa.ui.list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import vn.dl.lmmdfa.view.EmptyNoteView

class SwipeToDeleteCallback(
    private val onSwiped: (Int) -> Unit
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.START or ItemTouchHelper.END
) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwiped.invoke(viewHolder.adapterPosition)
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder.itemView is EmptyNoteView) 0 else super.getSwipeDirs(
            recyclerView,
            viewHolder
        )
    }
}