package com.example.flavumandroid.home.view

import android.view.View
import com.example.flavumandroid.base.BaseFragment
import com.example.flavumandroid.databinding.FragmentAccountBinding
import com.example.flavumandroid.util.widgets.Constants
import com.example.flavumandroid.util.widgets.AlertDialogs

class AccountFragment : BaseFragment<FragmentAccountBinding>() {

    // Set up the view based on the user type (Doctor or Patient)
    override fun setupView() {
        when (pref.userType) {
            Constants.DOCTOR -> {
                binding.userName.text = pref.doctorName
            }

            Constants.PATIENT -> {
                binding.userName.text = pref.patientName
            }

            else -> {
                // Handle other cases if needed
            }
        }
    }

    // Bind view events, including the logout button
    override fun bindViewEvents() {
        super.bindViewEvents()
        binding.logout.setOnClickListener(onClickListener)
    }

    // Handle click events, e.g., Logout button click
    override fun onClick(view: View) {
        when (view) {
            binding.logout -> {
                // Show a logout confirmation dialog
                AlertDialogs.showLogoutAlert(requireContext())
            }
        }
    }

    // Bind ViewModel if needed
    override fun bindViewModel() {
        // Implement ViewModel binding if necessary
    }

    // Inflate the view binding for the fragment
    override fun getViewBinding() = FragmentAccountBinding.inflate(layoutInflater)
}
