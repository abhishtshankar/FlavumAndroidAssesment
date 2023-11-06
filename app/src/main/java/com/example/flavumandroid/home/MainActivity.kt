package com.example.flavumandroid.home

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import com.example.flavumandroid.base.BaseActivity
import com.example.flavumandroid.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import androidx.navigation.plusAssign
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flavumandroid.R
import com.example.flavumandroid.home.viewmodel.HomeViewModel
import com.example.flavumandroid.home.viewmodel.SharedViewModel
import com.example.flavumandroid.util.widgets.KeepStateNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    private val viewModel: HomeViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun setupView() {
        configBottomNav()
    }

    private fun configBottomNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
        val navigator =
            KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)
        navController.navigatorProvider += navigator
        navController.setGraph(
            R.navigation.nav_main
        )

        binding.bottomNav.apply {
            inflateMenu(R.menu.main_nav_host_menu)
            setupWithNavController(navController)
            setOnItemSelectedListener(navItemSelected)
        }
    }

    private val navItemSelected = NavigationBarView.OnItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_home -> {
                navController.navigate(R.id.navigation_home)
            }

            R.id.navigation_account -> {
                navController.navigate(R.id.navigation_account)
            }
        }
        true
    }

    override fun bindViewModel() {

    }

    override fun onClick(view: View) {
    }

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    companion object {
        fun present(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

}