package com.example.shopapp.domain.usecase

import android.net.Uri
import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserProfileImageUpdate @Inject constructor(private val repo: Repo) {
    fun updateUserProfileImage(uri: Uri): Flow<ResultState<String>> {
        return repo.updateUserProfileImage(uri)
    }
}