package com.example.shopapp.domain.models

data class CategoryDataModel(
    var name: String = "",
    var image: String,
    var date: Long = System.currentTimeMillis(),
    var createdBy: String,
)
