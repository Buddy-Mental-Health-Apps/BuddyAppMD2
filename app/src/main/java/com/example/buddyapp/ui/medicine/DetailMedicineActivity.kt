package com.example.buddyapp.ui.medicine

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.buddyapp.data.local.Medicine
import com.example.buddyapp.databinding.ActivityDetailMedicineBinding
import com.example.buddyapp.ui.MedicineViewModelFactory

class DetailMedicineActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailMedicineBinding
    private val viewModel: MedicineViewModel by viewModels { MedicineViewModelFactory.getInstance(this) }

    private lateinit var name: String
    private var price: String? = null
    private var description: String? = null
    private var url: String? = null
    private var medicine: Medicine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        name = intent.getStringExtra(EXTRA_NAME).toString()
        price = intent.getStringExtra(EXTRA_PRICE)
        description = intent.getStringExtra(EXTRA_DESCRIPTION)
        url = intent.getStringExtra(EXTRA_URL)
        medicine = Medicine(
            name = name,
            price = price,
            description = description,
            url = url
        )
        binding.tvName.text = medicine?.name
        binding.tvPrice.text = medicine?.price
        binding.tvDescription.text = medicine?.description
        getMedicineImage(name)
        binding.btnLink.setOnClickListener(this)
    }

    private fun getMedicineImage(name: String) {
        val image = viewModel.getMedicineDetail(name)
        Glide.with(this)
            .load(image)
            .into(binding.ivMedImage)
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
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PRICE = "extra_price"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_URL = "extra_url"
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnLink.id -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
    }
}