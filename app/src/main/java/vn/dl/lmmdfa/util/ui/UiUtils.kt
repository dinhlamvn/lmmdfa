package vn.dl.lmmdfa.util.ui

import android.view.View
import androidx.annotation.IntRange
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import com.google.android.material.snackbar.Snackbar

object UiUtils {

    /**
     * Show the snake bar view to the anchor view
     */
    @JvmStatic
    @UiThread
    fun showSnakeBar(
        anchor: View,
        content: CharSequence,
        actionText: CharSequence?,
        actionTextColor: Int = 0,
        @IntRange(from = -2, to = 0) duration: Int,
        actionClick: (() -> Unit)? = null
    ) {
        Snackbar.make(anchor, content, duration).apply {
            if (actionText != null) {
                if (actionTextColor != 0) {
                    setActionTextColor(actionTextColor)
                }
                setAction(actionText) {
                    actionClick?.invoke()
                }
            }
        }.show()
    }

    /**
     * Show the snake bar view to the anchor view
     */
    @JvmStatic
    @UiThread
    fun showSnakeBar(
        anchor: View,
        @StringRes contentRes: Int,
        @StringRes actionTextRes: Int,
        actionTextColor: Int = 0,
        @IntRange(from = -2, to = 0) duration: Int,
        actionClick: (() -> Unit)? = null
    ) {
        val context = anchor.context
        val content = context.getString(contentRes)
        val actionText = context.getString(actionTextRes)
        showSnakeBar(anchor, content, actionText, actionTextColor, duration, actionClick)
    }
}