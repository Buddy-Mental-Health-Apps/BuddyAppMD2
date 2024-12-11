package com.example.buddyapp.ui.medicine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buddyapp.data.MedicineRepository
import com.example.buddyapp.data.api.response.DetailMedResponseItem
import com.example.buddyapp.data.local.Medicine
import kotlinx.coroutines.launch

class MedicineViewModel(private val medicineRepository: MedicineRepository) : ViewModel() {

    private val _listMedicine = MutableLiveData<List<Medicine>>()
    val listMedicine: LiveData<List<Medicine>> = _listMedicine

    private val _detailMedicine = MutableLiveData<List<DetailMedResponseItem>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getAllMedicine()
    }

    private fun getAllMedicine() {
        _isLoading.value = true
        viewModelScope.launch {
            val response = medicineRepository.getAllMedicines()
            val medicineDetails = medicineRepository.getMedicineDetail()
            _detailMedicine.value = medicineDetails
            val medicines = response.map { medicine ->
                val image = getMedicineImageUrl(medicine.url)
                Medicine(
                    name = medicine.name,
                    price = medicine.price,
                    url = medicine.url,
                    description = medicine.description,
                    imageUrl = image
                )
            }
            _listMedicine.postValue(medicines)
            _isLoading.value = false
            viewModelScope.launch {
                medicineRepository.insertMedicines(medicines)
            }
        }
    }

    private fun getMedicineImageUrl(url: String?): String {
        val savedMedicineDetails = _detailMedicine.value
        val medicineImage = savedMedicineDetails?.find {
            it.url == url
        }
        return medicineImage?.imageUrl ?: ""
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