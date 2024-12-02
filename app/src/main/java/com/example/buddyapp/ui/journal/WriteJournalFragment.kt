package com.example.buddyapp.ui.journal

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.buddyapp.R
import com.example.buddyapp.data.local.Journal
import com.example.buddyapp.databinding.FragmentWriteJournalBinding
import com.example.buddyapp.helper.DateHelper
import com.example.dicodingevents.ui.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yalantis.ucrop.UCrop
import java.io.File

class WriteJournalFragment : Fragment() {

    private var _binding: FragmentWriteJournalBinding? = null
    private val binding get() = _binding!!

    private lateinit var journalViewModel: JournalViewModel

    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Hide the action bar
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        val bottomNav =
            (activity as? AppCompatActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav?.visibility = View.GONE
        
        _binding = FragmentWriteJournalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity().application)
        journalViewModel = ViewModelProvider(this, factory)[JournalViewModel::class.java]

        val titleEditText = binding.etJournalTitle
        val backButton = binding.btnBack

        titleEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_70))

        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    titleEditText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.blue_70
                        )
                    )
                } else {
                    titleEditText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.blue
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        journalViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                currentImageUri = uri
                binding.ivJournalCover.setImageURI(uri)
            }
        }

        binding.btnOpenGallery.setOnClickListener { startGallery() }
        binding.btnSave.setOnClickListener { saveJournal() }

        journalViewModel.saveResult.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                navigateToDetailJournalPage()
            } else {
                Toast.makeText(requireContext(), "Failed to save journal.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun navigateToDetailJournalPage() {
        val title = binding.etJournalTitle.text.toString()
        val description = binding.etJournalContent.text.toString()
        val imageUri = currentImageUri.toString()

        val action =
            WriteJournalFragmentDirections.actionWriteJournalFragmentToDetailJournalFragment(
                title, description, imageUri
            )
        findNavController().navigate(action)
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
                        requireActivity().cacheDir,
                        "cropped_image_${System.currentTimeMillis()}.jpg"
                    )
                )
            uCropLauncher.launch(
                UCrop.of(uri, destinationUri)
                    .getIntent(requireActivity())
            )
        } else {
            Log.d("Photo Picker", "No Media Selected")
        }
    }

    private val uCropLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
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

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun saveJournal() {
        val journal = Journal(
            title = binding.etJournalTitle.text.toString(),
            image = currentImageUri.toString(),
            description = binding.etJournalContent.text.toString(),
            timestamp = DateHelper.getCurrentDate()
        )
        journalViewModel.insert(journal)

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        val bottomNav =
            (activity as? AppCompatActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav?.visibility = View.VISIBLE
    }

}