package com.example.shopapp.common

import com.example.shopapp.domain.models.BannerDataModels
import com.example.shopapp.domain.models.CategoryDataModel
import com.example.shopapp.domain.models.ProductsDataModel

data class HomeScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val categories: List<CategoryDataModel?> = emptyList(),
    val products: List<ProductsDataModel?> = emptyList(),
    val banners: List<BannerDataModels?> = emptyList()
)