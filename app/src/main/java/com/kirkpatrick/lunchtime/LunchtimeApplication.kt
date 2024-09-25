package com.kirkpatrick.lunchtime

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LunchtimeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}