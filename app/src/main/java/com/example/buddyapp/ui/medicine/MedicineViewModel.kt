package com.example.buddyapp.ui.medicine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buddyapp.data.MedicineRepository
import com.example.buddyapp.data.api.ApiConfig
import com.example.buddyapp.data.local.Medicine
import kotlinx.coroutines.launch

class MedicineViewModel(private val medicineRepository: MedicineRepository) : ViewModel() {

    private val _listMedicine = MutableLiveData<List<Medicine>>()
    val listMedicine: LiveData<List<Medicine>> = _listMedicine

    private val _detailMedicine = MutableLiveData<String>()
    val detailMedicine: LiveData<String> = _detailMedicine

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getAllMedicine()
    }

    private fun getAllMedicine() {
        _isLoading.value = true
        viewModelScope.launch {
            val response = medicineRepository.getAllMedicines()
            _isLoading.value = false
            val medicines = response.map { medicine ->
                Medicine(
                    name = medicine.name,
                    price = medicine.price,
                    url = medicine.url,
                    description = medicine.description
                )
            }
            _listMedicine.value = medicines

            viewModelScope.launch {
                medicineRepository.insertMedicines(medicines)
            }
        }
    }

    fun getMedicineDetail(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val response = ApiConfig.getApiService().getMedicineDetail(name)
            _detailMedicine.value = response.imageUrl
            _isLoading.value = false
        }
    }

    fun searchMedicine(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val response = medicineRepository.searchMedicine(query)
            _listMedicine.value = response
            _isLoading.value = false
        }
    }

    fun retryGetMedicine() {
        getAllMedicine()
    }
}