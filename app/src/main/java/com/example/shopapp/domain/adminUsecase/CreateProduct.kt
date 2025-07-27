package com.example.shopapp.domain.adminUsecase

import android.content.Context
import android.net.Uri
import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.domain.repo.AdminRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateProduct @Inject constructor(private val adminRepo: AdminRepo) {
    fun createProducts(
        context: Context,
        productsDataModel: ProductsDataModel,
        imageUri: Uri
    ): Flow<ResultState<String>> {
        return adminRepo.createProduct(context, productsDataModel, imageUri)
    }
}