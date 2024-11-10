package com.example.buddyapp.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.buddyapp.R
import com.example.buddyapp.databinding.FragmentOnboardingOneBinding

class OnboardingOne : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentOnboardingOneBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentOnboardingOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener(this)
        binding.btnBefore.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_next) {
            val onboardingTwo = OnboardingTwo()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, onboardingTwo, OnboardingTwo::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

}