package com.example.buddyapp.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.buddyapp.R
import com.example.buddyapp.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {
    private var index: Int = 0
    private val viewModel: QuizViewModel by activityViewModels()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            index = it.getInt(ARG_INDEX, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentQuizBinding.inflate(inflater, container, false)

        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            if (questions.isNotEmpty()) {
                val question = questions.getOrNull(index)
                if (question != null) {
                    binding.questionText.text = question.question
                    binding.questionNumber.text = question.id.toString()
                    val answers = question.answers

                    binding.optionA.text = getAnswerText(answers?.jsonMember0)
                    binding.optionB.text = getAnswerText(answers?.jsonMember1)
                    binding.optionC.text = getAnswerText(answers?.jsonMember2)
                    binding.optionD.text = getAnswerText(answers?.jsonMember3)
                    binding.optionE.text = getAnswerText(answers?.jsonMember4)

                    binding.optionA.setOnClickListener {
                        viewModel.setUserAnswer(question.id!!, answers?.jsonMember0 ?: "")
                    }
                    binding.optionB.setOnClickListener {
                        viewModel.setUserAnswer(question.id!!, answers?.jsonMember1 ?: "")
                    }
                    binding.optionC.setOnClickListener {
                        viewModel.setUserAnswer(question.id!!, answers?.jsonMember2 ?: "")
                    }
                    binding.optionD.setOnClickListener {
                        viewModel.setUserAnswer(question.id!!, answers?.jsonMember3 ?: "")
                    }
                    binding.optionE.setOnClickListener {
                        viewModel.setUserAnswer(question.id!!, answers?.jsonMember4 ?: "")
                    }
                }
            }
        }

        binding.submitButton.setOnClickListener {
            showFinishDialog()
        }

        binding.nextButton.setOnClickListener {
            if (index < (viewModel.questions.value?.size ?: 0) - 1) {
                viewModel.nextQuestion()
            }
        }

        binding.backButton.setOnClickListener {
            if (index > 0) {
                viewModel.previousQuestion()
            }
        }

        when (index) {
            0 -> {
                // Soal pertama
                binding.backButton.visibility = View.GONE
                binding.submitButton.visibility = View.GONE
                binding.nextButton.visibility = View.VISIBLE
            }
            (viewModel.questions.value?.size ?: 0) - 1 -> {
                // Soal terakhir
                binding.backButton.visibility = View.VISIBLE
                binding.submitButton.visibility = View.VISIBLE
                binding.nextButton.visibility = View.GONE
            }
            else -> {
                // Soal tengah
                binding.backButton.visibility = View.VISIBLE
                binding.submitButton.visibility = View.GONE
                binding.nextButton.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    // Fungsi untuk mendapatkan teks jawaban yang sesuai
    private fun getAnswerText(answer: String?): String {
        return answer ?: "Tidak ada jawaban"
    }

    // Fungsi untuk menampilkan AlertDialog
    private fun showFinishDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.ad_test_finish, null)

        val optionYes = dialogView.findViewById<View>(R.id.option_yes)
        val optionBack = dialogView.findViewById<View>(R.id.option_back)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        optionYes.setOnClickListener {
            alertDialog.dismiss()
//            startActivity(Intent(requireContext(), ResultActivity::class.java))
        }

        optionBack.setOnClickListener {
            // Aksi ketika user memilih "Kembali"
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
