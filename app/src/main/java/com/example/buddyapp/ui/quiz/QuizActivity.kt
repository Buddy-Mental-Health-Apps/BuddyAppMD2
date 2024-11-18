package com.example.buddyapp.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.buddyapp.MainActivity
import com.example.buddyapp.R

class QuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)

        // Set toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar_quiz)
        setSupportActionBar(toolbar)

        // Remove default title
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Handle back button
        val btnBack: ImageButton = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            showExitDialog()
        }
    }

    private fun showExitDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog, null)

        val optionYes = dialogView.findViewById<TextView>(R.id.option_yes)
        val optionBack = dialogView.findViewById<TextView>(R.id.option_back)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        optionYes.setOnClickListener {
            val intent = Intent(this@QuizActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        optionBack.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
