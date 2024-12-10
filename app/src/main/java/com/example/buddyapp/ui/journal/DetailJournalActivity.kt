package com.example.buddyapp.ui.journal

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.buddyapp.R
import com.example.buddyapp.data.local.Journal
import com.example.buddyapp.data.local.ResultJournal
import com.example.buddyapp.databinding.ActivityDetailJournalBinding
import com.example.buddyapp.helper.journalAnalyzerHelper
import com.example.buddyapp.ui.ViewModelFactory

class DetailJournalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailJournalBinding
    private val journalViewModel: JournalViewModel by viewModels {
        ViewModelFactory.getInstance(Application()) }

    private var journal: Journal? = null
    private lateinit var positivePercentage: String
    private lateinit var negativePercentage: String
    private var positiveCount: Int = 0
    private var negativeCount: Int = 0
    private lateinit var positiveWordsString: String
    private lateinit var negativeWordsString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        journal = intent.getParcelableExtra(EXTRA_JOURNAL)

        if (journal != null) {
            journal?.let { journal ->
                Glide.with(this)
                    .load(journal.image)
                    .placeholder(R.drawable.default_image_buddy)
                    .into(binding.ivJournalCover)
                binding.tvJournalTitle.text = journal.title
                binding.tvJournalContent.text = journal.description
            }
        }

        binding.btnDelete.setOnClickListener { alertDeleteJournal() }
        binding.btnBack.setOnClickListener { finish() }
        binding.fabEdit.setOnClickListener {
            val intent = Intent(this, WriteJournalActivity::class.java)
            intent.putExtra(EXTRA_JOURNAL, journal)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val journalId = journal?.id
        if (journalId != null) {
            journalViewModel.getJournalById(journalId)
            observeDetailJournal()
        }
    }

    private fun observeDetailJournal() {
        journalViewModel.detailJournal.observe(this) { journal ->
            if (journal != null) {
                setupAnalyzeButton(journal)
            }
        }
    }

    private fun setupAnalyzeButton(journal: Journal) {
        if (journal.isAnalyzed) {
            binding.btnAnalyze.text = getString(R.string.see_result)
            binding.btnAnalyze.setOnClickListener {
                showLoading(true)
                showAnalyzeResult()
            }
        } else {
            binding.btnAnalyze.text = getString(R.string.analisis_jurnal)
            binding.btnAnalyze.setOnClickListener {
                showLoading(true)
                val content = binding.tvJournalContent.text.toString()
                analyzeJournal(this, content)
            }
        }
    }

    private fun showAnalyzeResult() {
        showLoading(true)

        journalViewModel.resultJournal.observe(this) { resultJournal ->
            showLoading(false)

            resultJournal?.let {
                positivePercentage = it.positivePercentage
                negativePercentage = it.negativePercentage
                positiveCount = it.positiveCount
                negativeCount = it.negativeCount
                positiveWordsString = it.positiveWords
                negativeWordsString = it.negativeWords

                val intent = Intent(
                    this@DetailJournalActivity,
                    ResultJournalActivity::class.java
                ).apply {
                    putExtra(ResultJournalActivity.EXTRA_CONTENT, binding.tvJournalContent.text.toString())
                    putExtra(ResultJournalActivity.EXTRA_POSITIVE_RESULT, positivePercentage)
                    putExtra(ResultJournalActivity.EXTRA_NEGATIVE_RESULT, negativePercentage)
                    putExtra(ResultJournalActivity.EXTRA_POSITIVE_COUNT, positiveCount)
                    putExtra(ResultJournalActivity.EXTRA_NEGATIVE_COUNT, negativeCount)
                    putExtra(ResultJournalActivity.EXTRA_POSITIVE_WORDS, positiveWordsString)
                    putExtra(ResultJournalActivity.EXTRA_NEGATIVE_WORDS, negativeWordsString)
                }
                startActivity(intent)
            }
        }

        journal?.id?.let {
            journalViewModel.getResultJournal(it)
        }
    }

    private fun analyzeJournal(context: Context, inputText: String) {
        val sentimentScores = journalAnalyzerHelper(context, inputText)

        val negativityScore = sentimentScores[0]
        val positivityScore = sentimentScores[1]
        positivePercentage = "%.2f".format(positivityScore * 100) + "%"
        negativePercentage = "%.2f".format(negativityScore * 100) + "%"

        val positiveWords = listOf(
            "bahagia", "senang", "baik", "keren", "hebat", "positif", "bagus", "terbaik", "bersyukur", "syukur", "terbaik", "menyenangkan",
            "sukses", "semangat", "ceria", "cinta", "kuat", "tenang", "pintar", "damai", "sabar", "tulus", "berani", "bersyukur"
        )
        val negativeWords = listOf(
            "sedih", "kecewa", "lelah", "capek", "sulit", "jelek", "buruk", "gagal", "negatif", "kesal", "nangis", "menyerah",
            "frustrasi", "marah", "tertekan", "cemas", "stress", "gagal", "hancur", "malas", "terpuruk", "kacau", "benci"
        )

        val words = inputText.split(" ", ".", ",", "!", "?", ";", ":")

        val positiveWordsList = mutableListOf<String>()
        val negativeWordsList = mutableListOf<String>()

        for (word in words) {
            when {
                positiveWords.contains(word.lowercase()) -> {
                    positiveCount++
                    positiveWordsList.add(word)
                }

                negativeWords.contains(word.lowercase()) -> {
                    negativeCount++
                    negativeWordsList.add(word)
                }
            }
        }

        positiveWordsString = positiveWordsList.joinToString(", ")
        negativeWordsString = negativeWordsList.joinToString(", ")
        showLoading(false)
        saveAndMoveToResult()
    }

    private fun saveAndMoveToResult() {
        journal?.id?.let {
            journalViewModel.analyzeStatusUpdate(it,true)
        }

        val resultJournal = ResultJournal(
            id = journal?.id,
            positivePercentage = positivePercentage,
            negativePercentage = negativePercentage,
            positiveCount = positiveCount,
            negativeCount = negativeCount,
            positiveWords = positiveWordsString,
            negativeWords = negativeWordsString,
        )
        journalViewModel.saveResultJournal(resultJournal)


        val intent = Intent(this, ResultJournalActivity::class.java).apply {
            putExtra(ResultJournalActivity.EXTRA_CONTENT, binding.tvJournalContent.text.toString())
            putExtra(ResultJournalActivity.EXTRA_POSITIVE_RESULT, positivePercentage)
            putExtra(ResultJournalActivity.EXTRA_NEGATIVE_RESULT, negativePercentage)
            putExtra(ResultJournalActivity.EXTRA_POSITIVE_COUNT, positiveCount)
            putExtra(ResultJournalActivity.EXTRA_NEGATIVE_COUNT, negativeCount)
            putExtra(ResultJournalActivity.EXTRA_POSITIVE_WORDS, positiveWordsString)
            putExtra(ResultJournalActivity.EXTRA_NEGATIVE_WORDS, negativeWordsString)
        }
        startActivity(intent)
    }

    private fun alertDeleteJournal() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.alert_delete_journal, null)
        val btnDelete = dialogView.findViewById<TextView>(R.id.btn_delete)
        val btnCancel = dialogView.findViewById<TextView>(R.id.btn_cancel)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnDelete.setOnClickListener {
            journal?.let { journalViewModel.deleteJournal(it) }
            finish()
        }

        btnCancel.setOnClickListener { alertDialog.dismiss() }

        alertDialog.show()
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressIndicator.visibility = View.VISIBLE
            binding.progressIndicator.show()
        } else {
            binding.progressIndicator.visibility = View.INVISIBLE
            binding.progressIndicator.hide()
        }
    }

    companion object {
        const val EXTRA_JOURNAL = "extra_journal"
    }
}