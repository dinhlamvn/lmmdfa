package vn.dl.lmmdfa.ui.edit

import vn.dl.lmmdfa.base.BaseViewModel

data class EditState(
    val todoId: String = "",
    val content: CharSequence = "",
    val newTodo: Boolean = true
) : BaseViewModel.ViewModelState