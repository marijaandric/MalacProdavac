package com.example.front.viewmodels.oneshop

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.front.helper.DataStore.DataStoreManager
import com.example.front.model.DTO.EditShopDTO
import com.example.front.model.DTO.FiltersDTO
import com.example.front.model.DTO.NewProductDTO
import com.example.front.model.DTO.NewProductDisplayDTO
import com.example.front.repository.Repository
import com.example.front.screens.myshop.getMultipartBodyPart
import com.example.front.screens.myshop.state.ImageState
import com.example.front.screens.shop.state.DeleteProductDisplayState
import com.example.front.screens.shop.state.DeleteShopState
import com.example.front.screens.shop.state.EditShopState
import com.example.front.screens.shop.state.GetCategoriesState
import com.example.front.screens.shop.state.GetMetricsState
import com.example.front.screens.shop.state.NewDisplayState
import com.example.front.screens.shop.state.PostReviewState
import com.example.front.screens.shop.state.ProductDisplayState
import com.example.front.screens.shop.state.ProductState
import com.example.front.screens.shop.state.ShopReviewState
import com.example.front.screens.shop.state.ShopState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.osmdroid.util.GeoPoint
import java.io.File
import javax.inject.Inject


@HiltViewModel
class OneShopViewModel @Inject constructor(
    private val repository: Repository,
    val dataStoreManager: DataStoreManager
) : ViewModel() {

    sealed class UploadStatus {
        object Idle : UploadStatus()
        object InProgress : UploadStatus()
        data class Success(val response: com.example.front.model.response.Success) : UploadStatus()
        data class Error(val message: String) : UploadStatus()
    }

    private val _uploadStatus = MutableStateFlow<UploadStatus>(UploadStatus.Idle)
    val uploadStatus: StateFlow<UploadStatus> = _uploadStatus

    private val _state = mutableStateOf(ShopState())
    var state: State<ShopState> = _state;

    private val _stateProduct = mutableStateOf(ProductState())
    var stateProduct: State<ProductState> = _stateProduct;

    // defaultni filteri
    private val _filtersState =
        mutableStateOf(FiltersDTO(0, listOf(), 0, null, 0, null, 0, "", 1, null, null, null))
    var filtersState: State<FiltersDTO> = _filtersState;

    private val _stateReview = mutableStateOf(ShopReviewState())
    var stateReview: State<ShopReviewState> = _stateReview;

    //ShopReviewState
    private val _statePostReview = mutableStateOf(PostReviewState())
    var statePostReview: State<PostReviewState> = _statePostReview;

    //CategoriesGetState
    private val _stateGetMetrics = mutableStateOf(GetMetricsState())
    var stateGetMetrics: State<GetMetricsState> = _stateGetMetrics;

    //CategoriesGetState
    private val _stateGetCategories = mutableStateOf(GetCategoriesState())
    var stateGetCategories: State<GetCategoriesState> = _stateGetCategories;

    // prikaz product display
    private val _stateProductDisplay = mutableStateOf(ProductDisplayState())
    var stateProductDisplay: State<ProductDisplayState> = _stateProductDisplay;

    //delete product display
    private val _stateDeleteProductDisplay = mutableStateOf(DeleteProductDisplayState())
    var stateDeleteProductDisplay: State<DeleteProductDisplayState> = _stateDeleteProductDisplay;//

    //newProductDisplay
    private val _stateNewProductDisplay = mutableStateOf(NewDisplayState())
    var stateNewProductDisplay: State<NewDisplayState> = _stateNewProductDisplay;//

    //delete Shop
    private val _stateDeleteShop = mutableStateOf(DeleteShopState())
    var statedeleteShop: State<DeleteShopState> = _stateDeleteShop;

    //edit Shop
    private val _stateEditShop = mutableStateOf(EditShopState())
    var stateEditShop: State<EditShopState> = _stateEditShop;

    //slika
    private val _stateimage = mutableStateOf(ImageState())
    var stateimage: State<ImageState> = _stateimage;

    private val _stateProductID = mutableStateOf(1)
    var stateProductID: State<Int> = _stateProductID;

    private val _pictures = mutableStateOf<List<MultipartBody.Part>>(listOf())
    val pictures: State<List<MultipartBody.Part>> = _pictures

    fun setPictures(newPictures: List<MultipartBody.Part>) {
        _pictures.value = newPictures
    }

    fun uploadAllImages() {
        viewModelScope.launch {
            pictures.value.forEach { picture ->
                uploadImage(1, stateProductID.value, picture)
            }
        }
    }

    fun uploadAllImagesEdit(productId: Int) {
        viewModelScope.launch {
            pictures.value.forEach { picture ->
                uploadImage(1, productId, picture)
            }
        }
    }

    fun processSelectedImages(uris: List<Uri>, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val pictures = uris.mapNotNull { uri ->
                try {
                    getMultipartBodyPart(context, uri)
                } catch (e: Exception) {
                    null
                }
            }
            withContext(Dispatchers.Main) {
                setPictures(pictures)
            }
        }
    }

    fun getShopDetails(userId: Int, shopId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getShopDetails(userId, shopId)
                if (response.isSuccessful) {
                    val shopp = response.body()
                    _state.value = _state.value.copy(
                        isLoading = false,
                        shop = shopp
                    )
                } else {
                    _state.value = _state.value.copy(
                        error = "NotFound"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }

    fun editShop(editShop: EditShopDTO) {
        viewModelScope.launch {
            try {
                Log.d("EDITSHOPDTO", editShop.toString())
                val response = repository.editShop(editShop)
                Log.d("EDIT", response.toString())
                if (response.isSuccessful) {
                    val shopp = response.body()
                    _stateEditShop.value = _stateEditShop.value.copy(
                        isLoading = false,
                        success = shopp,
                        error = ""
                    )
                } else {
                    _stateEditShop.value = _stateEditShop.value.copy(
                        isLoading = false,
                        success = null,
                        error = "NotFound"
                    )
                }
            } catch (e: Exception) {
                _stateEditShop.value = _stateEditShop.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }

    fun getProducts(
        userId: Int,
        categories: List<Int>?,
        rating: Int?,
        open: Boolean?,
        range: Int?,
        location: String?,
        sort: Int,
        search: String?,
        page: Int,
        specificShop: Int?,
        favorite: Boolean?,
        currLat: Float?,
        currLong: Float?
    ) {
        inicijalnoStanje()
        viewModelScope.launch {
            try {
                val response = repository.getProducts(
                    userId,
                    categories,
                    rating,
                    open,
                    range,
                    location,
                    sort,
                    search,
                    page,
                    specificShop,
                    favorite,
                    currLat,
                    currLong
                )
                _filtersState.value = _filtersState.value.copy(
                    userId = userId,
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
                if (response.isSuccessful) {
                    val prod = response.body()
                    _stateProduct.value = _stateProduct.value.copy(
                        isLoading = false,
                        products = prod
                    )
                } else {
                    _stateProduct.value = _stateProduct.value.copy(
                        isLoading = false,
                        error = "NotFound"
                    )
                }
            } catch (e: Exception) {
                _stateProduct.value = _stateProduct.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }

    fun getShopReview(shopId: Int, page: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getShopReviews(shopId, page)
                if (response.isSuccessful) {
                    val rev = response.body()
                    _stateReview.value = _stateReview.value.copy(
                        isLoading = false,
                        reviews = rev!!,
                        error = ""
                    )
                } else {
                    _stateReview.value = _stateReview.value.copy(
                        error = "NotFound"
                    )
                }
            } catch (e: Exception) {
                _stateReview.value = _stateReview.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }

    fun leaveReview(shopId: Int, userId: Int, rating: Int?, comment: String) {
        viewModelScope.launch {
            try {
                val response = repository.postShopReview(shopId, userId, rating, comment)
                if (response.isSuccessful) {
                    val rev = response.body()
                    _statePostReview.value = _statePostReview.value.copy(
                        isLoading = false,
                        review = response.body(),
                        error = ""
                    )
                } else {
                    _statePostReview.value = _statePostReview.value.copy(
                        isLoading = false,
                        review = null,
                        error = "NotFound"
                    )
                }
            } catch (e: Exception) {
                _statePostReview.value = _statePostReview.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }

    suspend fun getUserId(): Int? {
        return dataStoreManager.getUserIdFromToken()
    }

    fun changeToggleLike(userId: Int, shopId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.toggleLike(shopId, userId)
            } catch (e: Exception) {
                Log.d("Error", "Error - cant change")
            }
        }
    }

    fun inicijalnoStanje() {
        _stateProduct.value = _stateProduct.value.copy(
            isLoading = true,
            products = emptyList(),
            error = ""
        )
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


    fun Sort(s: Int, specificShopId: Int?) {
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
            specificShopId,
            _filtersState.value.favorite,
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

    fun Search(s: String, specificShopId: Int?) {
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
            specificShopId,
            _filtersState.value.favorite,
            _filtersState.value.currLat,
            _filtersState.value.currLon
        )
    }

    fun DialogFilters(
        categories: List<Int>?,
        rating: Int?,
        open: Boolean?,
        location: String?,
        range: Int?,
        specificShopId: Int?
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
            specificShopId,
            _filtersState.value.favorite,
            _filtersState.value.currLat,
            _filtersState.value.currLon
        )
    }

    fun getCategories() {
        viewModelScope.launch {
            try {
                val response = repository.getCategories()
                if (response.isSuccessful) {
                    val rev = response.body()
                    _stateGetCategories.value = _stateGetCategories.value.copy(
                        isLoading = false,
                        categories = response.body(),
                        error = ""
                    )
                } else {
                    _stateGetCategories.value = _stateGetCategories.value.copy(
                        error = "No categories"
                    )
                }
            } catch (e: Exception) {
                _stateGetCategories.value = _stateGetCategories.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }

    fun getMetrics() {
        viewModelScope.launch {
            try {
                val response = repository.getMetrics()
                if (response.isSuccessful) {
                    _stateGetMetrics.value = _stateGetMetrics.value.copy(
                        isLoading = false,
                        metrics = response.body(),
                        error = ""
                    )
                } else {
                    _stateGetCategories.value = _stateGetCategories.value.copy(
                        error = "No metrics"
                    )
                }
            } catch (e: Exception) {
                _stateGetCategories.value = _stateGetCategories.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }

    fun postNewProduct(newProd: NewProductDTO) {
        viewModelScope.launch {
            val response = repository.postNewProduct(newProd)
            val responseBody = response.body()

            if (responseBody != null) {
                _stateProductID.value = responseBody.id
            } else {
                Log.e("NEWPROD", "Response body or ID is null")
            }
        }
    }

    fun editProduct(newProd: NewProductDTO) {
        viewModelScope.launch {
            Log.d("EDITPRODUCT", newProd.toString())
            repository.editProduct(newProd)
        }
    }

    fun getProductDisplay(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getProductDisplay(id)
                if (response.isSuccessful) {
                    _stateProductDisplay.value = _stateProductDisplay.value.copy(
                        isLoading = false,
                        displayProduct = response.body(),
                        error = ""
                    )
                } else {
                    _stateProductDisplay.value = _stateProductDisplay.value.copy(
                        isLoading = false,
                        displayProduct = null,
                        error = "No product display"
                    )
                }
            } catch (e: Exception) {
                _stateProductDisplay.value = _stateProductDisplay.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }

    fun deleteProductDisplay(id: Int?) {
        viewModelScope.launch {
            try {
                var x = 0
                if (id != null) {
                    x = id
                    val response = repository.deleteProductDisplay(x)
                    if (response.isSuccessful) {
                        val res = response.body()
                        _stateDeleteProductDisplay.value = _stateDeleteProductDisplay.value.copy(
                            isLoading = false,
                            success = res
                        )
                        _stateProductDisplay.value = _stateProductDisplay.value.copy(
                            displayProduct = null
                        )
                    } else {
                        _stateDeleteProductDisplay.value = _stateDeleteProductDisplay.value.copy(
                            error = "NotFound"
                        )
                    }
                }
            } catch (e: Exception) {
                _stateDeleteProductDisplay.value = _stateDeleteProductDisplay.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }

    fun inicijalnoStanjeNewPD() {
        _stateNewProductDisplay.value = _stateNewProductDisplay.value.copy(
            isLoading = true,
            newProductDisplay = null,
            error = ""
        )
    }

    fun newProductDisplay(productDisplay: NewProductDisplayDTO) {
        viewModelScope.launch {
            try {
                val response = repository.newProductDisplay(productDisplay)

                if (response.isSuccessful) {
                    val res = response.body()
                    _stateNewProductDisplay.value = _stateNewProductDisplay.value.copy(
                        isLoading = false,
                        newProductDisplay = res
                    )
                } else {
                    _stateNewProductDisplay.value = _stateNewProductDisplay.value.copy(
                        error = "NotFound",
                        newProductDisplay = null,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _stateNewProductDisplay.value = _stateNewProductDisplay.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }

    fun deleteShop(shopId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.deleteShop(shopId)
                if (response.isSuccessful) {
                    val shopp = response.body()
                    _stateDeleteShop.value = _stateDeleteShop.value.copy(
                        isLoading = false,
                        success = shopp,
                        error=""
                    )
                } else {
                    _stateDeleteShop.value = _stateDeleteShop.value.copy(
                        isLoading = false,
                        success = null,
                        error = "You cannot delete the store now! Try it later."
                    )
                }
            } catch (e: Exception) {
                _stateDeleteShop.value = _stateDeleteShop.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }

    fun uploadImage(type: Int, id: Int, imagePart: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val response = repository.uploadImage2(type, id, imagePart)

                if (response.isSuccessful) {
                    _stateimage.value = _stateimage.value.copy(
                        isLoading = false,
                        image = response.body(),
                        error = "CantAdd"
                    )
                } else {
                    _stateimage.value = _stateimage.value.copy(
                        isLoading = false,
                        image = null,
                        error = ""
                    )
                }
            } catch (e: Exception) {
                _stateimage.value = _stateimage.value.copy(
                    error = e.message.toString()
                )
            }
        }
    }
}