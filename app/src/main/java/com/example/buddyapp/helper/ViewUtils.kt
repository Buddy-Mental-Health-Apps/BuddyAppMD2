package com.example.buddyapp.helper

import android.os.Build
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager

object ViewUtils {
    fun setupView(window: Window, supportActionBar: androidx.appcompat.app.ActionBar?) {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}