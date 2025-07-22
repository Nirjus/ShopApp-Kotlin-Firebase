package com.example.shopapp

import android.app.Application
import android.util.Log
import com.example.shopapp.data.repo.RepoImpl
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

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("BaseApplication", "Coroutine exception: ${throwable.message}", throwable)
    }

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)

    override fun onCreate() {
        super.onCreate()
        Log.d("BaseApplication", "onCreate called")
        try {
            initializeAWS()
            Log.d("BaseApplication", "initializeAWS called")
        } catch (e: Exception) {
            Log.e("BaseApplication", "Exception in onCreate: ${e.message}", e)
        }
    }

    private fun initializeAWS() {
        applicationScope.launch {
            Log.d("BaseApplication", "Starting AWS initialization")
            try {
                repoImpl.initializeAWS()
                Log.d("BaseApplication", "AWS initialized successfully")
            } catch (e: Exception) {
                Log.e("BaseApplication", "Failed to initialize AWS: ${e.message}", e)
            }
        }
    }
}