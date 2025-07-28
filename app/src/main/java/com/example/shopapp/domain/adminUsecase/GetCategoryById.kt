package com.example.shopapp.domain.adminUsecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.domain.repo.AdminRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryById @Inject constructor(private val adminRepo: AdminRepo) {
    fun getCategoriesById(categoryId: String): Flow<ResultState<CategoryDataModel>> {
        return adminRepo.getCategoryById(categoryId)
    }
}