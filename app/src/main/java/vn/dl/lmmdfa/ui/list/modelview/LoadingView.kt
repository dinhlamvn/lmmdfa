package vn.dl.lmmdfa.ui.list.modelview

import android.view.View
import vn.dl.lmmdfa.R
import vn.dl.lmmdfa.base.BaseListAdapter

data class LoadingView(val id: String) : BaseListAdapter.BaseModelView() {

    override fun id(): String {
        return "loading_$id"
    }

    override fun bindView(view: View, adapterPosition: Int) {

    }

    override fun layout(): Int {
        return R.layout.itemview_loading
    }
}