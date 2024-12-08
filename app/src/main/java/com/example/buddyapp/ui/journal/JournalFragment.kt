package com.example.buddyapp.ui.journal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buddyapp.R
import com.example.buddyapp.databinding.FragmentJournalBinding
import com.example.dicodingevents.ui.ViewModelFactory

class JournalFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentJournalBinding? = null
    private val binding get() = _binding!!

    private lateinit var journalAdapter: JournalAdapter
    private lateinit var journalViewModel: JournalViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentJournalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvJournal.layoutManager = layoutManager

        journalAdapter = JournalAdapter()
        binding.rvJournal.adapter = journalAdapter

        val factory = ViewModelFactory.getInstance(requireActivity().application)
        journalViewModel = ViewModelProvider(this, factory)[JournalViewModel::class.java]

        journalViewModel.getAllJournal().observe(viewLifecycleOwner) { journalList ->
            journalAdapter.setListJournal(journalList)
            if (journalList.isEmpty()) {
                binding.ivBuddyEmptyJournal.visibility = View.VISIBLE
            } else {
                binding.ivBuddyEmptyJournal.visibility = View.GONE
            }
        }

        binding.fabNewJournal.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fab_new_journal) {
            val intent = Intent(requireActivity(), WriteJournalActivity::class.java)
            startActivity(intent)
        }
    }
}