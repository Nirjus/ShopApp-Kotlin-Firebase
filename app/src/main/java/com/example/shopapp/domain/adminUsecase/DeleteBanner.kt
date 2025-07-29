package com.example.shopapp.domain.adminUsecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.repo.AdminRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteBanner @Inject constructor(private val adminRepo: AdminRepo) {
    fun deleteBanners(bannerId: String): Flow<ResultState<String>> {
      return adminRepo.deleteBannerById(bannerId)
    }
}