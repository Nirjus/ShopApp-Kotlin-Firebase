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
    val createdAt: String = "",
    val availableUnites:Int  = 0,

    )
