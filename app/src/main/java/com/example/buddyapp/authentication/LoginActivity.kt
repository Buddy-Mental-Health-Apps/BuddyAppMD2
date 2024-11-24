package com.example.buddyapp.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.buddyapp.MainActivity
import com.example.buddyapp.data.viewmodelfactory.ViewModelFactory
import com.example.buddyapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        observeViewModel()
        showLoading(false)
    }

    private fun setupView() {
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

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                showLoading(true)
                viewModel.login(email, password)
                binding.loginButton.isEnabled = false
            } else {
                showAlertDialog("Error", "Semua kolom harus diisi dengan benar.", false)
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.loginResult.collect { result ->
                result?.let {
                    it.onSuccess { userModel ->
                        viewModel.apiMessage.collect { message ->
                            message?.let {
                                Toast.makeText(this@LoginActivity, it, Toast.LENGTH_LONG).show()
                            }
                        }
                    }.onFailure { exception ->
                        showAlertDialog(exception.message ?: "Terjadi kesalahan", "Password atau Email Salah, Silakan Coba Lagi.", false)
                        binding.loginButton.isEnabled = true
                        showLoading(false)
                    }
                }
            }
        }
    }


    private fun showAlertDialog(title: String, message: String, isSuccess: Boolean, onPositiveAction: (() -> Unit)? = null) {
        showLoading(false)
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(if (isSuccess) "Lanjut" else "OK") { _, _ ->
                onPositiveAction?.invoke()
            }
            setCancelable(false)
            create()
            show()
        }
    }



    private fun showLoading(isLoading: Boolean) {
        binding.pBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 7000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }
}
