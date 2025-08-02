package com.example.shopapp

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.example.shopapp.data.repo.RepoImpl
import com.example.shopapp.domain.repo.AdminRepo
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {
    @Inject
    lateinit var repoImpl: RepoImpl
//
    @Inject
    lateinit var adminRepo: AdminRepo

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("BaseApplication", "Coroutine exception: ${throwable.message}", throwable)
    }

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)

    override fun onCreate() {
        super.onCreate()
        Log.d("BaseApplication", "onCreate called")
        try {
            initializeAmplify()
            Log.d("BaseApplication", "initializeAWS called")
        } catch (e: Exception) {
            Log.e("BaseApplication", "Exception in onCreate: ${e.message}", e)
        }
    }

    private fun initializeAmplify() {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(applicationContext)
        } catch (error: AmplifyException) {
            Log.e("BaseApplication", "Could not initialize Amplify", error)
        } catch (e: Exception) {
            // Catch any other unexpected error during plugin addition or configuration
            Log.e("BaseApplication", "Unexpected error during Amplify initialization: ${e.message}", e)
        }
    }
}