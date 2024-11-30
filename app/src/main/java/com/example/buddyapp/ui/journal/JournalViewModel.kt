package com.example.buddyapp.ui.journal

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buddyapp.data.JournalRepository
import com.example.buddyapp.data.local.Journal
import kotlinx.coroutines.launch

class JournalViewModel(application: Application) : ViewModel() {
    private val mJournalRepository: JournalRepository = JournalRepository(application)

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: MutableLiveData<Uri> get() = _imageUri

    private val _saveResult = MutableLiveData<Boolean>()
    val saveResult: LiveData<Boolean> get() = _saveResult

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun getAllJournal(): LiveData<List<Journal>> = mJournalRepository.getAllJournal()

    fun getJournalById(id: String): LiveData<Journal> = mJournalRepository.getJournalById(id)

    fun insert(journal: Journal) {
        viewModelScope.launch {
            try {
                mJournalRepository.insert(journal)
                _saveResult.postValue(true) // Notify save was successful
            } catch (e: Exception) {
                _saveResult.postValue(false) // Notify save failed
            }
        }
    }

    suspend fun update(journal: Journal) {
        mJournalRepository.update(journal)
    }

    suspend fun delete(journal: Journal) {
        mJournalRepository.delete(journal)
    }
}