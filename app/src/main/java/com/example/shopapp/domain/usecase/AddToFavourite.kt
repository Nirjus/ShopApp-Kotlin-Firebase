package com.example.shopapp.domain.usecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToFavourite @Inject constructor(private val repo: Repo) {
    fun addToFavourite(productsDataModel: ProductsDataModel): Flow<ResultState<String>>{
        return repo.addToFavourite(productsDataModel)
    }
}