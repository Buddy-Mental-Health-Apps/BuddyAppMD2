package com.example.buddyapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.buddyapp.MainActivity
import com.example.buddyapp.R
import com.example.buddyapp.authentication.AuthenticationActivity
import com.example.buddyapp.data.ds.UserPreference
import com.example.buddyapp.data.ds.dataStore
import com.example.buddyapp.databinding.ActivityOnboardingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        CoroutineScope(Dispatchers.Main).launch {
            userPreference.isFirstTime().collect { isFirstTime ->
                if (!isFirstTime) {
                    userPreference.getSession().collect { userModel ->
                        if (userModel.isLogin) {
                            val intent = Intent(this@OnboardingActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(this@OnboardingActivity, AuthenticationActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    userPreference.setFirstTimeStatus()
                    val fragmentManager = supportFragmentManager
                    val onboardingZero = OnboardingZero()
                    fragmentManager.beginTransaction()
                        .add(R.id.frame_container, onboardingZero, OnboardingZero::class.java.simpleName)
                        .commit()
                }
            }
        }
    }
}
