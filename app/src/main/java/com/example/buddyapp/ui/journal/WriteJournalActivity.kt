package com.example.buddyapp.ui.journal

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.buddyapp.R
import com.example.buddyapp.data.local.Journal
import com.example.buddyapp.databinding.ActivityWriteJournalBinding
import com.example.buddyapp.helper.DateHelper
import com.example.buddyapp.ui.ViewModelFactory
import com.yalantis.ucrop.UCrop
import java.io.File

class WriteJournalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteJournalBinding
    private val journalViewModel: JournalViewModel by viewModels {
        ViewModelFactory.getInstance(Application()) }

    private var currentImageUri: Uri? = null
    private var journal: Journal? = null
    private var isUpdate = false
    private var isAnalyzed: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        val titleEditText = binding.etJournalTitle
        val backButton = binding.btnBack

        journal = intent.getParcelableExtra(EXTRA_JOURNAL)
        if (journal != null) {
            isUpdate = true
            binding.etJournalTitle.setText(journal?.title)
            binding.etJournalContent.setText(journal?.description)
            currentImageUri = Uri.parse(journal?.image)
            isAnalyzed = journal?.isAnalyzed
            showImage()
        } else {
            journal = Journal()
        }

        if (titleEditText.text.toString().isEmpty()) {
            titleEditText.setTextColor(ContextCompat.getColor(this, R.color.blue_70))
            addTextChangedListener(titleEditText)
        } else {
            titleEditText.setTextColor(ContextCompat.getColor(this, R.color.blue))
        }

        backButton.setOnClickListener {
            if (binding.etJournalTitle.text.toString().isEmpty() &&
                binding.etJournalContent.text.toString().isEmpty() &&
                currentImageUri == null) {
                this.onBackPressedDispatcher.onBackPressed()
            } else {
                showBackAlertDialog()
            }
        }

        journalViewModel.imageUri.observe(this) { uri ->
            if (uri != null) {
                currentImageUri = uri
                binding.ivJournalCover.setImageURI(uri)
            }
        }

        binding.btnOpenGallery.setOnClickListener { startGallery() }
        binding.btnSave.setOnClickListener { saveJournal() }

        journalViewModel.saveResult.observe(this) { isSuccess ->
            if (isSuccess) {
                navigateToDetailJournalPage()
            } else {
                Toast.makeText(this, "Failed to save journal.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showBackAlertDialog()
    }

    private fun saveJournal() {
        val title = binding.etJournalTitle.text.toString()
        val image = currentImageUri.toString()
        val description = binding.etJournalContent.text.toString()
        val initialTimestamp = if (isUpdate) {
            journal?.initialTimestamp
        } else {
            System.currentTimeMillis()
        }
        val timestamp = if (isUpdate) {
            "Diperbarui pada ${DateHelper.getCurrentDate()}"
        } else {
            DateHelper.getCurrentDate()
        }
        when {
            title.isEmpty() -> {
                binding.etJournalTitle.error = getString(R.string.empty_journal_title)
            }
            description.isEmpty() -> {
                binding.etJournalContent.error = getString(R.string.empty_journal_description)
            }
            image.isEmpty() -> {
                showToast(getString(R.string.empty_journal_image))
            }
            else -> {
                showLoading(true)
                if (isUpdate) {
                    val journal = journal?.let {
                        Journal(
                            id = it.id,
                            title = title,
                            image = image,
                            description = description,
                            initialTimestamp = initialTimestamp,
                            timestamp = timestamp
                        )
                    }
                    journalViewModel.updateJournal(journal)
                    if (journal?.isAnalyzed == true) {
                        journalViewModel.deleteResultJournal(journal.id)
                    }
                    showToast(getString(R.string.journal_updated))
                } else {
                    val journal = journal?.let {
                        Journal(
                            title = title,
                            image = image,
                            description = description,
                            initialTimestamp = initialTimestamp,
                            timestamp = timestamp
                        )
                    }
                    journalViewModel.insertJournal(journal)
                    journalViewModel.addJournalHistory(journal?.timestamp, journal?.title)
                    showToast(getString(R.string.journal_saved))
                }
            }
        }
    }

    private fun navigateToDetailJournalPage() {
        journalViewModel.getNewestJournalId().observe(this) { journalId ->
            val title = binding.etJournalTitle.text.toString()
            val description = binding.etJournalContent.text.toString()
            val imageUri = currentImageUri.toString()
            val initialTimestamp = System.currentTimeMillis()
            val isAnalyzed = false
            val journal = Journal(id = journalId, title = title, description = description, image = imageUri, initialTimestamp = initialTimestamp, isAnalyzed = isAnalyzed)

            showLoading(false)
            val intent = Intent(this, DetailJournalActivity::class.java)
            intent.putExtra(DetailJournalActivity.EXTRA_JOURNAL, journal)
            startActivity(intent)
            finish()

            journalViewModel.writeJournal(initialTimestamp.toString())
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val destinationUri =
                Uri.fromFile(
                    File(
                        this.cacheDir,
                        "cropped_image_${System.currentTimeMillis()}.jpg"
                    )
                )
            uCropLauncher.launch(
                UCrop.of(uri, destinationUri)
                    .getIntent(this)
            )
        } else {
            Log.d("Photo Picker", "No Media Selected")
        }
    }

    private val uCropLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val resultUri = UCrop.getOutput(result.data ?: return@registerForActivityResult)
                resultUri?.let {
                    journalViewModel.setImageUri(it)
                    showImage()
                }
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                val cropError = UCrop.getError(result.data ?: return@registerForActivityResult)
                Log.e("uCrop", "Crop error: $cropError")
                showToast(getString(R.string.crop_error))
            }
        }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivJournalCover.setImageURI(it)
        }
    }

    private fun showBackAlertDialog() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.alert_back_journal_dialog, null)
        val btnDelete = dialogView.findViewById<TextView>(R.id.btn_delete)
        val btnCancel = dialogView.findViewById<TextView>(R.id.btn_cancel)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnDelete.setOnClickListener {
            finish()
        }

        btnCancel.setOnClickListener { alertDialog.dismiss() }

        alertDialog.show()
    }

    private fun addTextChangedListener(titleEditText: EditText) {
        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    titleEditText.setTextColor(
                        ContextCompat.getColor(
                            this@WriteJournalActivity,
                            R.color.blue_70
                        )
                    )
                } else {
                    titleEditText.setTextColor(
                        ContextCompat.getColor(
                            this@WriteJournalActivity,
                            R.color.blue
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressIndicator.visibility = View.VISIBLE
            binding.progressIndicator.show()
        } else {
            binding.progressIndicator.visibility = View.GONE
            binding.progressIndicator.hide()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
        const val EXTRA_JOURNAL = "extra_journal"
    }
}