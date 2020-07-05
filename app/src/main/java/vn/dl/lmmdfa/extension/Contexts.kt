package vn.dl.lmmdfa.extension

import android.content.Context
import android.content.ContextWrapper
import vn.dl.lmmdfa.App

fun Context.asApp(): App {
    val contextWrapper = this as? ContextWrapper ?: return this.applicationContext as App
    return contextWrapper.baseContext.applicationContext as App
}