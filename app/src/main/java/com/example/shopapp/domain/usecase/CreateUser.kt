package com.example.shopapp.domain.usecase

import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.models.UserData
import com.example.shopapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUser @Inject constructor(private val repo: Repo) {

    fun createUser(userData: UserData): Flow<ResultState<String>>{
        return repo.registerUserWithEmailAndPassword(userData)
    }

}