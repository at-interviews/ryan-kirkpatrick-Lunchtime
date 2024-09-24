package com.kirkpatrick.lunchtime

import android.app.Application
import android.util.Log
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LunchtimeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initializePlacesSDK()
    }

    private fun initializePlacesSDK() {
        // Define a variable to hold the Places API key.
        val apiKey = BuildConfig.PLACES_API_KEY

        // Log an error if apiKey is not set.
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("Failed to initialize Places SDK", "No api key")
            return
        }

        // Initialize the SDK
        Places.initialize(applicationContext, apiKey)
    }


}