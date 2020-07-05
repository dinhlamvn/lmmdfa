package vn.dl.lmmdfa.ui.list.modelview

import android.view.View
import android.widget.TextView
import vn.dl.lmmdfa.R
import vn.dl.lmmdfa.base.BaseListAdapter

data class LoadingView(val id: String) : BaseListAdapter.BaseModelView() {

    override fun id(): String {
        return "loading_$id"
    }

    override fun bindView(view: View, adapterPosition: Int) {
        val context = view.context
        val textViewLoading: TextView = view.findViewById(R.id.text_view_loading)
        textViewLoading.setText(context.getString(R.string.loading))
    }

    override fun layout(): Int {
        return R.layout.loading_itemview
    }
}