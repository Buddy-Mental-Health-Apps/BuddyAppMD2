package com.example.buddyapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.buddyapp.R
import com.example.buddyapp.authentication.AuthenticationActivity
import com.example.buddyapp.databinding.FragmentOnboardingZeroBinding

class OnboardingZero : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentOnboardingZeroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingZeroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnMulai.setOnClickListener(this)
        binding.btnSkip.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_mulai) {
            val onboardingOne = OnboardingOne()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, onboardingOne, OnboardingOne::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        if (v?.id == R.id.btn_skip) {
            val intent = Intent(requireActivity(), AuthenticationActivity::class.java)
            startActivity(intent)
        }
    }


}