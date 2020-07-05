package vn.dl.lmmdfa.ui.list.modelview

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import vn.dl.lmmdfa.R
import vn.dl.lmmdfa.base.BaseListAdapter
import vn.dl.lmmdfa.extension.bindView
import vn.dl.lmmdfa.util.Formats

data class TodoView(
    val id: String,
    val content: String,
    val createdAt: Long,
    val isSelected: Boolean = false,
    val onViewClick: View.OnClickListener?
) : BaseListAdapter.BaseModelView() {

    companion object {
        private const val MAX_TITLE_LENGTH = 100
    }

    override fun id(): String {
        return "NoteModel_$id"
    }

    override fun bindView(view: View, adapterPosition: Int) {
        val tvTitle: TextView by view.bindView(R.id.text_view_title)
        val tvSummary: TextView by view.bindView(R.id.text_view_content_summary)
        val vMore: View by view.bindView(R.id.view_more)
        //val tvCreated: TextView by view.bindView(R.id.text_view_created)

        if (isSelected) {
            view.setBackgroundColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.color_selected
                )
            )
        } else {
            //view.background = null
        }

        val title = getTitle()
        val content = getContentSummary()

        applyTitle(tvTitle, title)
        applyContentSummary(tvSummary, content)

        vMore.setOnClickListener {

        }

        //applyCreateAt(tvCreated)

        applyListener(view, onViewClick)
    }

    override fun layout(): Int {
        return R.layout.itemview_note_element
    }

    private fun getTitle(): String {
        val firstNewLineIndex = content.indexOfFirst { c -> c == '\n' }

        if (firstNewLineIndex in 0..MAX_TITLE_LENGTH) {
            return content.substring(0, firstNewLineIndex)
        }

        if (content.length < MAX_TITLE_LENGTH) {
            return content
        }

        return content.substring(
            0,
            MAX_TITLE_LENGTH
        )
    }

    private fun getContentSummary(): String {
        val firstNewLineIndex = content.indexOfFirst { c -> c == '\n' }

        if (firstNewLineIndex in 0..MAX_TITLE_LENGTH) {
            return content.substring(firstNewLineIndex + 1)
        }

        if (content.length < MAX_TITLE_LENGTH) {
            return content
        }

        return content.substring(MAX_TITLE_LENGTH + 1)
    }

    private fun applyTitle(textView: TextView, title: String) {
        textView.text = title
    }

    private fun applyContentSummary(textView: TextView, content: String) {
        textView.text = content
    }

    private fun applyCreateAt(textView: TextView) {
        textView.text = Formats.formatDate(createdAt)
    }

    private fun applyListener(view: View, listener: View.OnClickListener?) {
        view.setOnClickListener(listener)
    }
}