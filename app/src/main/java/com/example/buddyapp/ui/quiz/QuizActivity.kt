package com.example.buddyapp.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.example.buddyapp.MainActivity
import com.example.buddyapp.R
import com.example.buddyapp.data.api.ApiConfig
import com.example.buddyapp.helper.QuizHelper
import kotlinx.coroutines.launch

class QuizActivity : AppCompatActivity(), QuizFragment.QuizSubmissionListener {
    private val viewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val token = intent.getStringExtra("token") ?: ""
        Log.d("QuizActivity", "Token: $token")

        val toolbar: Toolbar = findViewById(R.id.toolbar_quiz)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val btnBackQuiz: ImageButton = findViewById(R.id.btn_back_quiz)
        btnBackQuiz.setOnClickListener {
            showExitDialog()
        }

        val apiService = ApiConfig.getApiServices(token)
        lifecycleScope.launch {
            try {
                val response = apiService.question(emptyList()) // Gantilah dengan cara yang benar untuk request data soal
                val questions = response.questions?.filterNotNull() ?: emptyList()
                Log.d("QuizActivity", "Questions: $questions")
                viewModel.setQuestions(questions)  // Set soal ke ViewModel
            } catch (e: Exception) {
                Log.e("QuizActivity", "Error fetching questions", e)
                // Handle error, you can show a Toast or Dialog to inform the user
            }

        }

        // Observer untuk daftar soal
        viewModel.questions.observe(this) { questions ->
            if (questions.isNotEmpty()) {
                Log.d("QuizActivity", "Questions received: ${questions.size}")
                loadFragment(viewModel.currentQuestionIndex.value ?: 0)
            } else {
                Log.d("QuizActivity", "No questions found!")
            }
        }

        // Observer untuk perubahan indeks soal
        viewModel.currentQuestionIndex.observe(this) { index ->
            loadFragment(index)
        }
    }

    private fun loadFragment(index: Int) {
        val pBar = findViewById<ProgressBar>(R.id.pBar)

        pBar.visibility = View.VISIBLE
        // Pastikan kita memeriksa terlebih dahulu apakah questions ada
        val questions = viewModel.questions.value
        if (questions != null && questions.isNotEmpty()) {
            val fragment = QuizFragment.newInstance(index)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            pBar.visibility = View.GONE
        } else {
            pBar.visibility = View.VISIBLE
            Log.d("QuizActivity", "No questions available to display!")
        }
    }

    private fun showExitDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog, null)
        val optionYes = dialogView.findViewById<TextView>(R.id.option_yes)
        val optionBack = dialogView.findViewById<TextView>(R.id.option_back)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        optionYes.setOnClickListener {
            startActivity(Intent(this@QuizActivity, MainActivity::class.java))
            finish()
        }

        optionBack.setOnClickListener { alertDialog.dismiss() }

        alertDialog.show()
    }

    override fun onSubmitQuiz() {
        // Tangkap semua jawaban user
        val userAnswers: FloatArray = viewModel.getAllAnswers() // Pastikan ini mengembalikan FloatArray dengan 23 elemen

        // Tampilkan dialog konfirmasi sebelum melanjutkan
        showFinishDialog(userAnswers)
    }

    private fun showFinishDialog(userAnswers: FloatArray) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.ad_test_finish, null)

        val optionYes = dialogView.findViewById<View>(R.id.option_yes)
        val optionBack = dialogView.findViewById<View>(R.id.option_back)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        optionYes.setOnClickListener {
            alertDialog.dismiss()

            // Inisialisasi QuizHelper dan proses prediksi
            val quizHelper = QuizHelper(this)
            val predictions = quizHelper.predictResults(userAnswers)

            // Kirim hasil prediksi ke ResultActivity
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("predictions", predictions)
            startActivity(intent)
            finish() // Tutup activity saat ini
        }

        optionBack.setOnClickListener {
            alertDialog.dismiss() // Tutup dialog tanpa aksi tambahan
        }

        alertDialog.show()
    }
}

