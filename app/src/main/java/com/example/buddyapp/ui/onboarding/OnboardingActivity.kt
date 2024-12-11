package com.example.buddyapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.buddyapp.R
import com.example.buddyapp.ui.authentication.AuthenticationActivity
import com.example.buddyapp.data.ds.UserPreference
import com.example.buddyapp.data.ds.dataStore
import com.example.buddyapp.databinding.ActivityOnboardingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnboardingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        binding.btnMulai.setOnClickListener(this)
    }
    
    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_mulai) {
            CoroutineScope(Dispatchers.Main).launch {
                userPreference.setNotFirstTimeStatus()
            }
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
        }
    }
}
