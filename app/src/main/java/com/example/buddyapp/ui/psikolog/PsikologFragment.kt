package com.example.buddyapp.ui.psikolog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.buddyapp.R
import com.example.buddyapp.databinding.FragmentPsikologBinding

class PsikologFragment : Fragment(R.layout.fragment_psikolog) {

    private var _binding: FragmentPsikologBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val psikologViewModel =
            ViewModelProvider(this).get(PsikologViewModel::class.java)

        _binding = FragmentPsikologBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}