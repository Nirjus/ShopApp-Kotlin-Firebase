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

        return map
    }
}

data class UserDataParent(val nodeId: String = "", val userData: UserData = UserData())