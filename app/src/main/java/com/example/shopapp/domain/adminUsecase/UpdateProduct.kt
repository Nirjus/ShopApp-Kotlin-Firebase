package com.example.shopapp.domain.adminUsecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.domain.repo.AdminRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateProduct @Inject constructor(private val adminRepo: AdminRepo) {
    fun updateProducts(productDataModels: ProductsDataModel): Flow<ResultState<String>> {
        return adminRepo.updateProduct(productDataModels)
    }
}