package vn.dl.lmmdfa.ui.list

import android.view.View
import vn.dl.lmmdfa.base.BaseModelViewController
import vn.dl.lmmdfa.ui.list.modelview.EmptyView
import vn.dl.lmmdfa.ui.list.modelview.LoadingView
import vn.dl.lmmdfa.ui.list.modelview.TodoView

class ListController : BaseModelViewController<ListState>() {

    override fun buildModelView(state: ListState) {
        if (state.showLoading) {
            LoadingView("refreshLoading").addToController()
            return
        }

        val noteList = state.todoList

        if (noteList.isEmpty()) {
            EmptyView.addToController()
        } else {
            noteList.map { todo ->
                TodoView(
                    id = todo.uuid,
                    content = todo.content,
                    createdAt = todo.createdAt,
                    isSelected = todo.isSelected,
                    onViewClick = View.OnClickListener {
//                        val action =
//                            ListFragmentDirections.actionListFragmentToEditFragment(todo.uuid)
//                        findNavController().navigate(action)
                    }
                )
            }.addToController()
        }
    }
}