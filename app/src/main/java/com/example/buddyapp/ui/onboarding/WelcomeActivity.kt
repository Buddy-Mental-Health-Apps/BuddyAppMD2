package com.example.buddyapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.buddyapp.R
import com.example.buddyapp.ui.authentication.AuthenticationActivity
import com.example.buddyapp.data.ds.UserPreference
import com.example.buddyapp.databinding.ActivityWelcomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMulai.setOnClickListener(this)
        binding.btnSkip.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        CoroutineScope(Dispatchers.Main).launch {
            userPreference.setNotFirstTimeStatus()
        }
        if (v?.id == R.id.btn_mulai) {
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
        }
        if (v?.id == R.id.btn_skip) {
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
        }
    }
}