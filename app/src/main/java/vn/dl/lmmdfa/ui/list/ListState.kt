package vn.dl.lmmdfa.ui.list

import vn.dl.lmmdfa.base.BaseViewModel
import vn.dl.lmmdfa.model.Todo

data class ListState(
    val todoList: List<Todo> = emptyList(),
    val selectedTodo: Todo? = null,
    val total: Int = 0,
    val currentPage: Int = 1,
    val lastPage: Int = 0
) : BaseViewModel.ViewModelState