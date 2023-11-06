package com.example.flavumandroid.base

import android.os.Bundle

// An interface to define callbacks for common fragment functionality
interface BaseFragmentCallbacks {

    // Initialize the ViewModel for the fragment, if needed
    fun initViewModel(savedInstanceState: Bundle?)

    // Setup the view components and UI layout for the fragment
    fun setupView()

    // Bind view events and user interactions for the fragment
    fun bindViewEvents()

    // Bind the ViewModel to the fragment's view
    fun bindViewModel()
}
