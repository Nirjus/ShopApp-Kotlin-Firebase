package com.example.shopapp.domain.adminUsecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.domain.repo.AdminRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateCategory @Inject constructor(private val adminRepo: AdminRepo) {
    fun updateCategories(categoryDataModel: CategoryDataModel): Flow<ResultState<String>> {
        return adminRepo.updateCategory(categoryDataModel)
    }
}