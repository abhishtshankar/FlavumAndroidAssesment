package com.example.flavumandroid.common

import android.app.Application
import com.example.flavumandroid.data.Preferences
import dagger.hilt.android.HiltAndroidApp

// Custom Application class for the FlavumTest application
private var appContext: FlavumAndroid? = null

@HiltAndroidApp
class FlavumAndroid : Application() {
    // Function called when the application is created
    override fun onCreate() {
        super.onCreate()
        // Initialize the application context and Preferences
        appContext = this
        Preferences.init(this)
    }

    // Companion object function to obtain the application context
    companion object {
        // Get the application context instance
        fun getAppContext(): FlavumAndroid {
            return appContext!!
        }
    }
}
