package vn.dl.lmmdfa.util

import android.util.Log

object Console {

    private const val TAG = "NoteConsoleLog"

    @JvmStatic
    fun log(message: String) {
        Log.d(TAG, message)
    }

    @JvmStatic
    fun log(message: String, t: Throwable) {
        Log.d(TAG, message, t)
    }
}