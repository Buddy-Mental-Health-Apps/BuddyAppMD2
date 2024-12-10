package com.example.buddyapp.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.example.buddyapp.MainActivity
import com.example.buddyapp.R
import com.example.buddyapp.databinding.ActivityResultBinding
import com.example.buddyapp.ui.medicine.MedicineFragment

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

        binding.btnBackQuiz.setOnClickListener {
            finish()
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.messageResult.visibility = View.VISIBLE

        binding.findMedicineButton.setOnClickListener {
            Log.d("FragmentDebug", "Find Medicine Button Tapped")

            val toolbarResult = findViewById<Toolbar>(R.id.toolbar_result)
            toolbarResult.visibility = View.GONE

            val toolbarMedicine = findViewById<Toolbar>(R.id.toolbar_medicine)
            toolbarMedicine.visibility = View.VISIBLE

            val backButton: ImageButton = toolbarMedicine.findViewById(R.id.btn_back_medicine)
            backButton.setOnClickListener {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            val fragmentContainer = findViewById<FrameLayout>(R.id.fragment_container)
            fragmentContainer.visibility = View.VISIBLE

            val existingFragment =
                supportFragmentManager.findFragmentByTag(MedicineFragment::class.java.simpleName)
            if (existingFragment == null) {
                Log.d("FragmentDebug", "No existing fragment found, proceeding to add new one.")

                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                transaction.replace(
                    R.id.fragment_container,
                    MedicineFragment(),
                    MedicineFragment::class.java.simpleName
                )
                transaction.commitAllowingStateLoss()

                binding.fragmentContainer.requestLayout()
            } else {
                Log.d("FragmentDebug", "Fragment already exists.")
            }
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(MedicineFragment::class.java.simpleName)

        if (fragment != null && fragment.isVisible) {
            findViewById<FrameLayout>(R.id.fragment_container).visibility = View.GONE

            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}