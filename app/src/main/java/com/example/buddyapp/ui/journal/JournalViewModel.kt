package com.example.buddyapp.ui.journal

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.buddyapp.data.JournalRepository
import com.example.buddyapp.data.local.Journal
import com.example.buddyapp.data.local.JournalEntry
import com.example.buddyapp.data.local.ResultJournal
import com.example.buddyapp.helper.DateHelper
import kotlinx.coroutines.launch
import java.time.LocalDate

class JournalViewModel(application: Application) : AndroidViewModel(application) {
    private val mJournalRepository: JournalRepository = JournalRepository(application)

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: MutableLiveData<Uri> get() = _imageUri

    private val _saveResult = MutableLiveData<Boolean>()
    val saveResult: LiveData<Boolean> get() = _saveResult

    private val _detailJournal = MutableLiveData<Journal>()
    val detailJournal: LiveData<Journal> get() = _detailJournal

    private val _resultJournal = MutableLiveData<ResultJournal>()
    val resultJournal: LiveData<ResultJournal> get() = _resultJournal

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun getAllJournal(): LiveData<List<Journal>> = mJournalRepository.getAllJournal()

    fun getJournalById(journalId: Int) {
        viewModelScope.launch {
            val journal = mJournalRepository.getJournalById(journalId)
            _detailJournal.postValue(journal)
        }
    }

    fun getNewestJournalId(): LiveData<Int> {
        return mJournalRepository.getNewestJournalId()
    }

    fun insertJournal(journal: Journal?) {
        viewModelScope.launch {
            try {
                if (journal != null) {
                    mJournalRepository.insertJournal(journal)
                }
                _saveResult.postValue(true)
            } catch (e: Exception) {
                _saveResult.postValue(false)
            }
        }
    }

    fun updateJournal(journal: Journal?) {
        viewModelScope.launch {
            try {
                if (journal != null) {
                    mJournalRepository.updateJournal(journal)
                }
                _saveResult.postValue(true)
            } catch (e: Exception) {
                _saveResult.postValue(false)
            }
        }
    }

    fun analyzeStatusUpdate(id: Int, isAnalyzed: Boolean) {
        viewModelScope.launch {
            mJournalRepository.updateIsAnalyzed(id, isAnalyzed)
        }
    }

    fun deleteJournal(journal: Journal) {
        mJournalRepository.deleteJournal(journal)
    }

    fun saveResultJournal(resultJournal: ResultJournal) {
        viewModelScope.launch {
            val isSaved = mJournalRepository.isResultJournalSaved(resultJournal.id)
            if (!isSaved) {
                mJournalRepository.insertResultJournal(resultJournal)
            }
        }
    }

    fun deleteResultJournal(id: Int) {
        viewModelScope.launch {
            mJournalRepository.deleteResultJournal(id)
        }
    }

    fun getResultJournal(journalId: Int) {
        viewModelScope.launch {
            val resultJournal = mJournalRepository.getResultJournal(journalId)
            _resultJournal.postValue(resultJournal)
        }
    }

    fun getAllJournalsHistory(): LiveData<List<JournalEntry>> = mJournalRepository.allJournalsHistory

    fun addJournalHistory(date: String?, title: String?) {
        viewModelScope.launch {
            mJournalRepository.insertJournalHistory(JournalEntry(date = date, title = title))
        }
    }

    private val _currentStreak = MutableLiveData<Int>()
    val currentStreak: LiveData<Int> = _currentStreak

    private val _highestStreak = MutableLiveData<Int>()
    val highestStreak: LiveData<Int> = _highestStreak

    fun writeJournal(initialDate: String) {
        viewModelScope.launch {
            val date = DateHelper.convertTimestampToDate(initialDate.toLong())
            mJournalRepository.writeJournal(date)
            _currentStreak.postValue(mJournalRepository.getStreakData(date).currentStreak)
            _highestStreak.postValue(mJournalRepository.getHighestStreak())
        }
    }

    fun getStreakData() {
        viewModelScope.launch {
            val streak = mJournalRepository.getStreakData(date = LocalDate.now().toString())
            _currentStreak.postValue(streak.currentStreak)
            _highestStreak.postValue(mJournalRepository.getHighestStreak())
        }
    }
}