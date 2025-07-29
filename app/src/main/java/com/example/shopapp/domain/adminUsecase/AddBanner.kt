package com.example.shopapp.domain.adminUsecase

import android.content.Context
import android.net.Uri
import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.BannerDataModels
import com.example.shopapp.domain.repo.AdminRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddBanner @Inject constructor(private val adminRepo: AdminRepo) {
    fun addBanners(context: Context, bannerDataModels: BannerDataModels, imageUri: Uri): Flow<ResultState<String>> {
        return adminRepo.addBanner(context, bannerDataModels, imageUri)
    }
}