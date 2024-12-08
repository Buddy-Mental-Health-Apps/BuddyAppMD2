package com.example.buddyapp.ui.journal

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.buddyapp.R
import com.example.buddyapp.databinding.ActivityResultJournalBinding

class ResultJournalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultJournalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        val content = intent.getStringExtra(EXTRA_CONTENT) ?: ""
        val positiveWords = intent.getStringExtra(EXTRA_POSITIVE_WORDS)
        val negativeWords = intent.getStringExtra(EXTRA_NEGATIVE_WORDS)
        val positiveCount = intent.getIntExtra(EXTRA_POSITIVE_COUNT, 0)
        val negativeCount = intent.getIntExtra(EXTRA_NEGATIVE_COUNT, 0)
        binding.tvJournalContent.text = content
        if (positiveWords.isNullOrEmpty()) {
            binding.tvListPositiveWords.text = getString(R.string.positive_words, getString(R.string.empty))
        } else {
            binding.tvListPositiveWords.text = getString(R.string.positive_words, positiveWords)
        }

        if (negativeWords.isNullOrEmpty()) {
            binding.tvListNegativeWords.text = getString(R.string.negative_words, getString(R.string.empty))
        } else {
            binding.tvListNegativeWords.text = getString(R.string.negative_words, negativeWords)
        }
        binding.positiveCount.text = positiveCount.toString()
        binding.negativeCount.text = negativeCount.toString()

        val positiveResult = intent.getStringExtra(EXTRA_POSITIVE_RESULT)
        val negativeResult = intent.getStringExtra(EXTRA_NEGATIVE_RESULT)
        binding.tvPositiveResult.text = positiveResult
        binding.tvNegativeResult.text = negativeResult

        binding.btnChange.setOnClickListener {
            val positiveTitle = binding.tvPositiveTitle
            val positivePercentage = binding.tvPositiveResult
            val negativeTitle = binding.tvNegativeTitle
            val negativePercentage = binding.tvNegativeResult

            if (positiveTitle.visibility == View.VISIBLE && positivePercentage.visibility == View.VISIBLE) {
                positiveTitle.visibility = View.INVISIBLE
                positivePercentage.visibility = View.INVISIBLE
                negativeTitle.visibility = View.VISIBLE
                negativePercentage.visibility = View.VISIBLE
            } else {
                positiveTitle.visibility = View.VISIBLE
                positivePercentage.visibility = View.VISIBLE
                negativeTitle.visibility = View.INVISIBLE
                negativePercentage.visibility = View.INVISIBLE
            }
        }

        binding.tvSeeMore.setOnClickListener {
            binding.tvJournalContentTitle.visibility = View.VISIBLE
            binding.tvJournalContent.visibility = View.VISIBLE
            binding.tvListPositiveWords.visibility = View.VISIBLE
            binding.tvListNegativeWords.visibility = View.VISIBLE
            binding.tvSeeMore.visibility = View.GONE
            binding.ivBuddyLove.visibility = View.GONE
            binding.tvCloseSeeMore.visibility = View.VISIBLE
        }

        binding.tvCloseSeeMore.setOnClickListener {
            binding.tvJournalContentTitle.visibility = View.GONE
            binding.tvJournalContent.visibility = View.GONE
            binding.tvListPositiveWords.visibility = View.GONE
            binding.tvListNegativeWords.visibility = View.GONE
            binding.tvSeeMore.visibility = View.VISIBLE
            binding.ivBuddyLove.visibility = View.VISIBLE
            binding.tvCloseSeeMore.visibility = View.GONE
        }
        binding.btnBack.setOnClickListener { finish() }
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

    companion object {
        const val EXTRA_CONTENT = "extra_content"
        const val EXTRA_POSITIVE_RESULT = "extra_positive_result"
        const val EXTRA_NEGATIVE_RESULT = "extra_negative_result"
        const val EXTRA_POSITIVE_COUNT = "extra_positive_count"
        const val EXTRA_NEGATIVE_COUNT = "extra_negative_count"
        const val EXTRA_POSITIVE_WORDS = "extra_positive_words"
        const val EXTRA_NEGATIVE_WORDS = "extra_negative_words"
    }
}