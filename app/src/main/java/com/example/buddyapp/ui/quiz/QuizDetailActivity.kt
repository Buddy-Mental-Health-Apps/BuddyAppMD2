package com.example.buddyapp.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.buddyapp.MainActivity
import com.example.buddyapp.databinding.ActivityResultBinding

class QuizDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val date = intent.getStringExtra("date")

        if (title != null && description != null && date != null) {
            binding.resultTitle.text = title
            binding.resultDescription.text = description
            binding.date.text = date
        } else {
            binding.resultTitle.text = "Data Tidak Tersedia"
            binding.resultDescription.text = "Deskripsi tidak ditemukan"
            binding.date.text = "Tanggal tidak tersedia"
        }

    // Atur toolbar
        binding.btnBackQuiz.setOnClickListener {
            finish() // Kembali ke halaman sebelumnya
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Sembunyikan elemen yang tidak digunakan (opsional)
        binding.messageResult.visibility = View.VISIBLE
        binding.findMedicineButton.visibility = View.VISIBLE
    }
}
