package com.example.shopapp.domain.usecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.BannerDataModels
import com.example.shopapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBannerUseCase @Inject constructor(private val repo: Repo) {

    fun getBannerUseCase() : Flow<ResultState<List<BannerDataModels>>>{
        return repo.getBanners()
    }
}