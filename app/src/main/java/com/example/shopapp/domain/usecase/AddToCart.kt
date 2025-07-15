package com.example.shopapp.domain.usecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.CartDataModels
import com.example.shopapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToCart @Inject constructor(private val repo: Repo) {
    fun addToCart(cartDataModels: CartDataModels): Flow<ResultState<String>>{
        return repo.addToCart(cartDataModels)
    }
}