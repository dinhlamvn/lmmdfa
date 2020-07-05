package vn.dl.lmmdfa.extension

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

fun <T : View> Activity.bindView(@IdRes viewId: Int): Lazy<T> {
    return lazy { findViewById<T>(viewId) as T }
}

fun <T : View> Fragment.bindView(@IdRes viewId: Int): Lazy<T> {
    return lazy { view?.findViewById(viewId) as T }
}

fun <T : View> View.bindView(@IdRes viewId: Int): Lazy<T> {
    return lazy { findViewById<T>(viewId) }
}