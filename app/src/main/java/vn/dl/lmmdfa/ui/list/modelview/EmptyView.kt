package vn.dl.lmmdfa.ui.list.modelview

import android.view.View
import vn.dl.lmmdfa.R
import vn.dl.lmmdfa.base.BaseListAdapter

object EmptyView : BaseListAdapter.BaseModelView() {
    override fun id(): String {
        return "emptyModel"
    }

    override fun bindView(view: View, adapterPosition: Int) {

    }

    override fun layout(): Int {
        return R.layout.empty_itemview
    }
}