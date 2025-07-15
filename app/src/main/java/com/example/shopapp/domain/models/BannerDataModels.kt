package com.example.shopapp.domain.models

data class BannerDataModels(
    var name: String = "",
    val image: String = "",
    val data: Long = System.currentTimeMillis()
)
