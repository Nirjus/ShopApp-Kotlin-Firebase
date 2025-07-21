package com.example.shopapp.presentation.Screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.shopapp.presentation.viewModels.ShoppingAppViewModel

@Composable
fun EachCategoryProductScreen(
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    navController: NavController,
    categoryName: String
){

}