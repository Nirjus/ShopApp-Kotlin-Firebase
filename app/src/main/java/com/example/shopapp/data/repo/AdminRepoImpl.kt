package com.example.shopapp.data.repo

import android.content.Context
import android.net.Uri
import com.example.shopapp.common.ResultState
import com.example.shopapp.data.di.AWSHelper
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
    private val awsHelper: AWSHelper
): AdminRepo {
    override fun createProduct(context: Context, productDataModels: ProductsDataModel, imageUri: Uri): Flow<ResultState<String>> =
        callbackFlow{
            trySend(ResultState.Loading)
            try {
                val objectKey = "product_images/${System.currentTimeMillis()}"
                awsHelper.uploadFileFromUri(context, imageUri, objectKey).collect { result ->
                    when (result) {
                        is ResultState.Success -> {
                            val imageUrl = result.data
                            val finalProduct = productDataModels.copy(image = imageUrl) // Assuming 'image' field

                            firebaseFirestore.collection("Products")
                                .add(finalProduct)
                                .addOnSuccessListener {
                                    trySend(ResultState.Success("Product added successfully"))
                                }
                                .addOnFailureListener {
                                    trySend(ResultState.Error(it.message ?: "Upload failed"))
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
        val productDocRef = firebaseFirestore.collection("Products").document(productId)
        productDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val product = documentSnapshot.toObject(ProductsDataModel::class.java)
                    product?.image?.let { imageUrl ->
                        // Extract object key from URL or use a stored key if available
                        val objectKey = imageUrl.substringAfterLast("/") // This is a basic way, might need adjustment
                        launch { // Launch a coroutine to collect the flow
                            awsHelper.deleteFile("product_images/$objectKey").collectLatest { deleteResult ->
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
            awsHelper.uploadFileFromUri(context, imageUri, objectKey).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        val imageUrl = result.data
                        val finalCategory = categoryDataModel.copy(image = imageUrl) // Assuming 'image' field
                        firebaseFirestore.collection("Categories")
                            .add(finalCategory)
                            .addOnSuccessListener {
                                trySend(ResultState.Success("Category added successfully"))
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
        val categoryDocRef = firebaseFirestore.collection("Categories").document(categoryId)
        categoryDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val category = documentSnapshot.toObject(CategoryDataModel::class.java)
                    category?.image?.let { imageUrl ->
                        // Extract object key from URL or use a stored key if available
                        val objectKey = imageUrl.substringAfterLast("/") // This is a basic way, might need adjustment
                        launch { // Launch a coroutine to collect the flow
                            awsHelper.deleteFile("category_images/$objectKey").collectLatest { deleteResult ->
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
}