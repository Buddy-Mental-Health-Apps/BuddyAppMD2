package com.example.buddyapp.data

import com.example.buddyapp.data.api.ApiService
import com.example.buddyapp.data.api.response.MedicineResponseItem
import com.example.buddyapp.data.local.Medicine
import com.example.buddyapp.data.local.MedicineDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MedicineRepository(private val apiService: ApiService, private val medicineDao: MedicineDao) {
    suspend fun getAllMedicines(): List<MedicineResponseItem> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getAllMedicines()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun insertMedicines(medicines: List<Medicine>) {
        withContext(Dispatchers.IO) {
            medicineDao.insertMedicines(medicines)
        }
    }

    suspend fun searchMedicine(query: String): List<Medicine> {
        return medicineDao.searchMedicine(query)
    }
}