package com.example.front.viewmodels.products

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.FiltersDTO
import com.example.front.repository.MongoRepository
import com.example.front.repository.Repository
import com.example.front.screens.product.ProductProductState
import com.example.front.screens.product.ReviewProductState
import com.example.front.screens.products.state.ProductsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: Repository,
    internal val dataStoreManager: DataStoreManager,
    private val mongoRepository: MongoRepository
) : ViewModel() {

    private val _state = mutableStateOf(ProductsState())
    var state: State<ProductsState> = _state;

    private val _stateFav = mutableStateOf(ProductsState())
    var stateFav: State<ProductsState> = _stateFav;

    private val _stateReview = mutableStateOf(ReviewProductState())
    var stateReview: State<ReviewProductState> = _stateReview;

    private val _filtersState =
        mutableStateOf(FiltersDTO(0, listOf(), 0, null, 0, null, 0, "", 1, null, null, null))
    var filtersState: State<FiltersDTO> = _filtersState;



    fun getProducts(userID: Int,categories:List<Int>?, rating:Int?,open:Boolean?,range:Int?, location:String?,sort:Int,search:String?,page:Int,favorite:Boolean?,currLat:Float?, currLong:Float?) {
        viewModelScope.launch {
            try {
                inicijalnoStanje()
                _filtersState.value = _filtersState.value.copy(
                    userId = userID,
                    categories = categories,
                    rating = rating,
                    open = open,
                    range = range,
                    location = location,
                    sort = sort,
                    search = search,
                    page = page,
                    favorite = favorite,
                    currLat = currLat,
                    currLon = currLong
                )
                Log.d("FILTERI", _filtersState.toString())
                val response = repository.getProducts(userID,categories,rating,open,range,location,sort,search,page,null,favorite, currLat, currLong)
                Log.d("PRODUCTS",response.toString())
                if (response.isSuccessful) {
                    val products= response.body()
                    if(!favorite!!) {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            products = products,
                            error = ""
                        )
                    }
                    else if(favorite!!){
                        _stateFav.value = _stateFav.value.copy(
                            isLoading = false,
                            products = products,
                            error = ""
                        )
                    }
                } else {
                    if(!favorite!!) {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            products = emptyList(),
                            error = "NotFound"
                        )
                    }
                    else if(favorite!!){
                        _stateFav.value = _stateFav.value.copy(
                            isLoading = false,
                            products = emptyList(),
                            error = "NotFound"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    fun inicijalnoStanje() {
        _state.value = _state.value.copy(
            isLoading = true,
            products = emptyList(),
            error = ""
        )
        _stateFav.value = _stateFav.value.copy(
            isLoading = true,
            products = emptyList(),
            error = ""
        )
    }

    suspend fun getUserId(): Int? {
        return dataStoreManager.getUserIdFromToken()
    }

    fun withoutFilters() {
        _filtersState.value = _filtersState.value.copy(
            1, listOf(), null, null, 0, null, 0, null, 1
        )
    }

    fun changeCategories(categories: List<Int>?) {
        _filtersState.value = _filtersState.value.copy(
            categories = categories
        )
    }

    fun changeRating(rating: Int?) {
        _filtersState.value = _filtersState.value.copy(
            rating = rating
        )
    }

    fun changeOpen(open: Boolean?) {
        _filtersState.value = _filtersState.value.copy(
            open = open
        )
    }

    fun changeRange(range: Int?) {
        _filtersState.value = _filtersState.value.copy(
            range = range
        )
    }

    fun changeLocation(location: String?) {
        _filtersState.value = _filtersState.value.copy(
            location = location
        )
    }

    fun changeCoordinates(geoPoint: GeoPoint?) {
        _filtersState.value = _filtersState.value.copy(
            currLat = geoPoint!!.latitude.toFloat(),
            currLon = geoPoint!!.longitude.toFloat(),
        )
    }


    fun Sort(s: Int) {
        _filtersState.value = _filtersState.value.copy(
            sort = s
        )
        getProducts(
            _filtersState.value.userId,
            _filtersState.value.categories,
            _filtersState.value.rating,
            _filtersState.value.open,
            _filtersState.value.range,
            _filtersState.value.location,
            _filtersState.value.sort,
            _filtersState.value.search,
            _filtersState.value.page,
            true,
            _filtersState.value.currLat,
            _filtersState.value.currLon
        )
        getProducts(
            _filtersState.value.userId,
            _filtersState.value.categories,
            _filtersState.value.rating,
            _filtersState.value.open,
            _filtersState.value.range,
            _filtersState.value.location,
            _filtersState.value.sort,
            _filtersState.value.search,
            _filtersState.value.page,
            false,
            _filtersState.value.currLat,
            _filtersState.value.currLon
        )
    }

    fun ChangePage(s: Int) {
        _filtersState.value = _filtersState.value.copy(
            page = s
        )
        //getShops(_filtersState.value.userId,_filtersState.value.categories,_filtersState.value.rating,_filtersState.value.open,_filtersState.value.range,_filtersState.value.location,_filtersState.value.sort,_filtersState.value.search,_filtersState.value.page, true, _filtersState.value.currLat, _filtersState.value.currLon)
    }

    fun Search(s: String) {
        _filtersState.value = _filtersState.value.copy(
            search = s
        )
        getProducts(
            _filtersState.value.userId,
            _filtersState.value.categories,
            _filtersState.value.rating,
            _filtersState.value.open,
            _filtersState.value.range,
            _filtersState.value.location,
            _filtersState.value.sort,
            _filtersState.value.search,
            _filtersState.value.page,
            true,
            _filtersState.value.currLat,
            _filtersState.value.currLon
        )
        getProducts(
            _filtersState.value.userId,
            _filtersState.value.categories,
            _filtersState.value.rating,
            _filtersState.value.open,
            _filtersState.value.range,
            _filtersState.value.location,
            _filtersState.value.sort,
            _filtersState.value.search,
            _filtersState.value.page,
            false,
            _filtersState.value.currLat,
            _filtersState.value.currLon
        )
    }

    fun DialogFilters(
        categories: List<Int>?,
        rating: Int?,
        open: Boolean?,
        location: String?,
        range: Int?
    ) {
        _filtersState.value = _filtersState.value.copy(
            categories = categories,
            rating = rating,
            open = open,
            location = location,
            range = range
        )
        Log.d("STA SALJEM", filtersState.toString())
        getProducts(
            _filtersState.value.userId,
            _filtersState.value.categories,
            _filtersState.value.rating,
            _filtersState.value.open,
            _filtersState.value.range,
            _filtersState.value.location,
            _filtersState.value.sort,
            _filtersState.value.search,
            _filtersState.value.page,
            true,
            _filtersState.value.currLat,
            _filtersState.value.currLon
        )
        getProducts(
            _filtersState.value.userId,
            _filtersState.value.categories,
            _filtersState.value.rating,
            _filtersState.value.open,
            _filtersState.value.range,
            _filtersState.value.location,
            _filtersState.value.sort,
            _filtersState.value.search,
            _filtersState.value.page,
            false,
            _filtersState.value.currLat,
            _filtersState.value.currLon
        )
    }
}