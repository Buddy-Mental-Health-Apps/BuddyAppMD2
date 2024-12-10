package com.example.buddyapp.ui.journal

import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buddyapp.databinding.ActivityHistoryJournalBinding
import com.example.buddyapp.ui.ViewModelFactory

class HistoryJournalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryJournalBinding
    private val journalViewModel: JournalViewModel by viewModels {
        ViewModelFactory.getInstance(Application()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        binding.rvJournalHistory.layoutManager = LinearLayoutManager(this)

        journalViewModel.getAllJournalsHistory().observe(this) { history ->
            val adapter = HistoryJournalAdapter(history)
            binding.rvJournalHistory.adapter = adapter
            if (history.isEmpty()) {
                binding.ivBuddyEmptyJournal.visibility = View.VISIBLE
                binding.btnWriteJournal.visibility = View.VISIBLE
            } else {
                binding.ivBuddyEmptyJournal.visibility = View.GONE
                binding.btnWriteJournal.visibility = View.GONE
            }
        }

        journalViewModel.getStreakData()
        journalViewModel.currentStreak.observe(this) { currentStreak ->
            binding.tvCurrentStreak.text = currentStreak.toString()
        }

        journalViewModel.highestStreak.observe(this) { highestStreak ->
            binding.tvHighestStreak.text = highestStreak.toString()
        }

        binding.btnBack.setOnClickListener{ finish() }
        binding.btnWriteJournal.setOnClickListener {
            val intent = Intent(this, WriteJournalActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}