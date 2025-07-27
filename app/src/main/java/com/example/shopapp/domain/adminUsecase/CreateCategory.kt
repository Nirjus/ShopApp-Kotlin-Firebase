package com.example.shopapp.domain.adminUsecase

import android.content.Context
import android.net.Uri
import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.domain.repo.AdminRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateCategory @Inject constructor(private val adminRepo: AdminRepo) {
    fun createCategories(
        context: Context,
        categoryDataModel: CategoryDataModel,
        imageUri: Uri
    ): Flow<ResultState<String>> {
        return adminRepo.createCategory(context, categoryDataModel, imageUri)
    }
}