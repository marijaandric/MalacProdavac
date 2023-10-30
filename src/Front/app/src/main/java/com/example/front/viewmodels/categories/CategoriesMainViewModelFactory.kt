package com.example.front.viewmodels.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.front.repository.Repository

class CategoriesMainViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            return CategoriesViewModel(repository) as T
        }

        throw IllegalArgumentException("Nepodržan tip ViewModel-a: ${modelClass.name}")
    }
}
