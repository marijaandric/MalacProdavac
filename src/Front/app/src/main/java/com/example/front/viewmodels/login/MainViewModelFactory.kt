package com.example.front.viewmodels.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.front.helper.DataStoreManager
import com.example.front.repository.Repository
import com.example.front.viewmodels.RegisterViewModel

//ViewmModelProvider.Factory
class MainViewModelFactory(private val repository: Repository,private val dataStoreManager: DataStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository, dataStoreManager) as T
        }

        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(repository) as T
        }

        throw IllegalArgumentException("Nepodržan tip ViewModel-a: ${modelClass.name}")
    }
}