package com.example.buddyapp.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.buddyapp.MainActivity
import com.example.buddyapp.R

class LoginActivity : AppCompatActivity() {

    private lateinit var emailFieldLogin: EditText
    private lateinit var passwordFieldLogin: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        emailFieldLogin = findViewById(R.id.ed_login_email)
        passwordFieldLogin = findViewById(R.id.ed_login_password)

        val infoTextViewLogin2 = findViewById<TextView>(R.id.infoTextViewLogin2)
        infoTextViewLogin2.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        val registerButton = findViewById<Button>(R.id.loginButton)
        registerButton.setOnClickListener {
            if (areFieldsValid()) {
                showRegisterDialog()
            } else {
                showDataNotFoundDialog()
            }
        }
    }

    private fun areFieldsValid(): Boolean {
        return emailFieldLogin.text.isNotBlank() &&
                passwordFieldLogin.text.isNotBlank()
    }

    private fun showRegisterDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_login, null)
        val optionLogin = dialogView.findViewById<TextView>(R.id.option_login_in_login)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        optionLogin.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        alertDialog.show()
    }

    private fun showDataNotFoundDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_data_not_found, null)
        val optionOk = dialogView.findViewById<TextView>(R.id.option_ok)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        optionOk.setOnClickListener {
            alertDialog.dismiss() // Menutup dialog
        }

        alertDialog.show()
    }
}