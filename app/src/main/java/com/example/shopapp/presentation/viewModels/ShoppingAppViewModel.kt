package com.example.shopapp.presentation.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.common.HomeScreenState
import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.BannerDataModels
import com.example.shopapp.domain.models.CartDataModels
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.domain.models.UserData
import com.example.shopapp.domain.models.UserDataParent
import com.example.shopapp.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject


class ShoppingAppViewModel @Inject constructor(
    private val createUserUseCase: CreateUser,
    private val loginUserUseCase: LoginUser,
    private val gtUserUseCase: GetUserById,
    private val getAllFavouriteUseCase: GetAllFavourite,
    private val getAllProductsUseCase: GetAllProducts,
    private val getSpecificCategoryItemUseCase: GetSpesificUsecaseItem,
    private val getAllCategoryUseCase: GetAllCategory,
    private val getCheckoutUseCase: GetCheckouts,
    private val addToCartUseCase: AddToCart,
    private val addToFavUseCase: AddToFavourite,
    private val uploadUserProfileImageUseCase: UserProfileImageUpdate,
    private val getAllSuggestedProductsUseCase: GetAllSuggestiveProducts,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val updateUserUseCase: UpdateUserData,
    private val getAllCartUseCase: GetCarts,
    private val getBannerUseCase: GetBannerUseCase,
    private val getProductInLimitUseCase: GetProductInLimits,
    private val getCategoryInLimitUseCases: GetCategoryInLimit
) : ViewModel() {
    private val _signUpScreenState = MutableStateFlow(SignUpScreenState())
    val signUpScreenState = _signUpScreenState.asStateFlow()
    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val loginScreenState = _loginScreenState.asStateFlow()
    private val _profileScreenState = MutableStateFlow(ProfileScreenState())
    val profileScreenState = _profileScreenState.asStateFlow()
    private val _updateScreenState = MutableStateFlow(UpdateScreenState())
    val updateScreenState = _updateScreenState.asStateFlow()
    private val _uploadUserProfileImageState = MutableStateFlow(UploadUserProfileImageState())
    val uploadUserProfileImageState = _uploadUserProfileImageState.asStateFlow()
    private val _addToCartState = MutableStateFlow(AddToCartState())
    val addToCartState = _addToCartState.asStateFlow()
    private val _getProductByIdState = MutableStateFlow(GetProductByIdState())
    val getProductByIdState = _getProductByIdState.asStateFlow()
    private val _addToFavState = MutableStateFlow(AddToFavState())
    val addToFavState = _addToFavState.asStateFlow()
    private val _getAllFavouriteState = MutableStateFlow(GetAllFavouriteState())
    val getAllFavouriteState = _getAllFavouriteState.asStateFlow()
    private val _getAllProductsState = MutableStateFlow(GetAllProductsState())
    val getAllProductsState = _getAllProductsState.asStateFlow()
    private val _getCartState = MutableStateFlow(GetCartState())
    val getCartState = _getCartState.asStateFlow()
    private val _getAllCategoryState = MutableStateFlow(GetAllCategoryState())
    val getAllCategoryState = _getAllCategoryState.asStateFlow()
    private val _getCheckoutState = MutableStateFlow(GetCheckoutState())
    val getCheckoutState = _getCheckoutState.asStateFlow()
    private val _getSpecificCategoryItemState = MutableStateFlow(GetSpecificCategoryItemState())
    val getSpecificCategoryItemState = _getSpecificCategoryItemState.asStateFlow()
    private val _getAllSuggestedProductsState = MutableStateFlow(GetAllSuggestedProductsState())
    val getAllSuggestedProductsState = _getAllSuggestedProductsState.asStateFlow()
    private val _getBannerState = MutableStateFlow(GetBannersState())
    val getBannerState = _getBannerState.asStateFlow()
    private val _getProductInLimitState = MutableStateFlow(GetAllProductsState())
    val getProductInLimitState = _getProductInLimitState.asStateFlow()
    private val _getCategoryInLimitState = MutableStateFlow(GetAllCategoryState())
    val getCategoryInLimitState = _getCategoryInLimitState.asStateFlow()
    private val _getAllCartState = MutableStateFlow(GetCartState())
    val getAllCartState = _getAllCartState.asStateFlow()
    private val _getUserDataState = MutableStateFlow(UserDataParent())
    val getUserDataState = _getUserDataState.asStateFlow()
    private val _updateUserDataState = MutableStateFlow(UserDataParent())
    val updateUserDataState = _updateUserDataState.asStateFlow()
    private val _getUserByIdState = MutableStateFlow(UserDataParent())
    val getUserByIdState = _getUserByIdState.asStateFlow()
    private val _getAllCategoryInLimitState = MutableStateFlow(GetAllCategoryState())
    val getAllCategoryInLimitState = _getAllCategoryInLimitState.asStateFlow()
    private val _getAllProductsInLimitState = MutableStateFlow(GetAllProductsState())
    val getAllProductsInLimitState = _getAllProductsInLimitState.asStateFlow()
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    fun getSpecificCategoryItems(categoryName: String) {
        viewModelScope.launch {
            getSpecificCategoryItemUseCase.getSpesificCategoryItem(categoryName).collect {

                when (it) {
                    is ResultState.Error -> {
                        _getSpecificCategoryItemState.value =
                            _getSpecificCategoryItemState.value.copy(
                                isLoading = false,
                                errorMessage = it.message
                            )
                    }

                    is ResultState.Loading -> {
                        _getSpecificCategoryItemState.value =
                            _getSpecificCategoryItemState.value.copy(
                                isLoading = true,
                            )
                    }

                    is ResultState.Success -> {
                        _getSpecificCategoryItemState.value =
                            _getSpecificCategoryItemState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                    }
                }
            }
        }
    }

    fun getCheckOut(productID: String) {
        viewModelScope.launch {
            getCheckoutUseCase.getCheckouts(productID).collect {
                when (it) {

                    is ResultState.Error -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun getAllCategories() {
        viewModelScope.launch {
            getAllCategoryUseCase.getAllCategory().collect {

                when (it) {
                    is ResultState.Error -> {
                        _getAllCategoryState.value =
                            _getAllCategoryState.value.copy(
                                isLoading = false,
                                errorMessage = it.message
                            )
                    }

                    is ResultState.Loading -> {
                        _getAllCategoryState.value =
                            _getAllCategoryState.value.copy(
                                isLoading = true,
                            )
                    }

                    is ResultState.Success -> {
                        _getAllCategoryState.value =
                            _getAllCategoryState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                    }
                }
            }
        }
    }

    fun getCart() {
        viewModelScope.launch {
            getAllCartUseCase.getAllCarts().collect {
                when (it) {
                    is ResultState.Error -> {
                        _getCartState.value = _getCartState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _getCartState.value = _getCartState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getCartState.value = _getCartState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            getAllProductsUseCase.getAllProduct().collect {
                when (it) {
                    is ResultState.Error -> {
                        _getAllProductsState.value = _getAllProductsState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _getAllProductsState.value = _getAllProductsState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getAllProductsState.value = _getAllProductsState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun getAllFavouriteProducts() {

        viewModelScope.launch {

            getAllFavouriteUseCase.getAllFavourite().collect {
                when (it) {

                    is ResultState.Error -> {
                        _getAllFavouriteState.value = _getAllFavouriteState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _getAllFavouriteState.value = _getAllFavouriteState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getAllFavouriteState.value = _getAllFavouriteState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun addToFavourite(productsDataModel: ProductsDataModel) {

        viewModelScope.launch {

            addToFavUseCase.addToFavourite(productsDataModel).collect {
                when (it) {
                    is ResultState.Error -> {
                        _addToFavState.value = _addToFavState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _addToFavState.value = _addToFavState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _addToFavState.value = _addToFavState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun getProductById(productId: String) {
        viewModelScope.launch {
            getProductByIdUseCase.getProductByID(productId).collect {
                when (it) {
                    is ResultState.Error -> {
                        _getProductByIdState.value = _getProductByIdState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _getProductByIdState.value = _getProductByIdState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getProductByIdState.value = _getProductByIdState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun addToCart(cartDataModels: CartDataModels) {
        viewModelScope.launch {
            addToCartUseCase.addToCart(cartDataModels).collect {
                when (it) {
                    is ResultState.Error -> {
                        _addToCartState.value = _addToCartState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _addToCartState.value = _addToCartState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _addToCartState.value = _addToCartState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }
    init {
        loadHomeScreenData()
    }
    fun loadHomeScreenData() {
        viewModelScope.launch {
            combine(
                getCategoryInLimitUseCases.getCategoryInLimit(),
                getProductInLimitUseCase.getProductInLimit(),
                getBannerUseCase.getBannerUseCase()
            ){ categoriesResult, productResult, bannerResult ->
                    when{
                        categoriesResult is ResultState.Error -> {
                            HomeScreenState(isLoading = false, errorMessage = categoriesResult.message)
                        }
                        productResult is ResultState.Error -> {
                            HomeScreenState(isLoading = false, errorMessage = productResult.message)
                        }
                        bannerResult is ResultState.Error -> {
                            HomeScreenState(isLoading = false, errorMessage = bannerResult.message)
                        }
                        categoriesResult is ResultState.Success && productResult is ResultState.Success && bannerResult is ResultState.Success -> {
                            HomeScreenState(
                                isLoading = false,
                                categories = categoriesResult.data,
                                products = productResult.data,
                                banners = bannerResult.data
                            )
                        }
                        else -> {
                            HomeScreenState(isLoading = true)
                        }
                    }
            }.collect {
                state -> _homeScreenState.value = state
            }
        }
    }

    fun uploadUserProfileIMage(uri: Uri){
        viewModelScope.launch {
            uploadUserProfileImageUseCase.updateUserProfileImage(uri).collect {
                when(it){
                    is ResultState.Error -> {
                        _uploadUserProfileImageState.value = _uploadUserProfileImageState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }
                    is ResultState.Loading -> {
                        _uploadUserProfileImageState.value = _uploadUserProfileImageState.value.copy(
                            isLoading = true,
                        )
                    }
                    is ResultState.Success -> {
                        _uploadUserProfileImageState.value = _uploadUserProfileImageState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun upDateUserData(userDataParent: UserDataParent){
        viewModelScope.launch {
            updateUserUseCase.updateUserData(userDataParent).collect {
                when(it){
                    is ResultState.Error -> {
                        _updateScreenState.value = _updateScreenState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }
                    is ResultState.Loading -> {
                        _updateScreenState.value = _updateScreenState.value.copy(
                            isLoading = true,
                        )
                    }
                    is ResultState.Success -> {
                        _updateScreenState.value = _updateScreenState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun createUser(userData: UserData){
        viewModelScope.launch {
            createUserUseCase.createUser(userData).collect {
                when(it){
                    is ResultState.Error -> {
                        _signUpScreenState.value = _signUpScreenState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }
                    is ResultState.Loading -> {
                        _signUpScreenState.value = _signUpScreenState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState.Success -> {
                        _signUpScreenState.value = _signUpScreenState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun loginUser(userData: UserData){
        viewModelScope.launch {
            loginUserUseCase.loginUsers(userData).collect {
                when(it){
                    is ResultState.Error -> {
                        _loginScreenState.value = _loginScreenState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }
                    is ResultState.Loading -> {
                        _loginScreenState.value = _loginScreenState.value.copy(
                            isLoading = true
                        )
                    }
                    is ResultState.Success -> {
                        _loginScreenState.value = _loginScreenState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

}

data class ProfileScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: UserDataParent? = null
)

data class SignUpScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)

data class LoginScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)

data class UpdateScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)

data class UploadUserProfileImageState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)

data class AddToCartState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)

data class GetProductByIdState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: ProductsDataModel? = null
)

data class AddToFavState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)

data class GetAllFavouriteState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<ProductsDataModel?> = emptyList()
)

data class GetAllProductsState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<ProductsDataModel?> = emptyList()
)

data class GetCartState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<CartDataModels?> = emptyList()
)

data class GetAllCategoryState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<CategoryDataModel?> = emptyList()
)

data class GetCheckoutState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: ProductsDataModel? = null
)

data class GetSpecificCategoryItemState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<ProductsDataModel?> = emptyList()
)

data class GetAllSuggestedProductsState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<ProductsDataModel?> = emptyList()
)

data class GetBannersState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<BannerDataModels?> = emptyList()
)