package com.example.buddyapp.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.buddyapp.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var usernameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        usernameField = findViewById(R.id.ed_register_name)
        emailField = findViewById(R.id.ed_register_email)
        passwordField = findViewById(R.id.ed_register_password)

        val infoTextView2 = findViewById<TextView>(R.id.infoTextView2)
        infoTextView2.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            if (areFieldsValid()) {
                showRegisterDialog()
            } else {
                showDataNotFoundDialog()
            }
        }
    }

    private fun areFieldsValid(): Boolean {
        return usernameField.text.isNotBlank() &&
                emailField.text.isNotBlank() &&
                passwordField.text.isNotBlank()
    }

    private fun showRegisterDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_register, null)
        val optionLogin = dialogView.findViewById<TextView>(R.id.option_login_in_register)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        optionLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
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
