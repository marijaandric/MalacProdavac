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
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val repository: Repository,
    val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _state = mutableStateOf(ShopsState())
    var state: State<ShopsState> = _state;

    private val _stateFav = mutableStateOf(ShopsState())
    var stateFav: State<ShopsState> = _stateFav;

    private val _statePageCount = mutableStateOf(1)
    var statePageCount: State<Int> = _statePageCount;

    private val _statePageCountFav = mutableStateOf(1)
    var statePageCountFav: State<Int> = _statePageCountFav;

    private val _usernameFlow = MutableStateFlow("")
    val usernameFlow: Flow<String> = _usernameFlow

    // defaultni filteri
    private val _filtersState = mutableStateOf(FiltersDTO(0,null, null, null,null, null, 0, null, 1, null, null, null))
    var filtersState: State<FiltersDTO> = _filtersState;


    fun getShops(userId: Int,categories:List<Int>?, rating:Int?,open:Boolean?,range:Int?, location:String?,sort:Int,search:String?,page:Int,favorite:Boolean,currLat:Float?, currLong:Float?) {
        inicijalnoStanje()
        viewModelScope.launch {
            try {
                val response = repository.getShops(userId,categories,rating,open,range,location,sort,search,page,favorite, currLat, currLong)

                _filtersState.value = _filtersState.value.copy(
                    userId = userId, categories = categories, rating = rating, open = open, range = range, location = location, sort = sort, search =search, page = page, currLat = currLat, currLon = currLong
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
                    getShopPages(userId,false)
                    getShopPages(userId,true)

                }
                else{
                    if(!favorite)
                    {
                        _state.value = _state.value.copy(
                            error = "NotFound"
                        )
                    }
                    else
                    {
                        _stateFav.value = _stateFav.value.copy(
                            error = "NotFound"
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

    fun getShopPages(id:Int,fav:Boolean)
    {
        viewModelScope.launch {
            try {
                val response = repository.getShopPages(id,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.search, fav, _filtersState.value.currLat, _filtersState.value.currLon)
                Log.d("RES PAGE", response.toString())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(fav)
                    {
                        _statePageCount.value = responseBody!!.pageCount
                    }
                    else{
                        _statePageCountFav.value = responseBody!!.pageCount
                    }
                }
                else{
                    if(fav)
                    {
                        _statePageCount.value = 0
                    }
                    else{
                        _statePageCountFav.value = 0
                    }
                }
            }
            catch (e: Exception)
            {
                if(fav)
                {
                    _statePageCount.value = 0
                }
                else{
                    _statePageCountFav.value = 0
                }
            }
        }
    }

    fun Sort(s:Int)
    {
        _filtersState.value = _filtersState.value.copy(
            sort = s
        )
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, true, _filtersState.value.currLat, _filtersState.value.currLon)
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, false, _filtersState.value.currLat, _filtersState.value.currLon)
    }

    fun ChangePage(s:Int)
    {
        _filtersState.value = _filtersState.value.copy(
            page = s
        )
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, true, _filtersState.value.currLat, _filtersState.value.currLon)
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, false, _filtersState.value.currLat, _filtersState.value.currLon)
    }

    fun Search(s:String)
    {
        _filtersState.value = _filtersState.value.copy(
            search = s
        )
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, true, _filtersState.value.currLat, _filtersState.value.currLon)
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, false, _filtersState.value.currLat, _filtersState.value.currLon)
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
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, true, _filtersState.value.currLat, _filtersState.value.currLon)
        getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, false, _filtersState.value.currLat, _filtersState.value.currLon)
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
            userId = 1,categories = listOf(),rating= null, open=null,range=0,location= null,sort= 0, search = null,page =1
        )
    }

    fun changeCategories(categories: List<Int>?)
    {
        _filtersState.value = _filtersState.value.copy(
            categories = categories
        )
    }
    fun changeRating(rating: Int?)
    {
        _filtersState.value = _filtersState.value.copy(
            rating = rating
        )
    }
    fun changeOpen(open: Boolean?)
    {
        _filtersState.value = _filtersState.value.copy(
            open = open
        )
    }
    fun changeRange(range: Int?)
    {
        _filtersState.value = _filtersState.value.copy(
            range = range
        )
    }
    fun changeLocation(location: String?)
    {
        _filtersState.value = _filtersState.value.copy(
            location = location
        )
    }

    fun changeCoordinates(geoPoint: GeoPoint?)
    {
        _filtersState.value = _filtersState.value.copy(
            currLat = geoPoint!!.latitude.toFloat(),
            currLon = geoPoint!!.longitude.toFloat(),
        )
    }
}
