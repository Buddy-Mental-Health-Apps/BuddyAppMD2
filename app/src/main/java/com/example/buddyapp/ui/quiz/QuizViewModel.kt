package com.example.buddyapp.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.buddyapp.data.api.QuestionsItem

class QuizViewModel : ViewModel() {
    private val _questions = MutableLiveData<List<QuestionsItem>>()
    val questions: LiveData<List<QuestionsItem>> get() = _questions

    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> get() = _currentQuestionIndex

    // Mengubah userAnswers untuk menyimpan jawaban dalam bentuk String
    private val userAnswers = mutableMapOf<Int, String>()

    // Mengatur daftar soal baru dan mereset indeks dan jawaban
    fun setQuestions(questions: List<QuestionsItem>) {
        _questions.value = questions
        _currentQuestionIndex.value = 0 // Reset ke soal pertama saat setQuestions dipanggil
        userAnswers.clear() // Reset jawaban pengguna
    }

    // Menyimpan jawaban pengguna untuk soal tertentu
    fun setUserAnswer(questionId: Int, answer: String) {
        userAnswers[questionId] = answer
    }

    // Melompat ke soal berikutnya
    fun nextQuestion() {
        val currentIndex = _currentQuestionIndex.value ?: 0
        val totalQuestions = _questions.value?.size ?: 0
        if (currentIndex < totalQuestions - 1) {
            _currentQuestionIndex.value = currentIndex + 1
        }
    }

    // Kembali ke soal sebelumnya
    fun previousQuestion() {
        val currentIndex = _currentQuestionIndex.value ?: 0
        if (currentIndex > 0) {
            _currentQuestionIndex.value = currentIndex - 1
        }
    }

    // Mendapatkan semua jawaban pengguna
    fun getUserAnswers(): Map<Int, String> = userAnswers
}
