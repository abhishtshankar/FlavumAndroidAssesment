package com.example.flavumandroid.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

// Abstract class for managing preferences
abstract class Preferences {

    // Companion object to handle initialization and context reference
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        fun init(context: Context) {
            Companion.context = context
        }
    }

    // SharedPreferences instance for managing preferences
    private val prefs: SharedPreferences by lazy {
        if (context != null) context!!.getSharedPreferences(
            javaClass.simpleName,
            Context.MODE_PRIVATE
        )
        else throw IllegalStateException("Context was not initialized. Call Preferences.init(context) before using it")
    }

    // Listeners for preference changes
    private val listeners = mutableListOf<SharedPrefsListener>()

    // Abstract class for defining preference delegates
    abstract class PrefDelegate<T>(val prefKey: String?) {
        abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
        abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
    }

    // Interface for shared preferences listeners
    interface SharedPrefsListener {
        fun onSharedPrefChanged(property: KProperty<*>)
    }

    // Property delegate to manage string preferences
    fun stringPref(prefKey: String? = null, defaultValue: String? = null) =
        StringPrefDelegate(prefKey, defaultValue)

    inner class StringPrefDelegate(prefKey: String? = null, private val defaultValue: String?) :
        PrefDelegate<String?>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>): String? =
            prefs.getString(prefKey ?: property.name, defaultValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
            prefs.edit().putString(prefKey ?: property.name, value).apply()
            onPrefChanged(property)
        }
    }

    // Property delegate to manage int preferences
    fun intPref(prefKey: String? = null, defaultValue: Int = 0) =
        IntPrefDelegate(prefKey, defaultValue)

    inner class IntPrefDelegate(prefKey: String? = null, private val defaultValue: Int) :
        PrefDelegate<Int>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>) =
            prefs.getInt(prefKey ?: property.name, defaultValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            prefs.edit().putInt(prefKey ?: property.name, value).apply()
            onPrefChanged(property)
        }
    }

    // Property delegate to manage float preferences
    fun floatPref(prefKey: String? = null, defaultValue: Float = 0f) =
        FloatPrefDelegate(prefKey, defaultValue)

    inner class FloatPrefDelegate(prefKey: String? = null, private val defaultValue: Float) :
        PrefDelegate<Float>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>) =
            prefs.getFloat(prefKey ?: property.name, defaultValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) {
            prefs.edit().putFloat(prefKey ?: property.name, value).apply()
            onPrefChanged(property)
        }
    }

    // Property delegate to manage boolean preferences
    fun booleanPref(prefKey: String? = null, defaultValue: Boolean = false) =
        BooleanPrefDelegate(prefKey, defaultValue)

    inner class BooleanPrefDelegate(prefKey: String? = null, private val defaultValue: Boolean) :
        PrefDelegate<Boolean>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>) =
            prefs.getBoolean(prefKey ?: property.name, defaultValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            prefs.edit().putBoolean(prefKey ?: property.name, value).apply()
            onPrefChanged(property)
        }
    }

    // Property delegate to manage long preferences
    fun longPref(prefKey: String? = null, defaultValue: Long = 0L) =
        LongPrefDelegate(prefKey, defaultValue)

    inner class LongPrefDelegate(prefKey: String? = null, private val defaultValue: Long) :
        PrefDelegate<Long>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>) =
            prefs.getLong(prefKey ?: property.name, defaultValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
            prefs.edit().putLong(prefKey ?: property.name, value).apply()
            onPrefChanged(property)
        }
    }

    // Function to log out the user by clearing preferences
    fun logoutUser() {
        //prefs.edit().clear().apply()
    }

    // Function to notify listeners about preference changes
    private fun onPrefChanged(property: KProperty<*>) {
        listeners.forEach { it.onSharedPrefChanged(property) }
    }
}
