package com.example.buddyapp.data.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buddyapp.ui.authentication.LoginViewModel
import com.example.buddyapp.data.api.ApiService
import com.example.buddyapp.data.di.Injection
import com.example.buddyapp.data.ds.UserRepository

class ViewModelFactory(
    private val repository: UserRepository,
    private val apiService: ApiService,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository, apiService) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideRepository(context),
                        Injection.provideApiService()
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
