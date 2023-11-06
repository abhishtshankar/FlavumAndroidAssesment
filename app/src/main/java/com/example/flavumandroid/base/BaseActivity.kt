package com.example.flavumandroid.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.flavumandroid.data.FlavumPreferences
import com.example.flavumandroid.util.widgets.hideSoftKeyboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Abstract base class for activities in the application
abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    // ViewBinding instance for the activity's layout
    lateinit var binding: B

    // Shared preferences instance using FlavumPreferences
    val pref by lazy { FlavumPreferences() }

    // Function called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pre-initialization logic
        preInit()
        // Obtain the ViewBinding instance for the activity's layout
        binding = getViewBinding()
        setContentView(binding.root)
        // Perform initialization logic
        init()
        // Setup the view components
        setupView()
        // Bind user interface events
        bindViewEvents()
        // Bind the ViewModel to the View
        bindViewModel()
    }

    // Initialize the activity (can be overridden by subclasses)
    private fun init() {
    }

    // Pre-initialization logic (can be overridden by subclasses)
    protected open fun preInit() {

    }

    // Setup the view components (must be implemented by subclasses)
    protected abstract fun setupView()

    // Bind user interface events (can be overridden by subclasses)
    protected open fun bindViewEvents() {
        requireNotNull(binding.root).setOnClickListener {
            // Utility function to hide the soft keyboard
            hideSoftKeyboard()
        }
    }

    // Bind the ViewModel to the View (must be implemented by subclasses)
    protected abstract fun bindViewModel()

    // Function to obtain the ViewBinding instance (must be implemented by subclasses)
    abstract fun getViewBinding(): B

    // OnClickListener for view components in the activity
    protected val onClickListener = View.OnClickListener {
        hideSoftKeyboard()
        // Handle the click event (must be implemented by subclasses)
        onClick(it)
    }

    // Handle the click event for view components (must be implemented by subclasses)
    protected abstract fun onClick(view: View)

    // Run a job with a delay using coroutines and lifecycleScope
    fun runDelayed(delayMilliSec: Long, job: suspend () -> Unit) =
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                delay(delayMilliSec)
                withContext(Dispatchers.Main) {
                    job.invoke()
                }
            }
        }
}
