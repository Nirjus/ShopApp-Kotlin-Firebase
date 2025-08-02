package com.example.shopapp.domain.di

import com.example.shopapp.data.di.AmplifyStorageHelper
import com.example.shopapp.data.repo.AdminRepoImpl
import com.example.shopapp.data.repo.RepoImpl
import com.example.shopapp.domain.repo.AdminRepo
import com.example.shopapp.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    fun provideRepo(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore, awsHelper: AmplifyStorageHelper): Repo{
        return RepoImpl(firebaseAuth, firebaseFirestore, awsHelper)
    }
    @Provides
    fun provideAdminRepo(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore, awsHelper: AmplifyStorageHelper): AdminRepo{
        return AdminRepoImpl(firebaseAuth, firebaseFirestore,awsHelper)
    }
}