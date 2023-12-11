package com.example.front.viewmodels.delivery

import androidx.lifecycle.ViewModel
import com.example.front.helper.DataStore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RouteDetailsViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager
) : ViewModel()