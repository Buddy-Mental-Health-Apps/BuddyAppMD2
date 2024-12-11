package com.example.buddyapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.buddyapp.R
import com.example.buddyapp.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMasuk.setOnClickListener(this)
        binding.btnDaftar.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_masuk) {
            val intentMasuk = Intent(this, LoginActivity::class.java)
            startActivity(intentMasuk)
        }
        if (v?.id == R.id.btn_daftar) {
            val intentDaftar = Intent(this, RegisterActivity::class.java)
            startActivity(intentDaftar)
        }
    }
}