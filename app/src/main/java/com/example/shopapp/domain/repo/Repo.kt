package com.example.shopapp.domain.repo

import android.net.Uri
import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.domain.models.UserData
import com.example.shopapp.domain.models.UserDataParent
import kotlinx.coroutines.flow.Flow

interface Repo {

    fun registerUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>>
    fun loginUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>>
    fun getUserById(userId: String): Flow<ResultState<String>>
    fun updateUserData(userDataParent: UserDataParent): Flow<ResultState<String>>
    fun userProfileImage(uri: Uri): Flow<ResultState<String>>
    fun getCategoriesInLimited(): Flow<ResultState<List<CategoryDataModel>>>
    fun getProductsInLimit(): Flow<ResultState<List<ProductsDataModel>>>
    fun getAllProducts(): Flow<ResultState<List<ProductsDataModel>>>
    fun getProductById(productId: String) : Flow<ResultState<ProductsDataModel>>
    fun addToCart(cartDataModel: ProductsDataModel): Flow<ResultState<String>>
    fun addToFavourite(productsDataModel: ProductsDataModel): Flow<ResultState<String>>

}