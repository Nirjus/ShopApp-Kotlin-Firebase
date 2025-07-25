package com.example.shopapp.presentation.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shopapp.domain.models.ProductsDataModel
import com.example.shopapp.presentation.utils.CircularIndicator
import com.example.shopapp.presentation.viewModels.ShoppingAppViewModel
import com.example.shopapp.R
import com.example.shopapp.domain.models.BannerDataModels
import com.example.shopapp.presentation.Navigation.Routes
import com.example.shopapp.presentation.utils.Banner

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    val homeState by viewModel.homeScreenState.collectAsStateWithLifecycle()
    val getAllSuggestedProduct =
        viewModel.getAllSuggestedProductsState.collectAsStateWithLifecycle()
    val getAllSuggestedProductData: List<ProductsDataModel> =
        getAllSuggestedProduct.value.userData.orEmpty().filterNotNull()
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllSuggestedProduct()
    }
    if (homeState.isLoading) {
        CircularIndicator()
    } else if (homeState.errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: " + homeState.errorMessage!!)
        }
    } else {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Search products") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search icon"
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(9.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    IconButton(onClick = {/* Notification handler */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notification icon",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                // category section
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Categories", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "See more>",
                            color = colorResource(R.color.purple_300),
                            modifier = Modifier.clickable {
                                navController.navigate(Routes.AllCategoryScreen)
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(homeState.categories) { category ->
                            CategoryItem(
                                imageUrl = category!!.image,
                                categoryName = category.name,
                                onClick = {
                                    navController.navigate(
                                        Routes.CategoryWiseProductsScreen(category.name)
                                    )
                                }
                            )
                        }
                    }
                }
                homeState.banners.let { banner ->
                    Banner(banner as List<BannerDataModels>)
                }
                // flash sells section
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Flash sells", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "See more>",
                            color = colorResource(id = R.color.purple_300),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.clickable { navController.navigate(Routes.SeeAllProductScreen) })
                    }
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(homeState.products) { product ->
                            ProductCard(
                                product = product!!,
                                onClick = {
                                    navController.navigate(
                                        Routes.ProductDetailsScreen(product.productId)
                                    )
                                })
                        }
                    }
                }
                // build the suggested product
                Column(modifier = Modifier.padding(top = 16.dp, bottom = 5.dp)) {
                    when {
                        getAllSuggestedProduct.value.isLoading -> {
                            CircularIndicator()
                        }

                        getAllSuggestedProduct.value.errorMessage != null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    getAllSuggestedProduct.value.errorMessage
                                        ?: "Error in fetching data"
                                )
                            }
                        }

                        getAllSuggestedProductData.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No suggested product available")
                            }
                        }

                        else -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Suggested for you", style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "See more>",
                                    color = colorResource(id = R.color.purple_300),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.clickable { navController.navigate(Routes.SeeAllProductScreen) })
                            }
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(getAllSuggestedProductData) { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = {
                                            navController.navigate(
                                                Routes.ProductDetailsScreen(product.productId)
                                            )
                                        })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(imageUrl: String, categoryName: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .clickable { onClick() }) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.LightGray, CircleShape)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "category image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
        }
        Text(categoryName, style = MaterialTheme.typography.bodyMedium)
    }
}

// It is the flash sells sections
@Composable
fun ProductCard(product: ProductsDataModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable { onClick() }
            .aspectRatio(0.7f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .width(100.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Rs.${product.finalPrice}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Rs.${product.price}",
                        style = MaterialTheme.typography.bodySmall,
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${product.availableUnites} lefts",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                }
            }
        }
    }
}