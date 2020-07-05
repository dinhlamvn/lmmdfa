package vn.dl.lmmdfa.common.modelview

import android.view.View
import vn.dl.lmmdfa.base.BaseListAdapter

object NullModelView : BaseListAdapter.BaseModelView() {

    override fun id(): String {
        return "null_view"
    }

    override fun bindView(view: View, adapterPosition: Int) {

    }

    override fun layout(): Int {
        return 0
    }
}