package com.example.shopapp.domain.usecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategory @Inject constructor(private val repo: Repo) {

    fun getAllCategory(): Flow<ResultState<List<CategoryDataModel>>> {
        return repo.getAllCategories()
    }
}