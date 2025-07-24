package com.example.shopapp.data.repo

import android.content.Context
import android.net.Uri
import com.example.shopapp.common.ADD_TO_CART
import com.example.shopapp.common.ADD_TO_FAV
import com.example.shopapp.common.PRODUCT_COLLECTION
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
                    if (it.isSuccessful) {
                        trySend(ResultState.Success("User data update successfully"))
                    } else {
                        trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                    }
                }
            awaitClose { close() }
        }
    override fun updateUserProfileImage(context: Context, uri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            val userId = firebaseAuth.currentUser?.uid ?: throw Exception("User not authenticated")
            val objectKey = "profile_images/$userId/${System.currentTimeMillis()}"
            awsHelper.uploadFileFromUri(context, uri, objectKey).collect { result ->
                when (result) {
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
        awaitClose { close() }
    }

    override fun getCategoriesInLimited(): Flow<ResultState<List<CategoryDataModel>>> =
        callbackFlow {
            firebaseFirestore.collection("Categories").limit(7).get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    val categories =
                        queryDocumentSnapshots.documents.mapNotNull { documentSnapshot ->
                            documentSnapshot.toObject(CategoryDataModel::class.java)
                        }
                    trySend(ResultState.Success(categories))
                }.addOnFailureListener { exception ->
                    trySend(ResultState.Error(exception.toString()))
                }
            awaitClose { close() }
        }

    override fun getAllCategories(): Flow<ResultState<List<CategoryDataModel>>> = callbackFlow {

        trySend(ResultState.Loading)

        firebaseFirestore.collection("categories").get().addOnSuccessListener {

            val categories = it.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(CategoryDataModel::class.java)
            }
            trySend(ResultState.Success(categories))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.toString()))
        }
        awaitClose { close() }
    }

    override fun getProductsInLimit(): Flow<ResultState<List<ProductsDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection("Products").limit(10).get().addOnSuccessListener {
            val products = it.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(ProductsDataModel::class.java)?.apply {
                    productId = documentSnapshot.id
                }
            }
            trySend(ResultState.Success(products))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.toString()))
        }
        awaitClose { close() }
    }

    override fun getAllProducts(): Flow<ResultState<List<ProductsDataModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection("Products").get().addOnSuccessListener {
            val products = it.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(ProductsDataModel::class.java)?.apply {
                    productId = documentSnapshot.id
                }
            }
            trySend(ResultState.Success(products))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.toString()))
        }
        awaitClose { close() }
    }

    override fun getProductById(productId: String): Flow<ResultState<ProductsDataModel>> =
        callbackFlow {
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

    override fun addToCart(cartDataModel: CartDataModels): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(ADD_TO_CART).document(firebaseAuth.currentUser!!.uid)
                .collection("User_Cart").add(
                    cartDataModel
                ).addOnSuccessListener {
                    trySend(ResultState.Success("Product added to cart successfully"))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.toString()))
                }
            awaitClose { close() }
        }

    override fun addToFavourite(productsDataModel: ProductsDataModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(ADD_TO_FAV).document(firebaseAuth.currentUser!!.uid)
                .collection("User_Fav").add(productsDataModel).addOnSuccessListener {
                    trySend(ResultState.Success("Product added to favourites successfully"))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.toString()))
                }
            awaitClose { close() }
        }

    override fun getAllFavourites(): Flow<ResultState<List<ProductsDataModel>>> = callbackFlow {

        trySend(ResultState.Loading)

        firebaseFirestore.collection(ADD_TO_FAV).document(firebaseAuth.currentUser!!.uid)
            .collection("User_Fav")
            .get().addOnSuccessListener {
                val fav = it.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(ProductsDataModel::class.java)
                }
                trySend(ResultState.Success(fav))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }
        awaitClose { close() }
    }

    override fun getAllSuggestiveProducts(): Flow<ResultState<List<ProductsDataModel>>> =
        callbackFlow {

            trySend(ResultState.Loading)
            firebaseFirestore.collection(ADD_TO_FAV).document(firebaseAuth.currentUser!!.uid)
                .collection("User_Fav").get().addOnSuccessListener {

                val fav = it.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(ProductsDataModel::class.java)
                }
                trySend(ResultState.Success(fav))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }

            awaitClose { close() }

        }

    override fun getAllCarts(): Flow<ResultState<List<CartDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(ADD_TO_CART).document(firebaseAuth.currentUser!!.uid)
            .collection("User_Cart")
            .get().addOnSuccessListener {
                val cart = it.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(CartDataModels::class.java)?.apply {
                        cartId = documentSnapshot.id
                    }
                }
                trySend(ResultState.Success(cart))
            }
        awaitClose {
            close()
        }
    }

    override fun getCheckOut(productId: String): Flow<ResultState<ProductsDataModel>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection("Products").document(productId).get()
                .addOnSuccessListener {

                    val product = it.toObject(ProductsDataModel::class.java)
                    trySend(ResultState.Success(product!!))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.toString()))
                }

            awaitClose { close() }
        }

    override fun getBanners(): Flow<ResultState<List<BannerDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection("banner").get().addOnSuccessListener {
            val banner = it.toObjects(BannerDataModels::class.java)
            trySend(ResultState.Success(banner))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.toString()))
        }
        awaitClose { close() }
    }

    override fun getSpecificCategory(categoryName: String): Flow<ResultState<List<ProductsDataModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection("Products").whereEqualTo("categories", categoryName).get()
                .addOnSuccessListener {

                    val products = it.documents.mapNotNull { documentSnapshot ->
                        documentSnapshot.toObject(ProductsDataModel::class.java)?.apply {
                            productId = documentSnapshot.id
                        }
                    }
                    trySend(ResultState.Success(products))
                }.addOnSuccessListener {
                    trySend(ResultState.Error(it.toString()))
                }
            awaitClose { close() }
        }

    suspend fun initializeAWS() {
        awsHelper.initAWS()
    }

    fun deleteFileFromS3(objectKey: String): Flow<ResultState<Boolean>> {
        return awsHelper.deleteFile(objectKey)
    }

    fun getS3FileUrl(objectKey: String): String {
        return awsHelper.getFileUrl(objectKey)
    }
}