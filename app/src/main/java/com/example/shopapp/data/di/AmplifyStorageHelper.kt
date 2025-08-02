package com.example.shopapp.data.di

import android.content.Context
import android.net.Uri
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageAccessLevel
import com.amplifyframework.storage.options.StorageRemoveOptions
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.example.shopapp.common.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import javax.inject.Inject

class AmplifyStorageHelper @Inject constructor(private val context: Context) {

    fun uploadFileFromUri(uri: Uri, key: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            // Create a temporary file from the Uri
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw Exception("Could not open input stream")
            val tempFile = File.createTempFile("upload", null, context.cacheDir)
            tempFile.outputStream().use { fileOut ->
                inputStream.copyTo(fileOut)
            }
            inputStream.close()

            // Upload options with public read access
            val options = StorageUploadFileOptions.builder()
                .accessLevel(StorageAccessLevel.PUBLIC)
                .build()

            var uploadedUrl = ""
            Amplify.Storage.uploadFile(
                key,
                tempFile,
                options,
                { progress -> /* You can emit progress updates here if needed */ },
                { result ->
                    uploadedUrl = Amplify.Storage.getUrl(
                        key,
                        { url -> uploadedUrl = url.toString() },
                        { error -> throw error }
                    ).toString()
                    trySend(ResultState.Success(uploadedUrl))
                },
                { error -> trySend(ResultState.Error(error.message ?: "Upload failed")) }
            )

            // Clean up temp file
            tempFile.delete()
        } catch (e: Exception) {
            trySend(ResultState.Error(e.message ?: "Upload failed"))
        }
        awaitClose { close() }
    }

    fun deleteFile(key: String): Flow<ResultState<Boolean>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            val options = StorageRemoveOptions.builder()
                .accessLevel(StorageAccessLevel.PUBLIC)
                .build()

            Amplify.Storage.remove(
                key,
                options,
                { trySend(ResultState.Success(true)) },
                { error -> trySend(ResultState.Error(error.message ?: "Delete failed")) }
            )
        } catch (e: Exception) {
            trySend(ResultState.Error(e.message ?: "Delete failed"))
        }
        awaitClose { close() }
    }

    fun getFileUrl(key: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            Amplify.Storage.getUrl(
                key,
                { url -> trySend(ResultState.Success(url.toString())) },
                { error -> trySend(ResultState.Error(error.message ?: "Failed to get URL")) }
            )
        } catch (e: Exception) {
            trySend(ResultState.Error(e.message ?: "Failed to get URL"))
        }
        awaitClose { close() }
    }
}
