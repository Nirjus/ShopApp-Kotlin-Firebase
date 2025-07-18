package com.example.shopapp

import android.app.Application
import android.util.Log
import com.example.shopapp.data.repo.RepoImpl
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {
    @Inject
    lateinit var repoImpl: RepoImpl

    override fun onCreate() {
        super.onCreate()
        initializeAWS()
    }

    private fun initializeAWS() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repoImpl.initializeAWS()
            } catch (e: Exception) {
                Log.e("AWS", "Failed to initialize AWS: ${e.message}")
            }
        }
    }
}