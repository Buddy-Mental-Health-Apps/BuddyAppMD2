package com.example.buddyapp.ui.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.buddyapp.R
import com.example.buddyapp.data.api.Answers
import com.example.buddyapp.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {
    private val viewModel: QuizViewModel by activityViewModels()
    private var listener: QuizSubmissionListener? = null

    companion object {
        private const val ARG_INDEX = "index"

        fun newInstance(index: Int): QuizFragment {
            val fragment = QuizFragment()
            val args = Bundle().apply {
                putInt(ARG_INDEX, index)
            }
            fragment.arguments = args
            return fragment
        }
    }

    interface QuizSubmissionListener {
        fun onSubmitQuiz()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? QuizSubmissionListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentQuizBinding.inflate(inflater, container, false)

        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            val currentIndex = viewModel.currentQuestionIndex.value ?: 0
            if (questions.isNotEmpty()) {
                updateUIForQuestion(currentIndex, binding) // [Tambahan]
            }
        }

        // [Tambahan] Observasi currentQuestionIndex untuk memperbarui UI
        viewModel.currentQuestionIndex.observe(viewLifecycleOwner) { index ->
            updateUIForQuestion(index, binding)
            setupNavigationButtons(binding)
        }

        binding.submitButton.setOnClickListener {
            listener?.onSubmitQuiz()
        }

        binding.nextButton.setOnClickListener {
            val currentIndex = viewModel.currentQuestionIndex.value ?: 0
            val currentQuestionId = viewModel.questions.value?.get(currentIndex)?.id

            if (currentQuestionId != null && !viewModel.isAnswerSelected(currentQuestionId)) {
                showAnswerNotSelectedDialog() // Tampilkan dialog jika belum memilih jawaban
            } else {
                viewModel.nextQuestion() // Lanjut ke pertanyaan berikutnya jika sudah memilih jawaban
            }
        }


        binding.backButton.setOnClickListener {
            viewModel.previousQuestion()
        }

        // [Tambahan] Setup navigasi awal
        setupNavigationButtons(binding)

        return binding.root
    }

    private fun setSelectedAnswer(binding: FragmentQuizBinding, currentIndex: Int) {
        val selectedAnswer = viewModel.userAnswers[viewModel.questions.value?.get(currentIndex)?.id]
        binding.optionA.isChecked = selectedAnswer == binding.optionA.text.toString() // [Tambahan]
        binding.optionB.isChecked = selectedAnswer == binding.optionB.text.toString() // [Tambahan]
        binding.optionC.isChecked = selectedAnswer == binding.optionC.text.toString() // [Tambahan]
        binding.optionD.isChecked = selectedAnswer == binding.optionD.text.toString() // [Tambahan]
        binding.optionE.isChecked = selectedAnswer == binding.optionE.text.toString() // [Tambahan]
    }

    private fun setupNavigationButtons(binding: FragmentQuizBinding) {
        val totalQuestions = viewModel.questions.value?.size ?: 0
        val currentIndex = viewModel.currentQuestionIndex.value ?: 0

        when (currentIndex) {
            0 -> {
                binding.backButton.visibility = View.GONE
                binding.submitButton.visibility = View.GONE
                binding.nextButton.visibility = View.VISIBLE
            }
            totalQuestions - 1 -> {
                binding.backButton.visibility = View.VISIBLE
                binding.submitButton.visibility = View.VISIBLE
                binding.nextButton.visibility = View.GONE
            }
            else -> {
                binding.backButton.visibility = View.VISIBLE
                binding.submitButton.visibility = View.GONE
                binding.nextButton.visibility = View.VISIBLE
            }
        }
    }

    // [Tambahan] Fungsi untuk memperbarui UI berdasarkan soal saat ini
    private fun updateUIForQuestion(index: Int, binding: FragmentQuizBinding) {
        val question = viewModel.questions.value?.getOrNull(index)
        if (question != null) {
            binding.questionText.text = question.question
            binding.questionNumber.text = question.id.toString()
            val answers = question.answers

            binding.optionA.text = getAnswerText(answers?.jsonMember0)
            binding.optionB.text = getAnswerText(answers?.jsonMember1)
            binding.optionC.text = getAnswerText(answers?.jsonMember2)
            binding.optionD.text = getAnswerText(answers?.jsonMember3)
            binding.optionE.text = getAnswerText(answers?.jsonMember4)

            setSelectedAnswer(binding, index) // [Tambahan]

            // Set listener untuk pilihan jawaban
            binding.optionA.setOnClickListener {
                viewModel.setUserAnswer(question.id!!, binding.optionA.text.toString())
            }
            binding.optionB.setOnClickListener {
                viewModel.setUserAnswer(question.id!!, binding.optionB.text.toString())
            }
            binding.optionC.setOnClickListener {
                viewModel.setUserAnswer(question.id!!, binding.optionC.text.toString())
            }
            binding.optionD.setOnClickListener {
                viewModel.setUserAnswer(question.id!!, binding.optionD.text.toString())
            }
            binding.optionE.setOnClickListener {
                viewModel.setUserAnswer(question.id!!, binding.optionE.text.toString())
            }
        }
    }

    private fun getAnswerText(answer: String?): String {
        return answer ?: "Tidak ada jawaban"
    }

    private fun showAnswerNotSelectedDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.ad_answer_not_found, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialogView.findViewById<TextView>(R.id.option_ok_answer_not_found).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}
