package com.example.buddyapp.ui.journal

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.buddyapp.R
import com.example.buddyapp.databinding.FragmentDetailJournalBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailJournalFragment : Fragment() {

    private var _binding: FragmentDetailJournalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailJournalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        val bottomNav = (activity as? AppCompatActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav?.visibility = View.GONE

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_detailJournalFragment_to_navigation_story)
        }

        binding.fabEdit.setOnClickListener {
            findNavController().navigate(R.id.action_detailJournalFragment_to_writeJournalFragment)
        }

        val args = DetailJournalFragmentArgs.fromBundle(requireArguments())
        binding.tvJournalTitle.text = args.title
        binding.tvJournalContent.text = args.description
        args.imageUri.let { binding.ivJournalCover.setImageURI(Uri.parse(it)) }
    }
}