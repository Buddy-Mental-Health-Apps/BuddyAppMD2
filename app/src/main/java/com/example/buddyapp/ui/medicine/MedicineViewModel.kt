package com.example.buddyapp.ui.medicine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MedicineViewModel : ViewModel() {

//    private val _listMedicine = MutableLiveData<List<Medicine>>()
//    val listMedicine: LiveData<List<Medicine>> = _listMedicine

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getAllMedicine()
    }

    private fun getAllMedicine() {
        TODO("Not yet implemented")
    }

    fun searchMedicine(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            TODO("Not yet implemented")
//            _isLoading.value = false
//            _listMedicine.value = TODO("Not yet implemented")
        }
    }

    fun retryGetMedicine() {
        getAllMedicine()
    }
}