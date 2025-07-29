package com.example.shopapp.domain.models

data class BannerDataModels(
    var bannerId: String = "",
    var name: String = "",
    val image: String = "",
    val data: Long = System.currentTimeMillis()
)
