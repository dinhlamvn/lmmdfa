package vn.dl.lmmdfa.ui.list

import vn.dl.lmmdfa.base.BaseViewModel
import vn.dl.lmmdfa.model.Note

data class ListState(
    val noteList: List<Note> = emptyList(),
    val selectedNote: Note? = null,
    val total: Int = 0,
    val currentPage: Int = 1,
    val lastPage: Int = 0
) : BaseViewModel.ViewModelState