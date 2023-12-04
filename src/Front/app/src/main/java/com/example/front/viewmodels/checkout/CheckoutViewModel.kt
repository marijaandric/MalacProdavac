package com.example.front.viewmodels.checkout

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.repository.MongoRepository
import com.example.front.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val repository: Repository,
    internal val dataStoreManager: DataStoreManager,
    private val mongoRepository: MongoRepository
) : ViewModel() {

    private val _state = mutableStateOf<List<Int>>(emptyList())
    val state: State<List<Int>> = _state

    suspend fun getCheckoutData() {
        try {
            mongoRepository.getUniqueShops().collect() { shops ->
                _state.value = shops
            }
        } catch (e: Exception) {
            Log.e("CheckoutViewModel", "Error fetching checkout data: ${e.message}")
        }
    }
}