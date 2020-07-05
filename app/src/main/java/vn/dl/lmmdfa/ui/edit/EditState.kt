package vn.dl.lmmdfa.ui.edit

import vn.dl.lmmdfa.base.BaseViewModel

data class EditState(
    val uuid: String = "",
    val content: CharSequence = "",
    val newNote: Boolean = true
) : BaseViewModel.ViewModelState