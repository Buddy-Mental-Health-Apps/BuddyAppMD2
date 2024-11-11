package com.example.buddyapp.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.buddyapp.R
import com.example.buddyapp.databinding.FragmentHomeBinding
import com.example.buddyapp.ui.quiz.QuizActivity

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val testButton = root.findViewById<Button>(R.id.testButton)
        testButton.setOnClickListener {
            val intent = Intent(requireContext(), QuizActivity::class.java)
            startActivity(intent)
        }

        val happyIcon = root.findViewById<ImageView>(R.id.happyIcon)
        happyIcon.setOnClickListener {
            val toast = Toast.makeText(requireContext(), "Senang mendengarnya", Toast.LENGTH_SHORT)
            toast.show()
        }

        val normalIcon = root.findViewById<ImageView>(R.id.normalIcon)
        normalIcon.setOnClickListener {
            val toast = Toast.makeText(requireContext(), "Coba lakukan aktivitas baru yuk!", Toast.LENGTH_SHORT)
            toast.show()
        }

        val anxiousIcon = root.findViewById<ImageView>(R.id.anxiousIcon)
        anxiousIcon.setOnClickListener {
            val toast = Toast.makeText(requireContext(), "Ada apa? yuk tulis jurnal hari ini!", Toast.LENGTH_SHORT)
            toast.show()
        }

        val sadIcon = root.findViewById<ImageView>(R.id.sadIcon)
        sadIcon.setOnClickListener {
            val toast = Toast.makeText(requireContext(), "Percayalah semua akan baik-baik saja", Toast.LENGTH_SHORT)
            toast.show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
