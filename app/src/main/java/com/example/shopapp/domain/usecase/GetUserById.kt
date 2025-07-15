package com.example.shopapp.domain.usecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.UserDataParent
import com.example.shopapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserById @Inject constructor(private val repo: Repo) {

    fun getUserByUserId(userId: String): Flow<ResultState<UserDataParent>> {
        return repo.getUserById(userId)
    }
}
