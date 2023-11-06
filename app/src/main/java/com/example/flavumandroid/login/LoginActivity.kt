package com.example.flavumandroid.login

import android.content.Context
import android.content.Intent
import android.view.View
import com.example.flavumandroid.base.BaseActivity
import com.example.flavumandroid.databinding.ActivityLoginBinding
import com.example.flavumandroid.home.MainActivity
import com.example.flavumandroid.util.widgets.Constants

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun setupView() {

    }

    override fun bindViewModel() {

    }

    override fun bindViewEvents() {
        super.bindViewEvents()
        binding.doctor.setOnClickListener(onClickListener)
        binding.patient.setOnClickListener(onClickListener)
    }

    override fun onClick(view: View) {
        when (view) {
            binding.doctor -> {
                pref.userType = Constants.DOCTOR
                pref.doctorName = "I'm Doctor"
                MainActivity.present(this)
            }

            binding.patient -> {
                pref.userType = Constants.PATIENT
                pref.patientName = "I'm Patient"
                MainActivity.present(this)
            }
        }
    }

    override fun getViewBinding() = ActivityLoginBinding.inflate(layoutInflater)

    companion object {
        fun present(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}