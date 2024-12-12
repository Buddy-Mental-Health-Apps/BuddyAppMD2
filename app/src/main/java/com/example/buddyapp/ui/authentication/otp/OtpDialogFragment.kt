package com.example.buddyapp.ui.authentication.otp

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.buddyapp.databinding.FragmentOtpDialogBinding

class OtpDialogFragment(private val correctOtp: String, private val onOtpVerified: () -> Unit) :
    DialogFragment() {

    private var _binding: FragmentOtpDialogBinding? = null
    private val binding get() = _binding!!
    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCountdown()

        binding.btnVerify.setOnClickListener {
            val enteredOtp = binding.edtOtp.text.toString()
            if (enteredOtp == correctOtp) {
                onOtpVerified()  // OTP verified successfully
                dismiss()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Kode OTP salah! Silakan coba lagi.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startCountdown() {
        timer = object : CountDownTimer(5 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                binding.txtTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.txtTimer.text = "Expired"
                binding.btnVerify.isEnabled = false
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        timer?.cancel()
    }
}
