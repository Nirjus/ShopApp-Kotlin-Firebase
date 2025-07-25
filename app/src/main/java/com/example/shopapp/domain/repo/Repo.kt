package com.example.shopapp.domain.repo

import android.content.Context
import android.net.Uri
import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.BannerDataModels
import com.example.shopapp.domain.models.CartDataModels
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.domain.models.UserData
import com.example.shopapp.domain.models.UserDataParent
import kotlinx.coroutines.flow.Flow

interface Repo {

    fun registerUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>>
    fun loginUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>>
    fun getUserById(userId: String): Flow<ResultState<UserDataParent>>
    fun updateUserData(userDataParent: UserDataParent): Flow<ResultState<String>>
    fun updateUserProfileImage(context: Context, uri: Uri): Flow<ResultState<String>>
    fun getAllCategories(): Flow<ResultState<List<CategoryDataModel>>>
    fun getCategoriesInLimited(): Flow<ResultState<List<CategoryDataModel>>>
    fun getProductsInLimit(): Flow<ResultState<List<ProductsDataModel>>>
    fun getAllProducts(): Flow<ResultState<List<ProductsDataModel>>>
    fun getProductById(productId: String) : Flow<ResultState<ProductsDataModel>>
    fun addToCart(cartDataModel: CartDataModels): Flow<ResultState<String>>
    fun addToFavourite(productsDataModel: ProductsDataModel): Flow<ResultState<String>>
    fun removeFromFavourite(productId: String): Flow<ResultState<String>>
    fun getAllFavourites(): Flow<ResultState<List<ProductsDataModel>>>
    fun getAllSuggestiveProducts(): Flow<ResultState<List<ProductsDataModel>>>
    fun getAllCarts(): Flow<ResultState<List<CartDataModels>>>
    fun getCheckOut(productId: String): Flow<ResultState<ProductsDataModel>>
    fun getBanners(): Flow<ResultState<List<BannerDataModels>>>
    fun getSpecificCategory(categoryName: String): Flow<ResultState<List<ProductsDataModel>>>

}