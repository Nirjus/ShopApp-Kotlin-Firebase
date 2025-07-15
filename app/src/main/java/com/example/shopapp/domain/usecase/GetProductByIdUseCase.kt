package com.example.shopapp.domain.usecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(private val repo: Repo) {

    fun getProductByID(productId: String): Flow<ResultState<ProductsDataModel>>{
            return repo.getProductById(productId)
    }
}