package com.example.shopapp.domain.adminUsecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.domain.repo.AdminRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductById @Inject constructor(private val adminRepo: AdminRepo) {
    fun getProductsById(productId: String): Flow<ResultState<ProductsDataModel>> {
        return adminRepo.getProductById(productId)
    }
}