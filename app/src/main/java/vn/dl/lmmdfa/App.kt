package vn.dl.lmmdfa

import android.app.Application
import vn.dl.lmmdfa.util.Console

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Console.log("App is initialized.")
    }
}