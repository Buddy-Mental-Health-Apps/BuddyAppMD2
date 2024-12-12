package com.example.buddyapp.ui.authentication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.buddyapp.R
import com.example.buddyapp.ui.authentication.otp.EmailSender
import com.example.buddyapp.ui.authentication.otp.OtpDialogFragment
import com.example.buddyapp.data.api.ApiConfig
import com.example.buddyapp.data.ds.Register
import com.example.buddyapp.data.ds.RegisterRepository
import com.example.buddyapp.data.viewmodelfactory.RegisterViewModelFactory
import com.example.buddyapp.databinding.ActivityRegisterBinding
import com.example.buddyapp.helper.ViewUtils

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(RegisterRepository(ApiConfig.getApiService()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewUtils.setupView(window, supportActionBar)
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

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            // Cek apakah semua field terisi
            if (validateInput(name, email, password)) {
                val register = Register(name, email, password)

                showLoading(true)
                registerViewModel.registerUser(register)
                binding.registerButton.isEnabled = false
            } else {
                showDataNotFoundDialog()
            }
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }

    private fun showDataNotFoundDialog() {
        val dialogView = layoutInflater.inflate(R.layout.alert_dialog_data_not_found, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<TextView>(R.id.option_ok).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun showOtpDialog(correctOtp: String) {
        val otpDialog = OtpDialogFragment(correctOtp = correctOtp) {
            showAlertDialog(isSuccess = true, message = "Registrasi berhasil diverifikasi!")
        }
        otpDialog.show(supportFragmentManager, "OtpDialog")
    }

    private fun observeRegisterResponse() {
        registerViewModel.registerResponse.observe(this) { registerResponse ->
            showLoading(false)
            binding.registerButton.isEnabled = true

            registerResponse?.let {
                if (!registerResponse.error!!) {
                    val email = binding.edRegisterEmail.text.toString()
                    val otp = (100000..999999).random().toString()

                    Thread {
                        try {
                            val emailAlreadyExists = checkEmailExists(email)
                            if (emailAlreadyExists) {
                                runOnUiThread {
                                    showLoading(false)
                                    showAlertDialog(
                                        isSuccess = false,
                                        message = "Email sudah terdaftar. Silakan gunakan email lain."
                                    )
                                    binding.registerButton.isEnabled = true
                                }
                            } else {
                                try {
                                    EmailSender.sendEmail(
                                        toEmail = email,
                                        subject = "Kode OTP Anda",
                                        messageBody = "Kode OTP Anda adalah: $otp. Berlaku selama 5 menit."
                                    )
                                    runOnUiThread {
                                        showOtpDialog(otp)
                                    }
                                } catch (e: Exception) {
                                    runOnUiThread {
                                        showLoading(false)
                                        showAlertDialog(
                                            isSuccess = false,
                                            message = "Gagal mengirim OTP: ${e.message}"
                                        )
                                        binding.registerButton.isEnabled = true
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                showLoading(false)
                                showAlertDialog(
                                    isSuccess = false,
                                    message = "Terjadi kesalahan saat memeriksa email."
                                )
                                binding.registerButton.isEnabled = true
                            }
                        }
                    }.start()

                    // Simpan nama pengguna ke SharedPreferences setelah registrasi berhasil
                    val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("name", binding.edRegisterName.text.toString())
                    editor.apply()

                } else {
                    val errorMessage =
                        registerResponse.message ?: "Terjadi kesalahan. Silakan coba lagi."
                    showAlertDialog(isSuccess = false, message = errorMessage)
                }
            } ?: run {
                showAlertDialog(isSuccess = false, message = "Response kosong. Silakan coba lagi.")
            }
        }
    }

    // Metode untuk memeriksa apakah email sudah terdaftar
    private fun checkEmailExists(email: String): Boolean {
        val registeredEmails = listOf("email@example.com", "user@domain.com")
        return registeredEmails.contains(email)
    }

    private fun showAlertDialog(isSuccess: Boolean, message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(if (isSuccess) "Success" else "Error")
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
        val infoTextView = ObjectAnimator.ofFloat(binding.infoTextView, View.ALPHA, 1f).setDuration(100)
        val infoTextView2 = ObjectAnimator.ofFloat(binding.infoTextView2, View.ALPHA, 1f).setDuration(100)
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
                infoTextView,
                infoTextView2,
                signup
            )
            startDelay = 100
        }.start()
    }
}
