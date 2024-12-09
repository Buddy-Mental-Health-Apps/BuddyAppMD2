package com.example.buddyapp.ui.journal

import android.app.Application
import android.os.Build
import android.os.Bundle
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
        }

        val currentStreak = journalViewModel.getCurrentStreak()
        val highestStreak = journalViewModel.getHighestStreak()
        binding.tvCurrentStreak.text = currentStreak.toString()
        binding.tvHighestStreak.text = highestStreak.toString()

        binding.btnBack.setOnClickListener{ finish() }
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