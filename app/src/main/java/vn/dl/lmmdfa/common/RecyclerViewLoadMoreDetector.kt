package vn.dl.lmmdfa.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewLoadMoreDetector(
    private val loadMoreInvoker: () -> Unit
) : RecyclerView.OnScrollListener() {

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(this)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
            val adapter = recyclerView.adapter
            layoutManager?.let { lm ->
                val lastVisible = lm.findLastCompletelyVisibleItemPosition()
                if (lastVisible == adapter?.itemCount?.minus(1) && lastVisible != -1) {
                    loadMoreInvoker.invoke()
                }
            }
        }
    }
}