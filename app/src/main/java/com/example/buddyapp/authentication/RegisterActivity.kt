package com.example.buddyapp.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.buddyapp.R
import com.example.buddyapp.data.api.ApiConfig
import com.example.buddyapp.data.api.RegisterResponse
import com.example.buddyapp.data.ds.Register
import com.example.buddyapp.data.ds.RegisterRepository
import com.example.buddyapp.data.viewmodelfactory.RegisterViewModelFactory
import com.example.buddyapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(RegisterRepository(ApiConfig.getApiService()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        observeRegisterResponse()

        showLoading(false)

        val infoTextView2 = findViewById<TextView>(R.id.infoTextView2)
        infoTextView2.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

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
        binding.registerButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (validateInput(name, email, password)) {
                val register = Register(name, email, password)

                showLoading(true)

                registerViewModel.registerUser(register)
                binding.registerButton.isEnabled = false
            } else {
                showAlertDialog("Error", "Semua kolom harus diisi dengan benar.", false)
            }
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }

    private fun observeRegisterResponse() {
        registerViewModel.registerResponse.observe(
            this,
            Observer { registerResponse: RegisterResponse? ->
                showLoading(false)
                binding.registerButton.isEnabled = true

                registerResponse?.let {
                    if (!registerResponse.error!!) {
                        showAlertDialog(
                            title = "Yeah!",
                            message = registerResponse.message ?: "Registrasi berhasil.",
                            isSuccess = true
                        )
                    } else {
                        val errorMessage = if (registerResponse.message?.contains("Email sudah terdaftar", true) == true) {
                            "Email sudah terdaftar. Silakan gunakan email lain."
                        } else {
                            registerResponse.message ?: "Terjadi kesalahan. Silakan coba lagi."
                        }

                        showAlertDialog(
                            title = "Error",
                            message = errorMessage,
                            isSuccess = false
                        )
                    }
                } ?: run {
                    showAlertDialog(
                        title = "Error",
                        message = "Response kosong. Silakan coba lagi.",
                        isSuccess = false
                    )
                }
            }
        )
    }

    private fun showAlertDialog(title: String, message: String, isSuccess: Boolean) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(if (isSuccess) "Masuk" else "OK") { _, _ ->
                if (isSuccess) {
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    binding.registerButton.isEnabled = true
                    showLoading(false)
                }
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
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}
