package com.example.buddyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buddyapp.data.MedicineRepository
import com.example.buddyapp.data.api.ApiConfig
import com.example.buddyapp.data.local.BuddyRoomDatabase
import com.example.buddyapp.ui.medicine.MedicineViewModel

class MedicineViewModelFactory private constructor(private val repository: MedicineRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MedicineViewModel::class.java) -> {
                MedicineViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        @Volatile
        private var instance: MedicineViewModelFactory? = null
        fun getInstance(context: Context): MedicineViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MedicineViewModelFactory(MedicineRepository(ApiConfig.getApiService(), BuddyRoomDatabase.getDatabase(context).medicineDao()))
            }.also { instance = it }
    }
}