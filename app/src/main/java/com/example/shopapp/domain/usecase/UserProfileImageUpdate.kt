package com.example.shopapp.domain.usecase

import android.net.Uri
import android.content.Context
import com.example.shopapp.common.ResultState
import com.example.shopapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserProfileImageUpdate @Inject constructor(private val repo: Repo) {
    /**
     * This use case is responsible for updating the user profile image.
     * It takes a context and a URI of the image to be uploaded.
     * It returns a Flow of ResultState<String> which indicates the status of the upload operation.
     */

    fun updateUserProfileImages(context: Context, uri: Uri): Flow<ResultState<String>> {
        return repo.updateUserProfileImage(uri = uri, context = context)
    }
}