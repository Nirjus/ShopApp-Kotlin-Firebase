package com.example.shopapp.common

import com.example.shopapp.domain.models.UserData
import com.example.shopapp.domain.models.UserDataParent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object UserUtils {
    private val firestore = FirebaseFirestore.getInstance()

    fun getCurrentUser(firebaseAuth: FirebaseAuth): Flow<ResultState<UserDataParent>> = callbackFlow {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            trySend(ResultState.Error("No user logged in"))
            close()
            return@callbackFlow
        }

        trySend(ResultState.Loading)

        // Add real-time listener for user data changes
        val listener = firestore.collection(USER_COLLECTION)
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ResultState.Error(error.localizedMessage ?: "Error getting user data"))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val userData = snapshot.toObject(UserData::class.java)
                    if (userData != null) {
                        trySend(ResultState.Success(UserDataParent(snapshot.id, userData)))
                    }
                } else {
                    trySend(ResultState.Error("User data not found"))
                }
            }

        awaitClose {
            listener.remove()
        }
    }
}
