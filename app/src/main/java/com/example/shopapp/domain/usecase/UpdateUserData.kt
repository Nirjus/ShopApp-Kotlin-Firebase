package com.example.shopapp.domain.usecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.UserDataParent
import com.example.shopapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateUserData @Inject constructor(private val repo: Repo) {

    fun updateUserData(userDataParent: UserDataParent): Flow<ResultState<String>> {
        return repo.updateUserData(userDataParent)
    }
}