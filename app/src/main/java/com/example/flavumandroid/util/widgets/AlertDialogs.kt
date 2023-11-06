package com.example.flavumandroid.util.widgets

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import com.example.flavumandroid.data.FlavumPreferences
import com.example.flavumandroid.R
import com.example.flavumandroid.login.LoginActivity

object AlertDialogs {

    private val pref by lazy { FlavumPreferences() }

    fun showLogoutAlert(context: Context) {
        with(Dialog(context)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.logout_alert_dialog)

            val tvCancel = findViewById<AppCompatTextView>(R.id.tvNo)
            val tvOk = findViewById<AppCompatTextView>(R.id.tvYes)

            tvCancel.setOnClickListener {
                dismiss()
            }
            tvOk.setOnClickListener {
                dismiss()
                pref.logout()
                LoginActivity.present(context)
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }

}