package vn.dl.lmmdfa.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView

class TodoView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(ctx, attrs, defStyle) {

    init {
        initialize()
    }

    private fun initialize() {
        val textView = TextView(context)
    }
}