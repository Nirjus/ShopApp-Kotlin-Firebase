package com.example.shopapp.data.repo

import android.content.Context
import android.net.Uri
import com.example.shopapp.common.BANNER_COLLECTION
import com.example.shopapp.common.CATEGORY_COLLECTION
import com.example.shopapp.common.PRODUCT_COLLECTION
import com.example.shopapp.common.ResultState
import com.example.shopapp.data.di.AmplifyStorageHelper
import com.example.shopapp.domain.models.BannerDataModels
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.domain.repo.AdminRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AdminRepoImpl @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var firebaseFirestore: FirebaseFirestore,
    private val amplifyStorageHelper: AmplifyStorageHelper
): AdminRepo {
    override fun createProduct(context: Context, productDataModels: ProductsDataModel, imageUri: Uri): Flow<ResultState<String>> =
        callbackFlow{
            trySend(ResultState.Loading)
            try {
                val objectKey = "product_images/${System.currentTimeMillis()}"
                amplifyStorageHelper.uploadFileFromUri(imageUri, objectKey).collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            val imageUrl = result.data
                            val finalProduct = productDataModels.copy(image = imageUrl) // Assuming 'image' field
                            firebaseFirestore.collection(PRODUCT_COLLECTION)
                                .add(finalProduct)
                                .addOnSuccessListener { documentReference ->
                                    val productId = documentReference.id
                                    documentReference.update("productId", productId).addOnSuccessListener {
                                        trySend(ResultState.Success("Product added successfully"))
                                    }.addOnFailureListener {
                                        trySend(ResultState.Error("Failed to update product id"))
                                    }
                                }
                                .addOnFailureListener {
                                    trySend(ResultState.Error(it.message ?: "Failed to add product"))
                                }
                        }
                        is ResultState.Error -> trySend(ResultState.Error(result.message))
                        is ResultState.Loading -> trySend(ResultState.Loading)
                    }
                }
            } catch (e: Exception) {
                trySend(ResultState.Error(e.message ?: "Unexpected error"))
            }

            awaitClose { close() }
        }

    override fun updateProduct(productDataModels: ProductsDataModel): Flow<ResultState<String>> = callbackFlow {
        TODO("Not yet implemented")
    }

    override fun deleteProduct(productId: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val productDocRef = firebaseFirestore.collection(PRODUCT_COLLECTION).document(productId)
        productDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val product = documentSnapshot.toObject(ProductsDataModel::class.java)
                    product?.image?.let { imageUrl ->
                        // Extract object key from URL or use a stored key if available
                        val objectKey = "product_images/${imageUrl.substringAfterLast("/")}" // This is a basic way, might need adjustment
                        launch { // Launch a coroutine to collect the flow
                            amplifyStorageHelper.deleteFile(objectKey).collectLatest { deleteResult ->
                                when (deleteResult) {
                                    is ResultState.Success -> {
                                        // Image deleted successfully, now delete product document
                                        productDocRef.delete()
                                            .addOnSuccessListener {
                                                trySend(ResultState.Success("Product and image deleted successfully"))
                                            }
                                            .addOnFailureListener { e ->
                                                trySend(ResultState.Error(e.message ?: "Failed to delete product document"))
                                            }
                                    }
                                    is ResultState.Error -> trySend(ResultState.Error(deleteResult.message))
                                    is ResultState.Loading -> trySend(ResultState.Loading) // Or handle appropriately
                                }
                            }
                        }
                    } ?: productDocRef.delete() // If no image URL, just delete the document
                        .addOnSuccessListener { trySend(ResultState.Success("Product deleted successfully (no image to delete)")) }
                        .addOnFailureListener { e -> trySend(ResultState.Error(e.message ?: "Delete failed")) }
                } else {
                    trySend(ResultState.Error("Product not found"))
                }
            }
        awaitClose { close() }
    }

    override fun createCategory(context: Context, categoryDataModel: CategoryDataModel, imageUri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            val objectKey = "category_images/${System.currentTimeMillis()}"
            amplifyStorageHelper.uploadFileFromUri(imageUri, objectKey).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        val imageUrl = result.data
                        val finalCategory = categoryDataModel.copy(image = imageUrl) // Assuming 'image' field
                        firebaseFirestore.collection(CATEGORY_COLLECTION)
                            .add(finalCategory)
                            .addOnSuccessListener { documentReference ->
                                val categoryId = documentReference.id
                                documentReference.update("categoryId", categoryId).addOnSuccessListener {
                                    trySend(ResultState.Success("Category added successfully"))
                                }.addOnFailureListener {
                                    trySend(ResultState.Error("Failed to update category id"))
                                }
                            }
                            .addOnFailureListener {
                                trySend(ResultState.Error(it.message ?: "Upload failed"))
                            }
                    }
                    is ResultState.Error -> trySend(ResultState.Error(result.message))
                    is ResultState.Loading -> trySend(ResultState.Loading)
                }
            }
        }catch (e: Exception){
            trySend(ResultState.Error(e.message ?: "Unexpected error"))
        }
        awaitClose { close() }
    }

    override fun updateCategory(categoryDataModel: CategoryDataModel): Flow<ResultState<String>> {
        TODO("Not yet implemented")
    }

    override fun deleteCategory(categoryId: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val categoryDocRef = firebaseFirestore.collection(CATEGORY_COLLECTION).document(categoryId)
        categoryDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val category = documentSnapshot.toObject(CategoryDataModel::class.java)
                    category?.image?.let { imageUrl ->
                        // Extract object key from URL or use a stored key if available
                        val objectKey = imageUrl.substringAfterLast("/") // This is a basic way, might need adjustment
                        launch { // Launch a coroutine to collect the flow
                            amplifyStorageHelper.deleteFile(objectKey).collectLatest { deleteResult ->
                                when (deleteResult) {
                                    is ResultState.Success -> {
                                        // Image deleted successfully, now delete category document
                                        categoryDocRef.delete()
                                            .addOnSuccessListener {
                                                trySend(ResultState.Success("Category and image deleted successfully"))
                                            }
                                            .addOnFailureListener { e ->
                                                trySend(ResultState.Error(e.message ?: "Failed to delete category document"))
                                            }
                                    }
                                    is ResultState.Error -> trySend(ResultState.Error(deleteResult.message))
                                    is ResultState.Loading -> trySend(ResultState.Loading) // Or handle appropriately
                                }
                            }
                        }
                    } ?: categoryDocRef.delete() // If no image URL, just delete the document
                        .addOnSuccessListener { trySend(ResultState.Success("Category deleted successfully (no image to delete)")) }
                        .addOnFailureListener { e -> trySend(ResultState.Error(e.message ?: "Delete failed")) }
                } else {
                    trySend(ResultState.Error("Category not found"))
                }
            }
        awaitClose { close() }
    }

    override fun getProductById(productId: String): Flow<ResultState<ProductsDataModel>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(PRODUCT_COLLECTION).document(productId).get()
            .addOnSuccessListener {
                val product = it.toObject(ProductsDataModel::class.java)
                trySend(ResultState.Success(product!!))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }
        awaitClose { close() }
    }

    override fun getCategoryById(categoryId: String): Flow<ResultState<CategoryDataModel>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(CATEGORY_COLLECTION).document(categoryId).get()
            .addOnSuccessListener {
                val category = it.toObject(CategoryDataModel::class.java)
                trySend(ResultState.Success(category!!))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }
        awaitClose { close() }
    }

    override fun addBanner(context: Context, bannerDataModels: BannerDataModels, imageUri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        try{
            val objectKey = "banner_images/${System.currentTimeMillis()}"
            amplifyStorageHelper.uploadFileFromUri(imageUri, objectKey).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        val imageUrl = result.data
                        val finalBanner = bannerDataModels.copy(image = imageUrl) // Assuming 'image' field
                        firebaseFirestore.collection(BANNER_COLLECTION)
                            .add(finalBanner)
                            .addOnSuccessListener { documentReference ->
                                val bannerId = documentReference.id
                                documentReference.update("bannerId", bannerId).addOnSuccessListener {
                                    trySend(ResultState.Success("Banner added successfully"))
                                    }.addOnFailureListener {
                                        trySend(ResultState.Error("Failed to update banner id"))
                                    }
                            }
                            .addOnFailureListener {
                                trySend(ResultState.Error(it.message ?: "Upload failed"))
                            }
                    }
                    is ResultState.Error -> trySend(ResultState.Error(result.message))
                    is ResultState.Loading -> trySend(ResultState.Loading)
                }
            }
        }catch (e: Exception){
            trySend(ResultState.Error(e.message ?: "Unexpected error"))
        }
        awaitClose { close() }
    }

    override fun deleteBannerById(bannerId: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val bannerRef = firebaseFirestore.collection(BANNER_COLLECTION).document(bannerId)
        bannerRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot.exists()){
                    val banner = documentSnapshot.toObject(BannerDataModels::class.java)
                    banner?.image?.let { imageUrl ->
                        // Extract object key from URL or use a stored key if available
                        val objectKey = imageUrl.substringAfterLast("/") // This is a basic way, might need adjustment
                        launch { // Launch a coroutine to collect the flow
                            amplifyStorageHelper.deleteFile(objectKey).collectLatest { deleteResult ->
                                when (deleteResult) {
                                    is ResultState.Success -> {
                                        // Image deleted successfully, now delete banner document
                                        bannerRef.delete()
                                            .addOnSuccessListener {
                                                trySend(ResultState.Success("Banner and image deleted successfully"))
                                            }
                                            .addOnFailureListener { e ->
                                                trySend(ResultState.Error(e.message ?: "Failed to delete banner document"))
                                            }
                                    }
                                    is ResultState.Error -> trySend(ResultState.Error(deleteResult.message))
                                    is ResultState.Loading -> trySend(ResultState.Loading) // Or handle appropriately
                                }
                            }
                        }
                    }?: bannerRef.delete() // If no image URL, just delete the document
                        .addOnSuccessListener { trySend(ResultState.Success("Banner deleted successfully (no image to delete)")) }
                        .addOnFailureListener { e -> trySend(ResultState.Error(e.message ?: "Delete failed")) }
                }else{
                    trySend(ResultState.Error("Banner not found"))
                }
            }
        awaitClose { close() }
    }
}