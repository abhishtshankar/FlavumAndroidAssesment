package com.example.flavumandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.example.flavumandroid.data.FlavumPreferences
import com.example.flavumandroid.util.widgets.hideSoftKeyboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Abstract base class for Fragments in the application
abstract class BaseFragment<B : ViewBinding> : Fragment(), BaseFragmentCallbacks {

    // ViewBinding instance for the fragment's layout
    lateinit var binding: B

    // Flag to track whether the root view has been initialized
    var hasInitializedRootView = false

    // Saved instance state bundle
    var savedInstanceStateBundle: Bundle? = null

    // Shared preferences instance using FlavumPreferences
    val pref by lazy { FlavumPreferences() }

    // Function called when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the ViewBinding instance for the fragment's layout
        binding = getViewBinding()
        // Initialize the ViewModel (if implemented by the fragment)
        (this as? BaseFragmentCallbacks)?.let { initViewModel(savedInstanceState) }
        savedInstanceStateBundle = savedInstanceState
    }

    // Provide the root view of the fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    // Function called when the view has been created
    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the ViewModel, setup the view, bind view events, and bind the ViewModel (if implemented by the fragment)
        (this as? BaseFragmentCallbacks)?.let {
            setupView()
            bindViewEvents()
            bindViewModel()
        }
    }

    // Initialize the ViewModel (if implemented by the fragment)
    override fun initViewModel(savedInstanceState: Bundle?) {}

    // Bind view events (can be overridden by subclasses)
    @CallSuper
    override fun bindViewEvents() {
        // Attach a click listener to the view to hide the soft keyboard
        requireNotNull(view).setOnClickListener {
            requireActivity().hideSoftKeyboard()
        }
    }

    // Extension function to bind a Flow to a lifecycle with repeatOnLifecycle
    protected inline infix fun <T> Flow<T>.bindTo(crossinline action: (T) -> Unit) {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    collect { action(it) }
                }
            }
        }
    }

    // OnClickListener for view components in the fragment
    protected val onClickListener = View.OnClickListener {
        requireActivity().hideSoftKeyboard()
        // Handle the click event (must be implemented by subclasses)
        onClick(it)
    }

    // Handle the click event for view components (must be implemented by subclasses)
    protected abstract fun onClick(view: View)

    // Function to obtain the ViewBinding instance (must be implemented by subclasses)
    abstract fun getViewBinding(): B

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
