package vn.dl.lmmdfa.model

data class Note(
    val uuid: String,
    val content: String,
    val createdAt: Long,
    val isSelected: Boolean = false
)