package com.example.shopapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.example.shopapp.common.HomeScreenState
import com.example.shopapp.domain.models.BannerDataModels
import com.example.shopapp.domain.models.CartDataModels
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.domain.models.UserDataParent
import com.example.shopapp.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class ShoppingAppViewModel @Inject constructor(
    private val createUserUseCase: CreateUser,
    private val loginUserUseCase: LoginUser,
    private val gtUserUseCase: GetUserById,
    private val getAllFavourite: GetAllFavourite,
    private val getAllProductsUseCase: GetAllProducts,
    private val getSpecificCategoryItemUseCase: GetSpesificUsecaseItem,
    private val getAllCategoryUseCase: GetAllCategory,
    private val getCheckoutUseCase: GetCheckouts,
    private val addToCartUseCase: AddToCart,
    private val addToFavUseCase: AddToFavourite,
    private val uploadUserProfileImageUseCase: UserProfileImageUpdate,
    private val getAllSuggestedProductsUseCase: GetAllSuggestiveProducts,
    private val getProductByIdUseCase : GetProductByIdUseCase,
    private val updateUserUseCase: UpdateUserData,
    private val getAllCartUseCase: GetCarts,
    private val getBannerUseCase: GetBannerUseCase ,
    private val getProductInLimitUseCase: GetProductInLimits,
    private val getCategoryInLimitUseCases: GetCategoryInLimit
): ViewModel() {
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
    private val _getAllFavourite = MutableStateFlow(GetAllFavourite())
    val getAllFavouriteState = _getAllFavourite.asStateFlow()
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
    val userData: String? = null
)

data class AddToFavState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)

data class GetAllFavourite(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<ProductsDataModel?> = emptyList()
)

data class GetAllProductsState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: List<CategoryDataModel?> = emptyList()
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