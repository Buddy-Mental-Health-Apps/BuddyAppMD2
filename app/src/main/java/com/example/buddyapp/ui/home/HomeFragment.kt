package com.example.buddyapp.ui.home

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buddyapp.R
import com.example.buddyapp.data.local.JournalRoomDatabase
import com.example.buddyapp.data.local.Quiz
import com.example.buddyapp.ui.quiz.HistoryItem
import com.example.buddyapp.ui.quiz.HistoryQuizAdapter
import com.example.buddyapp.ui.quiz.QuizActivity
import com.example.buddyapp.ui.quiz.QuizDetailActivity

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var adapter: HistoryQuizAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val sharedPref = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val name = sharedPref.getString("name", "Pengguna")

        val greetingTextView = root.findViewById<TextView>(R.id.greetingText)
        greetingTextView.text = "Hi, $name!"

        val testButton = root.findViewById<Button>(R.id.testButton)
        testButton.setOnClickListener {
            val intent = Intent(requireContext(), QuizActivity::class.java)
            startActivity(intent)
        }


        setupIcons(root)
        setupHistoryRecyclerView(root)

        return root
    }

    private fun setupIcons(root: View) {
        root.findViewById<ImageView>(R.id.happyIcon).setOnClickListener {
            Toast.makeText(requireContext(), "Senang mendengarnya", Toast.LENGTH_SHORT).show()
        }

        root.findViewById<ImageView>(R.id.normalIcon).setOnClickListener {
            Toast.makeText(requireContext(), "Coba lakukan aktivitas baru yuk!", Toast.LENGTH_SHORT).show()
        }

        root.findViewById<ImageView>(R.id.anxiousIcon).setOnClickListener {
            Toast.makeText(requireContext(), "Ada apa? yuk tulis jurnal hari ini!", Toast.LENGTH_SHORT).show()
        }

        root.findViewById<ImageView>(R.id.sadIcon).setOnClickListener {
            Toast.makeText(requireContext(), "Percayalah semua akan baik-baik saja", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupHistoryRecyclerView(root: View) {
        val recyclerView = root.findViewById<RecyclerView>(R.id.historyRecyclerView)
        val noDataImageView = root.findViewById<ImageView>(R.id.historyNotFoundImage)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)

        val quizDao = JournalRoomDatabase.getDatabase(requireContext()).quizDao()

        // Inisialisasi adapter kosong
        adapter = HistoryQuizAdapter(mutableListOf(), quizDao).apply {
            onItemClick = { historyItem ->
                val intent = Intent(requireContext(), QuizDetailActivity::class.java).apply {
                    putExtra("title", historyItem.title)
                    putExtra("description", historyItem.description) // Sesuaikan jika ada deskripsi
                    putExtra("date", historyItem.date)
                }
                startActivity(intent)
            }
        }
        recyclerView.adapter = adapter

        // Mengamati perubahan data Quiz menggunakan LiveData
        quizDao.getAllQuiz().observe(viewLifecycleOwner, Observer { quizList ->
            if (quizList.isNullOrEmpty()) {
                recyclerView.visibility = View.GONE
                noDataImageView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                noDataImageView.visibility = View.GONE

                // Mengubah data Quiz ke HistoryItem
                val historyItems = quizList.map { quiz ->
                    HistoryItem(
                        date = quiz.date ?: "No date",
                        title = quiz.title ?: "Untitled",
                        description = quiz.description?: "No Description"
                    )
                }
                adapter.updateData(historyItems)
            }
        })
    }
}