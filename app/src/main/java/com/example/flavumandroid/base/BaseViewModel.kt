package com.example.flavumandroid.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flavumandroid.common.FlavumAndroid
import com.example.flavumandroid.data.FlavumPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

// Base class for ViewModels in the application
open class BaseViewModel : ViewModel() {

    // Application context obtained from FlavumAndroid
    val context by lazy { FlavumAndroid.getAppContext() }

    // Shared preferences instance using FlavumPreferences
    val pref by lazy { FlavumPreferences() }

    // Execute a coroutine job with the provided dispatcher
    fun execute(dispatcher: CoroutineContext = Dispatchers.Main, job: suspend () -> Unit) =
        viewModelScope.launch(dispatcher) {
            job.invoke()
        }

    // Ignore a coroutine exception based on a custom condition
    fun ignoreCoroutineException(throwable: Throwable, callback: () -> Unit) {
        if (throwable.message?.contains("Standalone") != true) {
            callback.invoke()
        }
    }
}
