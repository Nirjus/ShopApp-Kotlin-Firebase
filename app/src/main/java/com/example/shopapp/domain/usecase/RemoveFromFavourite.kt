package com.example.shopapp.domain.usecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveFromFavourite @Inject constructor(private val repo: Repo) {
    fun removeFromFavourites(productId: String): Flow<ResultState<String>>{
        return repo.removeFromFavourite(productId)
    }
}