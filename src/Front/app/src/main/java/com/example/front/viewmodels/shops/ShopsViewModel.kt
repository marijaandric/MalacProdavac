package com.example.front.viewmodels.shops

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.FiltersDTO
import com.example.front.model.DTO.ShopDTO
import com.example.front.repository.Repository
import com.example.front.screens.sellers.states.ShopsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = mutableStateOf(ShopsState())
    var state: State<ShopsState> = _state;

    private val _stateFav = mutableStateOf(ShopsState())
    var stateFav: State<ShopsState> = _stateFav;

    private val _usernameFlow = MutableStateFlow("")
    val usernameFlow: Flow<String> = _usernameFlow

    // defaultni filteri
    private val _filtersState = mutableStateOf(FiltersDTO(0,null, null, null,null, null, 0, null, 1))
    var filtersState: State<FiltersDTO> = _filtersState;


    fun getShops(userId: Int,categories:List<Int>?, rating:Int?,open:Boolean?,range:Int?, location:String?,sort:Int,search:String?,page:Int,favorite:Boolean) {
        inicijalnoStanje()
        viewModelScope.launch {
            try {
                val response = repository.getShops(userId,categories,rating,open,range,location,sort,search,page,favorite)
                Log.d("VALUES",_filtersState.value.toString())
                Log.d("TAAAAG",response.body().toString())
                _filtersState.value = _filtersState.value.copy(
                    userId = userId, categories = categories, rating = rating, open = open, range = range, location = location, sort = sort, search =search, page = page
                )
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(!favorite)
                    {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            shops = response.body()
                        )
                    }
                    else
                    {
                        _stateFav.value = _stateFav.value.copy(
                            isLoading = false,
                            shops = response.body()
                        )
                    }


                }
                else{
                    if(!favorite)
                    {
                        _state.value = _state.value.copy(
                            error = "Error"
                        )
                    }
                    else
                    {
                        _stateFav.value = _stateFav.value.copy(
                            error = "Error"
                        )
                    }
                }
            } catch (e: Exception) {
                if(!favorite)
                {
                    _state.value = _state.value.copy(
                        error = e.message.toString()
                    )
                }
                else{
                    _stateFav.value = _stateFav.value.copy(
                        error = e.message.toString()
                    )
                }
            }
        }
    }

    fun Sort(s:Int)
    {
        _filtersState.value = _filtersState.value.copy(
            sort = s
        )
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, true)
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, false)
    }

    fun ChangePage(s:Int)
    {
        _filtersState.value = _filtersState.value.copy(
            page = s
        )
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, true)
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, false)
    }

    fun Search(s:String)
    {
        _filtersState.value = _filtersState.value.copy(
            search = s
        )
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, true)
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, false)
    }

    fun DialogFilters(categories: List<Int>?, rating: Int?, open: Boolean?, location: String?, range: Int?)
    {
        _filtersState.value = _filtersState.value.copy(
            categories = categories,
            rating = rating,
            open = open,
            location = location,
            range = range
        )
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, true)
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, false)
    }

    suspend fun getUserId(): Int?
    {
        return dataStoreManager.getUserIdFromToken()
    }

    fun inicijalnoStanje()
    {
        _state.value = _state.value.copy(
            isLoading = true,
            shops = emptyList(),
            error = ""
        )
        _stateFav.value = _stateFav.value.copy(
            isLoading = true,
            shops = emptyList(),
            error = ""
        )
    }

    fun withoutFilters()
    {
        _filtersState.value = _filtersState.value.copy(
            1, listOf(), null, null,0, "none", 0, "E", 1
        )
    }
}
