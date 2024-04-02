package com.xizz.chatstreamtesting

import android.app.Application
import android.util.Log

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.e("xizz", "MainApplication.onCreate()")
    }
}