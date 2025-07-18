package com.example.shopapp.data.repo

import android.net.Uri
import com.example.shopapp.common.ResultState
import com.example.shopapp.common.USER_COLLECTION
import com.example.shopapp.data.di.AWSHelper
import com.example.shopapp.domain.models.BannerDataModels
import com.example.shopapp.domain.models.CartDataModels
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.domain.models.UserData
import com.example.shopapp.domain.models.UserDataParent
import com.example.shopapp.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepoImpl @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var firebaseFirestore: FirebaseFirestore,
    private val awsHelper: AWSHelper
) : Repo {
    override fun registerUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseAuth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        firebaseFirestore.collection(USER_COLLECTION)
                            .document(it.result.user?.uid.toString()).set(userData)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    trySend(ResultState.Success("User Registered successfully and add toFirestore"))
                                } else {
                                    if (it.exception != null) {
                                        trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                                    }
                                }
                            }
                        trySend(ResultState.Success("User Registered successfully "))
                    } else {
                        if (it.exception != null) {
                            trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                        }
                    }
                }
            awaitClose { close() }
        }

    override fun loginUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseAuth.signInWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success("User login Successfully"))
                    } else {
                        if (it.exception != null) {
                            trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                        }
                    }
                }
            awaitClose { close() }
        }

    override fun getUserById(userId: String): Flow<ResultState<UserDataParent>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(USER_COLLECTION).document(userId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result.toObject(UserData::class.java)!!
                val userDataParent = UserDataParent(it.result.id, data)
                trySend(ResultState.Success(userDataParent))
            } else {
                if (it.exception != null) {
                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                }
            }
        }
        awaitClose { close() }
    }

    override fun updateUserData(userDataParent: UserDataParent): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFirestore.collection(USER_COLLECTION).document(userDataParent.nodeId)
                .update(userDataParent.userData.toMap()).addOnCompleteListener {
                    if(it.isSuccessful){
                        trySend(ResultState.Success("User data update successfully"))
                    }else{
                        trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                    }
                }
            awaitClose { close() }
        }

    override fun updateUserProfileImage(uri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            val userId = firebaseAuth.currentUser?.uid ?: throw Exception("User not authenticated")
            val objectKey = "profile_images/$userId/${System.currentTimeMillis()}"

            uploadFileToS3(uri.path!!, objectKey).collect { result ->
                when(result) {
                    is ResultState.Success -> {
                        // Update user profile with S3 URL
                        firebaseFirestore.collection(USER_COLLECTION)
                            .document(userId)
                            .update("profileImage", result.data)
                            .addOnSuccessListener {
                                trySend(ResultState.Success(result.data))
                            }
                            .addOnFailureListener { e ->
                                trySend(ResultState.Error(e.message ?: "Failed to update profile"))
                            }
                    }
                    is ResultState.Error -> trySend(ResultState.Error(result.message))
                    is ResultState.Loading -> trySend(ResultState.Loading)
                }
            }
        } catch (e: Exception) {
            trySend(ResultState.Error(e.message ?: "Failed to update profile image"))
        }
        awaitClose { }
    }

    override fun getAllCategories(): Flow<ResultState<List<CategoryDataModel>>> {
        TODO("Not yet implemented")
    }

    override fun getCategoriesInLimited(): Flow<ResultState<List<CategoryDataModel>>> {
        TODO("Not yet implemented")
    }

    override fun getProductsInLimit(): Flow<ResultState<List<ProductsDataModel>>> {
        TODO("Not yet implemented")
    }

    override fun getAllProducts(): Flow<ResultState<List<ProductsDataModel>>> {
        TODO("Not yet implemented")
    }

    override fun getProductById(productId: String): Flow<ResultState<ProductsDataModel>> {
        TODO("Not yet implemented")
    }

    override fun addToCart(cartDataModel: CartDataModels): Flow<ResultState<String>> {
        TODO("Not yet implemented")
    }

    override fun addToFavourite(productsDataModel: ProductsDataModel): Flow<ResultState<String>> {
        TODO("Not yet implemented")
    }

    override fun getAllFavourites(): Flow<ResultState<List<ProductsDataModel>>> {
        TODO("Not yet implemented")
    }

    override fun getAllSuggestiveProducts(): Flow<ResultState<List<ProductsDataModel>>> {
        TODO("Not yet implemented")
    }

    override fun getAllCarts(): Flow<ResultState<List<CartDataModels>>> {
        TODO("Not yet implemented")
    }

    override fun getCheckOut(productId: String): Flow<ResultState<ProductsDataModel>> {
        TODO("Not yet implemented")
    }

    override fun getBanners(): Flow<ResultState<List<BannerDataModels>>> {
        TODO("Not yet implemented")
    }

    override fun getSpecificCategory(categoryName: String): Flow<ResultState<List<CategoryDataModel>>> {
        TODO("Not yet implemented")
    }

    suspend fun initializeAWS() {
        awsHelper.initAWS()
    }

    fun uploadFileToS3(filePath: String, objectKey: String): Flow<ResultState<String>> {
        return awsHelper.uploadFile(filePath, objectKey)
    }

    fun deleteFileFromS3(objectKey: String): Flow<ResultState<Boolean>> {
        return awsHelper.deleteFile(objectKey)
    }

    fun getS3FileUrl(objectKey: String): String {
        return awsHelper.getFileUrl(objectKey)
    }
}