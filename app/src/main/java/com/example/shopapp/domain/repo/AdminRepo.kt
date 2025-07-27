package com.example.shopapp.domain.repo

import android.content.Context
import android.net.Uri
import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.domain.models.ProductsDataModel
import kotlinx.coroutines.flow.Flow

interface AdminRepo {

    fun createProduct(
        context: Context,
        productDataModels: ProductsDataModel,
        imageUri: Uri
    ): Flow<ResultState<String>>

    fun updateProduct(productDataModels: ProductsDataModel): Flow<ResultState<String>>
    fun deleteProduct(productId: String): Flow<ResultState<String>>
    fun createCategory(
        context: Context,
        categoryDataModel: CategoryDataModel,
        imageUri: Uri
    ): Flow<ResultState<String>>

    fun updateCategory(categoryDataModel: CategoryDataModel): Flow<ResultState<String>>
    fun deleteCategory(categoryId: String): Flow<ResultState<String>>

}