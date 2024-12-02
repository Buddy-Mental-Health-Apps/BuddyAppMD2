package com.example.buddyapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.buddyapp.MainActivity
import com.example.buddyapp.R
import com.example.buddyapp.authentication.AuthenticationActivity
import com.example.buddyapp.data.ds.UserPreference
import com.example.buddyapp.data.ds.dataStore
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

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        CoroutineScope(Dispatchers.Main).launch {
            userPreference.isFirstTime().collect { isFirstTime ->
                if (!isFirstTime) {
                    userPreference.getSession().collect { userModel ->
                        if (userModel.isLogin) {
                            val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(this@WelcomeActivity, AuthenticationActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
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