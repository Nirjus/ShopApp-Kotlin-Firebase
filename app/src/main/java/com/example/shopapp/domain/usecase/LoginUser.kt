package com.example.shopapp.domain.usecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.UserData
import com.example.shopapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUser @Inject constructor(private val repo: Repo) {

    fun loginUsers(userData: UserData): Flow<ResultState<String>> {
        return repo.loginUserWithEmailAndPassword(userData)
    }
}