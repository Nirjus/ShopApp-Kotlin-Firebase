package com.example.shopapp.domain.models

import androidx.compose.runtime.mutableStateMapOf

data class UserData(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val address: String = "",
    val image: String = "",
    val userRole: String = "USER"  // Default role is USER
)
{
    fun toMap() : Map<String, Any>{
        val map = mutableStateMapOf<String, Any>()

        map["firstName"] = firstName
        map["lastName"] = lastName
        map["email"] = email
        map["password"] = password
        map["address"] = address
        map["image"] = image
        map["phoneNumber"] = phoneNumber
        map["userRole"] = userRole

        return map
    }
}

// Role constants
object UserRoles {
    const val USER = "USER"
    const val ADMIN = "ADMIN"
}

data class UserDataParent(val nodeId: String = "", val userData: UserData = UserData())