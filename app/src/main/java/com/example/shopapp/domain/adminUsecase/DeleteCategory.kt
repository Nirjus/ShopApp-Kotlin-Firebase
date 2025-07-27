package com.example.shopapp.domain.adminUsecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.repo.AdminRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteCategory @Inject constructor(private val adminRepo: AdminRepo) {
    fun deleteCategories(categoryId: String): Flow<ResultState<String>> {
        return adminRepo.deleteCategory(categoryId)
    }
}