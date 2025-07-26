package com.example.shopapp.presentation.Screens.AdminScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shopapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrEditProductScreenUI(navController: NavController, productId: String? = null) {

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {
                Text(
                    text = if (productId == null) "Create Product" else "Edit product",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 10.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(R.color.purple_200),
                titleContentColor = MaterialTheme.colorScheme.background
            )
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreateOrEditProductScreenPreview() {
    val navController = NavController(LocalContext.current)
    CreateOrEditProductScreenUI(navController = navController, productId = null)
}