package com.example.shopapp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductsDataModel(

    var productId: String = "",
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val finalPrice: String = "",
    val category: String = "",
    val image: String = "",
    val date: Long = System.currentTimeMillis(),
    val createBy: String = "",
    val availableUnites:Int  = 0,

    )
